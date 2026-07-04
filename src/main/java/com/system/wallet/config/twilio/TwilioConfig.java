package com.system.wallet.config.twilio;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "twilio")
public record TwilioConfig(String accountSid , String authToken , String verifyServiceSid) {
}
