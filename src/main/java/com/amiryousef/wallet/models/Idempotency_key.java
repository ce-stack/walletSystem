package com.amiryousef.wallet.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "idempotency_keys")
public class Idempotency_key {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user_id;

    @Column(name = "key")
    private String key;

    @Column(name = "request_hash")
    private String request_hash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction_id;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private Date created_at;

    public Idempotency_key() {

    }

    public Idempotency_key(User user_id, String key, String request_hash, Transaction transaction_id, String status, Date created_at) {
        this.user_id = user_id;
        this.key = key;
        this.request_hash = request_hash;
        this.transaction_id = transaction_id;
        this.status = status;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRequest_hash() {
        return request_hash;
    }

    public void setRequest_hash(String request_hash) {
        this.request_hash = request_hash;
    }

    public Transaction getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(Transaction transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Idempotency_key{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", request_hash='" + request_hash + '\'' +
                ", status='" + status + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
