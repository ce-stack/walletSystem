package com.system.wallet.services;

import com.system.wallet.dto.request.TransferWalletRequest;
import com.system.wallet.dto.request.WalletRequest;
import com.system.wallet.models.User;
import com.system.wallet.models.Wallet;
import com.system.wallet.payload.ApiResponse;
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

    public UserService(UserRepositoryCustom userRepositoryCustom , UserRepository userRepository , WalletRepository walletRepository) {
        this.userRepositoryCustom = userRepositoryCustom;
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;


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

        System.out.println("FROM WALLET ID = " + fromWallet.getId());
        System.out.println("FROM WALLET STATUS = [" + fromWallet.getStatus() + "]");
        System.out.println("FROM WALLET STATUS LENGTH = " + fromWallet.getStatus().length());

        System.out.println("TO WALLET ID = " + toWallet.getId());
        System.out.println("TO WALLET STATUS = [" + toWallet.getStatus() + "]");
        System.out.println("TO WALLET STATUS LENGTH = " + toWallet.getStatus().length());

        if (!checkIfWalletsNotTheSame(fromWalletId, toWalletId)) {
            return new ApiResponse("You cannot transfer to the same wallet", false, 400);
        }

        if (!checkWalletStatus(fromWallet)) {
            return new ApiResponse("Sender wallet is not active", false, 400);
        }

        if (!checkWalletStatus(toWallet)) {
            return new ApiResponse("Receiver wallet is not active", false, 400);
        }

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
}
