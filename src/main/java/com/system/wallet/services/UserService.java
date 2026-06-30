package com.system.wallet.services;

import com.system.wallet.dto.request.WalletRequest;
import com.system.wallet.models.User;
import com.system.wallet.models.Wallet;
import com.system.wallet.payload.ApiResponse;
import com.system.wallet.repositories.UserRepository;
import com.system.wallet.repositories.UserRepositoryCustom;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    private UserRepositoryCustom userRepositoryCustom;
    private UserRepository userRepository;

    public UserService(UserRepositoryCustom userRepositoryCustom , UserRepository userRepository) {
        this.userRepositoryCustom = userRepositoryCustom;
        this.userRepository = userRepository;


    }

    public ApiResponse create_wallet(WalletRequest walletRequest) {
        Wallet wallet = new Wallet();
        User user = userRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Default user with id 1 not found"));
        wallet.setUser(user);
        wallet.setBalance(walletRequest.getBalance());
        wallet.setCurrency(walletRequest.getCurrency());
        wallet.setStatus(walletRequest.getStatus());
        wallet.setVersion(walletRequest.getVersion());

        userRepositoryCustom.creteWallet(wallet);
        return new ApiResponse("wallet created!" , true , 200);
    }
}
