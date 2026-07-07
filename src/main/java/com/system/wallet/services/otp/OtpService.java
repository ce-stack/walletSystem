package com.system.wallet.services.otp;

import com.system.wallet.dto.request.TransferWalletRequest;
import com.system.wallet.dto.request.otp.VerifyOtpRequest;

public interface OtpService {

    void sendOtp(String phoneNumber);

    boolean verifyOtp(VerifyOtpRequest verifyOtpRequest);
}
