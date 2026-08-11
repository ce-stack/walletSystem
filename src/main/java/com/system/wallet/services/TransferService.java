package com.system.wallet.services;

import com.system.wallet.config.auth.AuthUser;
import com.system.wallet.config.enums.LedgerType;
import com.system.wallet.config.enums.TransactionStatus;
import com.system.wallet.config.enums.TransactionType;
import com.system.wallet.config.enums.WalletStatus;
import com.system.wallet.dto.request.TransferWalletRequest;
import com.system.wallet.dto.request.WalletRequest;
import com.system.wallet.models.*;
import com.system.wallet.payload.ApiResponse;
import com.system.wallet.repositories.*;
import com.system.wallet.services.otp.TwilioVerifyOtpService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TransferService {

    private UserRepositoryCustom userRepositoryCustom;
    private UserRepository userRepository;
    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;
    private LedgerEntryRepository ledgerEntryRepository;
    private TwilioVerifyOtpService twilioVerifyOtpService;
    private IdempotencyKeyRepository idempotencyKeyRepository;
    private AuthUser authUser;
    private OutboxEventsRepository outboxEventsRepository;

    public TransferService(UserRepositoryCustom userRepositoryCustom , UserRepository userRepository , WalletRepository walletRepository , TransactionRepository transactionRepository , LedgerEntryRepository ledgerEntryRepository , TwilioVerifyOtpService twilioVerifyOtpService , AuthUser authUser , IdempotencyKeyRepository idempotencyKeyRepository , OutboxEventsRepository outboxEventsRepository )
    {
        this.userRepositoryCustom = userRepositoryCustom;
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.ledgerEntryRepository = ledgerEntryRepository;
        this.twilioVerifyOtpService = twilioVerifyOtpService;
        this.idempotencyKeyRepository = idempotencyKeyRepository;
        this.authUser = authUser;
        this.outboxEventsRepository = outboxEventsRepository;
    }

    public ApiResponse create_wallet(WalletRequest walletRequest) {
        Wallet wallet = new Wallet();

        User user = authUser.user();
        wallet.setUser(user);
        wallet.setBalance(walletRequest.getBalance());
        wallet.setCurrency("EGP");
        wallet.setStatus(WalletStatus.ACTIVE);
        wallet.setPhone_number(walletRequest.getPhone_number());
        wallet.setVersion(1);

        userRepositoryCustom.creteWallet(wallet);
        return new ApiResponse("wallet created!" , true , 200);
    }

    @Transactional
    public ApiResponse transferToWallet(TransferWalletRequest transferWalletRequest , String key) {

        Integer fromWalletId = transferWalletRequest.getFrom_wallet_id();
        Integer toWalletId = transferWalletRequest.getTo_wallet_id();
        Double amount = transferWalletRequest.getAmount();

        if (fromWalletId == null || toWalletId == null) {
            return new ApiResponse("Wallet ids are required", false, 400);
        }

        if (!checkIfWalletsNotTheSame(fromWalletId, toWalletId)) {
            return new ApiResponse("You cannot transfer to the same wallet", false, 400);
        }

        if (amount == null || amount <= 0) {
            return new ApiResponse("Amount must be greater than zero", false, 400);
        }
        Wallet fromWallet;
        Wallet toWallet;
        if (fromWalletId < toWalletId) {
            fromWallet = getWalletForUpdateOrThrow(fromWalletId);
            toWallet = getWalletForUpdateOrThrow(toWalletId);
        } else {
            toWallet = getWalletForUpdateOrThrow(toWalletId);
            fromWallet = getWalletForUpdateOrThrow(fromWalletId);
        }

        if (!checkWalletStatus(fromWallet)) {
            return new ApiResponse("Sender wallet is not active", false, 400);
        }

        if (!checkWalletStatus(toWallet)) {
            return new ApiResponse("Receiver wallet is not active", false, 400);
        }

        if (fromWallet.getBalance() < amount) {
            return new ApiResponse("Insufficient balance", false, 400);
        }

        Map<String , Object> data = new HashMap<>();
        data.put("sender phone number", fromWallet.getPhone_number());
        data.put("sender wallet number" , fromWallet.getId());
        data.put("receiver wallet number", toWallet.getId());
        data.put("the amount to be transferred", amount);
        twilioVerifyOtpService.sendOtp(transferWalletRequest.getPhoneNumber());
        Transaction transaction = storeTheTransaction(transferWalletRequest, fromWallet, toWallet);
        User user = authUser.user();
        boolean createedKey = createKeyIfExists(user , key , transaction);
        if(!createedKey) {
            return new ApiResponse("Key does not exist", false, 400);
        }
        Ledger_entry sender = createLedgerEntry(transaction , fromWallet , LedgerType.SENDER);
        Ledger_entry receiver = createLedgerEntry(transaction , toWallet , LedgerType.RECEIVER);
       // Outbox_event outbox_event = creatOutBoxEvent(transaction , );
        return new ApiResponse("transfer is pending enter the otp check your SMS!" ,  true, 200 , data
        );
    }

    private Transaction storeTheTransaction(TransferWalletRequest transferWalletRequest,Wallet fromWallet,Wallet toWallet) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transferWalletRequest.getAmount());
        transaction.setFrom_wallet_id(fromWallet);
        transaction.setTo_wallet_id(toWallet);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setRef_no(generateRefNo());
        transaction.setTypes(TransactionType.TRANSFER);
        return transactionRepository.save(transaction);
    }

    private boolean checkIfWalletsNotTheSame(Integer fromWalletId, Integer toWalletId) {
        return !fromWalletId.equals(toWalletId);
    }

    private boolean checkWalletStatus(Wallet wallet) {
        return wallet.getStatus() == WalletStatus.ACTIVE;
    }

    private Wallet getWalletForUpdateOrThrow(Integer walletId) {
        return walletRepository.findByIdForUpdate(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet with id " + walletId + " not found"));
    }

    private Integer generateRefNo() {
        return (int) (System.currentTimeMillis() % 1000000000);
    }

    private Ledger_entry createLedgerEntry(Transaction transaction , Wallet wallet, LedgerType ledgerType) {
        Ledger_entry ledger_entry = new Ledger_entry();
        Double balanceAfter = calcBalanceAfter(wallet , transaction , ledgerType);
        ledger_entry.setAmount(transaction.getAmount());
        ledger_entry.setWallet_id(wallet);
        ledger_entry.setBalance_after(balanceAfter);
        ledger_entry.setTypes(ledgerType);
        ledger_entry.setCreated_at(new Date());
        ledger_entry.setTransaction_id(transaction);
        return ledgerEntryRepository.save(ledger_entry);
    }

    private Double calcBalanceAfter(Wallet wallet, Transaction transaction , LedgerType ledgerType) {
        switch (ledgerType) {
            case SENDER:
                return wallet.getBalance() - (transaction.getAmount());
            case RECEIVER:
                return wallet.getBalance() + (transaction.getAmount());
            default:
                throw new RuntimeException("Invalid ledger type");
        }
    }

    private boolean createKeyIfExists(User user , String key , Transaction transaction) {
        if(idempotencyKeyRepository.existsByKey(key)) {
            return false;
        }
        Idempotency_key idempotency_key = new Idempotency_key();
        idempotency_key.setKey(key);
        idempotency_key.setRequest_hash(UUID.randomUUID().toString());
        idempotency_key.setTransaction_id(transaction);
        idempotency_key.setUser_id(user);
        idempotency_key.setStatus("success");
        idempotencyKeyRepository.save(idempotency_key);
        return true;
    }

    private void creatOutBoxEvent(Long aggerate_id, String payLoad){
        Outbox_event outbox_event = new Outbox_event();
        outbox_event.setAggerate_id(aggerate_id);
        outbox_event.setPayload(payLoad);
        outbox_event.setStatus("pending");
        outbox_event.setEvent_type("transfer");
        outbox_event.setPublished_at(new Date());
        outbox_event.setCreated_at(new Date());
        outboxEventsRepository.save(outbox_event);
    }
}
