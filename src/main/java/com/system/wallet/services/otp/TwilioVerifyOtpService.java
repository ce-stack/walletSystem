package com.system.wallet.services.otp;


import com.system.wallet.config.enums.TransactionStatus;
import com.system.wallet.dto.request.otp.VerifyOtpRequest;
import com.system.wallet.models.Transaction;
import com.system.wallet.models.Wallet;
import com.system.wallet.repositories.TransactionRepository;
import com.system.wallet.repositories.WalletRepository;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@ConditionalOnProperty(name = "otp.provider" , havingValue = "twilio")
public class TwilioVerifyOtpService implements OtpService{

    private final String accountSid;
    private final String authToken;
    private final String verifyServiceSid;
    private TransactionRepository transactionRepository;
    private WalletRepository walletRepository;

    public TwilioVerifyOtpService(@Value("${TWILIO_ACCOUNT_SID}") String accountSid,@Value("${TWILIO_AUTH_TOKEN}") String authToken,@Value("${TWILIO_VERIFY_SERVICE_SID}") String verifyServiceSid , TransactionRepository transactionRepository , WalletRepository walletRepository) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.verifyServiceSid = verifyServiceSid;
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    @PostConstruct
    void initTwilio() {
        if(isBlank(accountSid) || isBlank(authToken) || isBlank(verifyServiceSid)) {
            throw new IllegalArgumentException("Twilio configuration parameters are not valid");
        }
        Twilio.init(accountSid, authToken);
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
    @Transactional
    public boolean verifyOtp(VerifyOtpRequest verifyOtpRequest) {
        try {
            VerificationCheck verificationCheck = VerificationCheck.creator(
                            verifyServiceSid
            )
            .setTo(verifyOtpRequest.getFromPhoneNumber())
            .setCode(verifyOtpRequest.getCode())
            .create();

            getWalletsTransaction(verifyOtpRequest);

            Integer fromWalletId =  verifyOtpRequest.getFrom_wallet_id();
            Integer toWalletId = verifyOtpRequest.getTo_wallet_id();

            Wallet fromWallet;
            Wallet toWallet;
            fromWallet = getWalletForUpdateOrThrow(fromWalletId);
            toWallet = getWalletForUpdateOrThrow(toWalletId);
            afterPayment(fromWallet, toWallet, verifyOtpRequest.getAmount());
            return "Approved".equalsIgnoreCase(verificationCheck.getStatus());
        } catch (ApiException e) {
            return e.getCode() == 400;
        }
    }

    private Transaction getWalletsTransaction(VerifyOtpRequest verifyOtpRequest) {
        Transaction transaction = transactionRepository
                .findLatestTransactions(
                        verifyOtpRequest.getFrom_wallet_id(),
                        verifyOtpRequest.getTo_wallet_id(),
                        TransactionStatus.PENDING,
                        PageRequest.of(0, 1)
                )
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid wallet transaction"));

        transaction.setStatus(TransactionStatus.DONE);

        return transactionRepository.save(transaction);
    }


    private void afterPayment(Wallet fromWallet, Wallet toWallet, Double amount) {
        Double newBalanceFrom = fromWallet.getBalance() - amount;
        fromWallet.setBalance(newBalanceFrom);
        Double newBalanceTo = toWallet.getBalance() + amount;
        toWallet.setBalance(newBalanceTo);
        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);
    }


    private Wallet getWalletForUpdateOrThrow(Integer walletId) {
        return walletRepository.findByIdForUpdate(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet with id " + walletId + " not found"));
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

}
