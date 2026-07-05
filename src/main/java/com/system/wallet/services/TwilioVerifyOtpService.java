package com.system.wallet.services;


import com.system.wallet.config.twilio.TwilioConfig;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "otp.provider" , havingValue = "twilio")
public class TwilioVerifyOtpService implements OtpService{


    private final String accountSid;
    private final String authToken;
    private final String verifyServiceSid;


    @PostConstruct
    void initTwilio() {
        if(isBlank(accountSid) || isBlank(authToken) || isBlank(verifyServiceSid)) {
            throw new IllegalArgumentException("Twilio configuration parameters are not valid");
        }
        Twilio.init(accountSid, authToken);
    }


    public TwilioVerifyOtpService(@Value("${TWILIO_ACCOUNT_SID}") String accountSid,@Value("${TWILIO_AUTH_TOKEN}") String authToken,@Value("${TWILIO_VERIFY_SERVICE_SID}") String verifyServiceSid) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.verifyServiceSid = verifyServiceSid;

    }

    @Override
    public void sendOtp(String phoneNumber) {
        try {
            Verification.creator(
                    verifyServiceSid,
                    phoneNumber,
                    "sms"
            ).create();
        } catch (ApiException ex) {
            throw new IllegalStateException(ex);
        }
    }



    @Override
    public boolean verifyOtp(String phoneNumber, String code) {
        try {
            VerificationCheck verificationCheck = VerificationCheck.creator(
                            verifyServiceSid
            )
            .setTo(phoneNumber)
            .setCode(code)
            .create();
            return "Approved".equalsIgnoreCase(verificationCheck.getStatus());
        } catch (ApiException e) {
            return false;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }


}
