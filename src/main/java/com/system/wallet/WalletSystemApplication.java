package com.system.wallet;

import com.system.wallet.config.twilio.TwilioConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TwilioConfig.class)
public class WalletSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletSystemApplication.class, args);
	}

}
