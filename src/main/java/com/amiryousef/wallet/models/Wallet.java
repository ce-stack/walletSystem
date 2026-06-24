package com.amiryousef.wallet.models;

import jakarta.persistence.*;

import java.util.Date;

//@Entity
//@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "currency")
    private String currency;

    @Column(name = "status")
    private String status;

    @Column(name = "version")
    private int version;

    @Column(name = "date")
    private Date date;
}
