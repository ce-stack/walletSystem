package com.system.wallet.dto.request.otp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class SendOtpRequest {

    @NotBlank(message = "phone number is required")
    @Pattern(regexp = "^\\+[1-9]\\d{7,14}$",message = "Phone number must be in E.164 format")
    public String phoneNumber;
}
