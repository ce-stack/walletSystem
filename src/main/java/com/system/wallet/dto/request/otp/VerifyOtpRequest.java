package com.system.wallet.dto.request.otp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class VerifyOtpRequest {

    @NotBlank(message = "phone number is required")
    @Pattern(regexp = "^\\+[1-9]\\d{7,14}$",message = "Phone number must be in E.164 format")
    public String phoneNumber;


    @NotBlank(message = "Otp code is required")
    @Pattern(regexp = "^\\d{4,8}$",message = "OTP code must contain digits only")
    public String code;
}
