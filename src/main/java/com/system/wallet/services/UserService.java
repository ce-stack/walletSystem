package com.system.wallet.services;

import com.system.wallet.dto.request.WalletRequest;
import com.system.wallet.models.Wallet;
import com.system.wallet.payload.ApiResponse;
import com.system.wallet.repositories.UserRepositoryCustom;

public class UserService {

    private UserRepositoryCustom userRepositoryCustom;

    public UserService(UserRepositoryCustom userRepositoryCustom) {
        this.userRepositoryCustom = userRepositoryCustom;
    }

    public ApiResponse create_wallet(WalletRequest walletRequest , int id) {
        Wallet wallet = new Wallet();

        wallet.setId(id);
        wallet.setBalance(walletRequest.getBalance());
        wallet.setCurrency(walletRequest.getCuurency());
        wallet.setStatus(walletRequest.getStatus());
        wallet.setVersion(walletRequest.getVersion());
        return new ApiResponse("wallet created!" , true , 200);
    }
}
