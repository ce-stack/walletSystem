package com.system.wallet.services;

import com.system.wallet.config.LedgerType;
import com.system.wallet.config.TransactionStatus;
import com.system.wallet.config.TransactionType;
import com.system.wallet.config.WalletStatus;
import com.system.wallet.dto.request.TransferWalletRequest;
import com.system.wallet.dto.request.WalletRequest;
import com.system.wallet.models.Ledger_entry;
import com.system.wallet.models.Transaction;
import com.system.wallet.models.User;
import com.system.wallet.models.Wallet;
import com.system.wallet.payload.ApiResponse;
import com.system.wallet.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransferService {

    private UserRepositoryCustom userRepositoryCustom;
    private UserRepository userRepository;
    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;
    private LedgerEntryRepository ledgerEntryRepository;

    public TransferService(UserRepositoryCustom userRepositoryCustom , UserRepository userRepository , WalletRepository walletRepository , TransactionRepository transactionRepository , LedgerEntryRepository ledgerEntryRepository) {
        this.userRepositoryCustom = userRepositoryCustom;
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.ledgerEntryRepository = ledgerEntryRepository;
    }

    public ApiResponse create_wallet(WalletRequest walletRequest) {
        Wallet wallet = new Wallet();
        User user = userRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Default user with id 1 not found"));
        wallet.setUser(user);
        wallet.setBalance(walletRequest.getBalance());
        wallet.setCurrency(walletRequest.getCurrency());
        wallet.setStatus(WalletStatus.ACTIVE);
        wallet.setVersion(walletRequest.getVersion());

        userRepositoryCustom.creteWallet(wallet);
        return new ApiResponse("wallet created!" , true , 200);
    }

    @Transactional
    public ApiResponse transferToWallet(TransferWalletRequest transferWalletRequest) {

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
       Transaction transaction = storeTheTransaction(transferWalletRequest, fromWallet, toWallet);
       Ledger_entry sender = createLedgerEntry(transaction , fromWallet , LedgerType.SENDER);
       Ledger_entry receiver = createLedgerEntry(transaction , toWallet , LedgerType.RECEIVER);
       afterPayment(fromWallet, toWallet, amount);
        return new ApiResponse("transfer done!", true, 200);
    }

    private Transaction storeTheTransaction(TransferWalletRequest transferWalletRequest,Wallet fromWallet,Wallet toWallet) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transferWalletRequest.getAmount());
        transaction.setFrom_wallet_id(fromWallet);
        transaction.setTo_wallet_id(toWallet);
        transaction.setStatus(TransactionStatus.DONE);
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

    private void afterPayment(Wallet fromWallet, Wallet toWallet, Double amount) {
        Double newBalanceFrom = fromWallet.getBalance() - amount;
        fromWallet.setBalance(newBalanceFrom);
        Double newBalanceTo = toWallet.getBalance() + amount;
        toWallet.setBalance(newBalanceTo);
        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);
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
        ledger_entry.setWallet_id(transaction.getFrom_wallet_id());
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
}
