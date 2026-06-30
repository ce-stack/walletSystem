package com.system.wallet.dto.request;

import jakarta.validation.constraints.NotNull;

public class WalletRequest {

    @NotNull(message = "User is required")
    private int user_id;

    @NotNull(message = "Balance is required")
    private Double balance;

    @NotNull(message = "Currency is required")
    private String currency;

    private String status;
    private int version;

    public WalletRequest() {

    }

    public WalletRequest(int user_id, Double balance, String currency) {
        this.user_id = user_id;
        this.balance = balance;
        this.currency = currency;
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
                ", status='" + status + '\'' +
                ", version=" + version +
                '}';
    }
}
