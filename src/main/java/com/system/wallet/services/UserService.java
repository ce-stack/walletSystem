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

        Wallet fromWallet = getWalletOrThrow(fromWalletId);
        Wallet toWallet = getWalletOrThrow(toWalletId);

        if (!checkIfWalletsNotTheSame(fromWalletId, toWalletId)) {
            return new ApiResponse("You cannot transfer to the same wallet", false, 400);
        }

        if (!checkWalletStatus(fromWallet)) {
            return new ApiResponse("Sender wallet is not active", false, 400);
        }

        if (!checkWalletStatus(toWallet)) {
            return new ApiResponse("Receiver wallet is not active", false, 400);
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(transferWalletRequest.getAmount());
        transaction.setTo_wallet_id(fromWallet);
        transaction.setFrom_wallet_id(toWallet);
        transaction.setStatus("active");
        transaction.setRef_no(123);
        transaction.setTypes("transfer");

        transactionRepository.save(transaction);
        afterPayemnt(fromWalletId , toWalletId , toWallet.getBalance(), fromWallet.getBalance() , transferWalletRequest.getAmount());
        return new ApiResponse("transfer done !", true, 200);
    }

    private Wallet getWalletOrThrow(Integer walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet with id " + walletId + " not found"));
    }

    private boolean checkIfWalletsNotTheSame(Integer fromWalletId, Integer toWalletId) {
        return !fromWalletId.equals(toWalletId);
    }

    private boolean checkWalletStatus(Wallet wallet) {
        return wallet != null
                && wallet.getStatus() != null
                && wallet.getStatus().trim().equalsIgnoreCase("active");
    }

    @Transactional
    private void afterPayemnt(Integer fromWalletId, Integer toWalletId , Double toBalance , Double fromBalance , Double amount) {
        Wallet fromWallet = walletRepository.findById(fromWalletId).orElseThrow(() -> new RuntimeException("Wallet with id " + fromWalletId + " not found"));
        Wallet toWallet = walletRepository.findById(toWalletId).orElseThrow(() -> new RuntimeException("Wallet with id " + toWalletId + " not found"));

        Double newBalanceFrom = fromWallet.getBalance() - amount;
        fromWallet.setBalance(newBalanceFrom);
        walletRepository.save(fromWallet);

        Double newBalanceTo = toWallet.getBalance() + amount;
        toWallet.setBalance(newBalanceTo);
        walletRepository.save(toWallet);

    }


}
