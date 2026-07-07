package com.system.wallet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TransferWalletRequest {

    @NotNull(message = "your wallet is required")
    private int from_wallet_id;

    @NotNull(message = "trader wallet is required")
    private int to_wallet_id;

    @NotNull(message = "not valid value")
    @Size(min = 10)
    private Double amount;


    @NotBlank(message = "phone number is required")

    public String phoneNumber;


    @NotBlank(message = "Otp code is required")
    @Pattern(regexp = "^\\d{4,8}$",message = "OTP code must contain digits only")
    public String code;

    private String types;

    private int ref_no;




    public TransferWalletRequest() {

    }

    public TransferWalletRequest(int from_wallet_id, int to_wallet_id, Double amount, String phoneNumber, String code) {
        this.from_wallet_id = from_wallet_id;
        this.to_wallet_id = to_wallet_id;
        this.amount = amount;
        this.phoneNumber = phoneNumber;
        this.code = code;
    }

    public int getFrom_wallet_id() {
        return from_wallet_id;
    }

    public void setFrom_wallet_id(int from_wallet_id) {
        this.from_wallet_id = from_wallet_id;
    }

    public int getTo_wallet_id() {
        return to_wallet_id;
    }

    public void setTo_wallet_id(int to_wallet_id) {
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

    public int getRef_no() {
        return ref_no;
    }

    public void setRef_no(int ref_no) {
        this.ref_no = ref_no;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "TransferWalletRequest{" +
                "amount=" + amount +
                ", types='" + types + '\'' +
                '}';
    }
}
