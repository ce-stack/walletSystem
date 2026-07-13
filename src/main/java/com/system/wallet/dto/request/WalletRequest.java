package com.system.wallet.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class WalletRequest {

    @NotNull(message = "Balance is required")
    private Double balance;

    @NotNull(message = "Phone number is required")
    @Size(min = 4 , max = 14)
    @Pattern(regexp = "^\\+[1-9]\\d{7,14}$",message = "Phone number must be in E.164 format")
    private String phone_number;


    public WalletRequest() {

    }

    public WalletRequest(Double balance, String phone_number) {
        this.balance = balance;
        this.phone_number = phone_number;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
