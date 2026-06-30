package com.system.wallet.controllers;


import com.system.wallet.dto.request.WalletRequest;
import com.system.wallet.payload.ApiResponse;
import com.system.wallet.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/wallets")
public class WalletController {

    private UserService userService;

    public WalletController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ApiResponse createWallet(@RequestBody WalletRequest walletRequest) {
        return userService.create_wallet(walletRequest);
    }

}
