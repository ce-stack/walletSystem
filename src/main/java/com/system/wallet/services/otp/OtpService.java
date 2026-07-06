package com.system.wallet.services.otp;

public interface OtpService {

    void sendOtp(String phoneNumber);

    boolean verifyOtp(String phoneNumber , String code);
}
