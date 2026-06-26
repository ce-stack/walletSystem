package com.system.wallet.services;

import com.system.wallet.repositories.UserRepositoryCustom;

public class UserService {

    private UserRepositoryCustom userRepositoryCustom;

    public UserService(UserRepositoryCustom userRepositoryCustom) {
        this.userRepositoryCustom = userRepositoryCustom;
    }


}
