package com.system.wallet.dto.request;

import com.system.wallet.models.Transaction;
import com.system.wallet.models.Wallet;
import jakarta.validation.constraints.NotNull;

public class LedgerEntryRequest {


    @NotNull(message = "Transaction is required")
    private Transaction transaction;

    @NotNull(message = "Wallet is requried")
    private Wallet wallet;

    @NotNull(message = "Type is required")
    private String type;

    @NotNull(message = "Amount is required")
    private Double amount;

    @NotNull(message = "Balance is required")
    private Double balance_after;

   public LedgerEntryRequest(){

   }


    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getBalance_after() {
        return balance_after;
    }

    public void setBalance_after(Double balance_after) {
        this.balance_after = balance_after;
    }

    @Override
    public String toString() {
        return "LedgerEntryRequest{" +
                "transaction=" + transaction +
                ", wallet=" + wallet +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", balance_after=" + balance_after +
                '}';
    }
}
