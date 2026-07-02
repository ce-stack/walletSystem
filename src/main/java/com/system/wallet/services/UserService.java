package com.system.wallet.services;

import com.system.wallet.dto.request.TransferWalletRequest;
import com.system.wallet.dto.request.WalletRequest;
import com.system.wallet.models.Transaction;
import com.system.wallet.models.User;
import com.system.wallet.models.Wallet;
import com.system.wallet.payload.ApiResponse;
import com.system.wallet.repositories.TransactionRepository;
import com.system.wallet.repositories.UserRepository;
import com.system.wallet.repositories.UserRepositoryCustom;
import com.system.wallet.repositories.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    private UserRepositoryCustom userRepositoryCustom;
    private UserRepository userRepository;
    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;

    public UserService(UserRepositoryCustom userRepositoryCustom , UserRepository userRepository , WalletRepository walletRepository , TransactionRepository transactionRepository) {
        this.userRepositoryCustom = userRepositoryCustom;
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    public ApiResponse create_wallet(WalletRequest walletRequest) {
        Wallet wallet = new Wallet();
        User user = userRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Default user with id 1 not found"));
        wallet.setUser(user);
        wallet.setBalance(walletRequest.getBalance());
        wallet.setCurrency(walletRequest.getCurrency());
        wallet.setStatus(walletRequest.getStatus());
        wallet.setVersion(walletRequest.getVersion());

        userRepositoryCustom.creteWallet(wallet);
        return new ApiResponse("wallet created!" , true , 200);
    }

    @Transactional
    public ApiResponse transfer_to_wallet(TransferWalletRequest transferWalletRequest) {

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

        afterPayment(fromWallet, toWallet, amount);

        storeTheTransaction(transferWalletRequest, fromWallet, toWallet);

        return new ApiResponse("transfer done!", true, 200);
    }

    private void storeTheTransaction(TransferWalletRequest transferWalletRequest,Wallet fromWallet,Wallet toWallet) {
        Transaction transaction = new Transaction();

        transaction.setAmount(transferWalletRequest.getAmount());

        transaction.setFrom_wallet_id(fromWallet);
        transaction.setTo_wallet_id(toWallet);

        transaction.setStatus("done");
        transaction.setRef_no(generateRefNo());
        transaction.setTypes("transfer");

        transactionRepository.save(transaction);
    }

    private boolean checkIfWalletsNotTheSame(Integer fromWalletId, Integer toWalletId) {
        return !fromWalletId.equals(toWalletId);
    }

    private boolean checkWalletStatus(Wallet wallet) {
        return wallet != null
                && wallet.getStatus() != null
                && wallet.getStatus().trim().equalsIgnoreCase("active");
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
}
