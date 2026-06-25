package com.system.wallet.models;

import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_wallet_id")
    private Wallet from_wallet_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_wallet_id")
    private Wallet to_wallet_id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "types")
    private String types;

    @Column(name = "status")
    private String status;

    @Column(name = "ref_no")
    private int ref_no;

    @Column(name = "created_at")
    private Date created_at;

    public Transaction() {

    }

    public Transaction(Wallet from_wallet_id, Wallet to_wallet_id, Double amount, String types, String status, int ref_no, Date created_at) {
        this.from_wallet_id = from_wallet_id;
        this.to_wallet_id = to_wallet_id;
        this.amount = amount;
        this.types = types;
        this.status = status;
        this.ref_no = ref_no;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Wallet getFrom_wallet_id() {
        return from_wallet_id;
    }

    public void setFrom_wallet_id(Wallet from_wallet_id) {
        this.from_wallet_id = from_wallet_id;
    }

    public Wallet getTo_wallet_id() {
        return to_wallet_id;
    }

    public void setTo_wallet_id(Wallet to_wallet_id) {
        this.to_wallet_id = to_wallet_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRef_no() {
        return ref_no;
    }

    public void setRef_no(int ref_no) {
        this.ref_no = ref_no;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", types='" + types + '\'' +
                ", status='" + status + '\'' +
                ", ref_no=" + ref_no +
                ", created_at=" + created_at +
                '}';
    }
}
