package com.system.wallet.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "ledger_entries")
public class Ledger_entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet_id;

    @Column(name = "types")
    private String types;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "balance_after")
    private Double balance_after;

    @Column(name = "created_at")
    private Date created_at;

    public Ledger_entry() {

    }

    public Ledger_entry(Transaction transaction_id, Wallet wallet_id, String types, Double amount, Double balance_after, Date created_at) {
        this.transaction_id = transaction_id;
        this.wallet_id = wallet_id;
        this.types = types;
        this.amount = amount;
        this.balance_after = balance_after;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Transaction getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(Transaction transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Wallet getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(Wallet wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Ledger_entry{" +
                "id=" + id +
                ", types='" + types + '\'' +
                ", amount=" + amount +
                ", balance_after=" + balance_after +
                ", created_at=" + created_at +
                '}';
    }
}
