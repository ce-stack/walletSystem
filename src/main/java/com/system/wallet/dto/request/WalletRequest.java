package com.system.wallet.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class WalletRequest {

    @NotNull(message = "User is required")
    private int user_id;

    @NotNull(message = "Balance is required")
    private Double balance;

    @NotNull(message = "Currency is required")
    private String currency;

    @NotNull(message = "Phone number is required")
    @Size(min = 4 , max = 14)
    @Pattern(regexp = "^\\+[1-9]\\d{7,14}$",message = "Phone number must be in E.164 format")
    private String phone_number;

    private String status;
    private int version;

    public WalletRequest() {

    }

    public WalletRequest(int user_id, Double balance, String currency , String phone_number) {
        this.user_id = user_id;
        this.balance = balance;
        this.currency = currency;
        this.phone_number = phone_number;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "WalletRequest{" +
                "user_id=" + user_id +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", status='" + status + '\'' +
                ", version=" + version +
                '}';
    }
}
