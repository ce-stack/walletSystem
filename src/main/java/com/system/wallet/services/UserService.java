package com.system.wallet.services;

import com.system.wallet.dto.request.WalletRequest;
import com.system.wallet.models.Wallet;
import com.system.wallet.payload.ApiResponse;
import com.system.wallet.repositories.UserRepositoryCustom;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepositoryCustom userRepositoryCustom;

    public UserService(UserRepositoryCustom userRepositoryCustom) {
        this.userRepositoryCustom = userRepositoryCustom;
    }

    public ApiResponse create_wallet(WalletRequest walletRequest) {
        Wallet wallet = new Wallet();

        int id = walletRequest.getUser_id();
        wallet.setBalance(walletRequest.getBalance());
        wallet.setCurrency(walletRequest.getCurrency());
        wallet.setStatus(walletRequest.getStatus());
        wallet.setVersion(walletRequest.getVersion());
        return new ApiResponse("wallet created!" , true , 200);
    }
}
