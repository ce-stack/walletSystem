package com.system.wallet.services;

public class TwilioVerifyOtpService implements OtpService{
    @Override
    public void sendOtp(String phoneNumber) {

    }

    @Override
    public boolean verifyOtp(String phoneNumber, String code) {
        return false;
    }
}
