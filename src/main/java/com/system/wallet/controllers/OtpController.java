package com.system.wallet.controllers;

import com.system.wallet.dto.request.TransferWalletRequest;
import com.system.wallet.dto.request.otp.SendOtpRequest;
import com.system.wallet.dto.request.otp.VerifyOtpRequest;
import com.system.wallet.dto.response.dto.VerifyOtpResponse;
import com.system.wallet.payload.ApiResponse;
import com.system.wallet.services.otp.OtpService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final OtpService otpService;
    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        otpService.sendOtp(request.phoneNumber);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/verify")
    public ApiResponse verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        boolean verified = otpService.verifyOtp(request);
        if(verified == true) {
            return new ApiResponse("Transfer is done" , true , 200);
        } else {
            return new ApiResponse("Error tyr later !" , false , 400);
        }

    }


}
