package com.system.wallet.dto.request.otp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SendOtpRequest {

    @NotBlank(message = "phone number is required")
    @Size(min = 4 , max = 14)
    @Pattern(regexp = "^\\+[1-9]\\d{7,14}$",message = "Phone number must be in E.164 format")
    public String phoneNumber;
}
