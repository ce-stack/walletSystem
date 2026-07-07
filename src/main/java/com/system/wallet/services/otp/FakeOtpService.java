package com.system.wallet.services.otp;

import com.system.wallet.dto.request.TransferWalletRequest;
import com.system.wallet.dto.request.otp.VerifyOtpRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "otp.provider" , havingValue = "fake")
public class FakeOtpService implements OtpService{


    private final String fakeCode;

    public FakeOtpService(@Value("${otp.fake-code}") String fakeCode) {
        this.fakeCode = fakeCode;
    }

    @Override
    public void sendOtp(String phoneNumber) {

    }

    @Override
    public boolean verifyOtp(VerifyOtpRequest verifyOtpRequest) {
        return fakeCode.equals(verifyOtpRequest.getCode());
    }
}
