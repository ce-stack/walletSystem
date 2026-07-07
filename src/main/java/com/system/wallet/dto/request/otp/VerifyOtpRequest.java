package com.system.wallet.dto.request.otp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class VerifyOtpRequest {

    @NotNull(message = "phone number is required")
    @Pattern(regexp = "^\\+[1-9]\\d{7,14}$",message = "Phone number must be in E.164 format")
    private String fromPhoneNumber;


    @NotNull(message = "the sender is required")
    private int from_wallet_id;

    @NotNull(message = "the receiver is required")
    private int to_wallet_id;

    @NotBlank(message = "Otp code is required")
    @Pattern(regexp = "^\\d{4,8}$",message = "OTP code must contain digits only")
    public String code;

    private Double amount;

    public VerifyOtpRequest(String fromPhoneNumber, int from_wallet_id, int to_wallet_id, String code ,  Double amount) {
        this.fromPhoneNumber = fromPhoneNumber;
        this.from_wallet_id = from_wallet_id;
        this.to_wallet_id = to_wallet_id;
        this.code = code;
        this.amount = amount;
    }


    public String getFromPhoneNumber() {
        return fromPhoneNumber;
    }

    public void setFromPhoneNumber(String fromPhoneNumber) {
        this.fromPhoneNumber = fromPhoneNumber;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
