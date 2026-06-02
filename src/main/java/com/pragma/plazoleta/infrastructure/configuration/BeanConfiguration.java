package com.pragma.plazoleta.infrastructure.configuration;

import com.pragma.plazoleta.domain.api.INotificationServicePort;
import com.pragma.plazoleta.domain.spi.INotificationProviderPort;
import com.pragma.plazoleta.domain.usecase.NotificationUseCase;
import com.pragma.plazoleta.infrastructure.configuration.security.token.ITokenValidationPort;
import com.pragma.plazoleta.infrastructure.out.notification.twilio.adapter.TwilioNotificationAdapter;
import com.pragma.plazoleta.infrastructure.out.notification.twilio.configuration.TwilioProperties;
import com.pragma.plazoleta.infrastructure.out.security.jwt.JwtAdapter;
import com.pragma.plazoleta.infrastructure.out.security.jwt.configuration.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final JwtProperties jwtProperties;
    private final TwilioProperties twilioProperties;

    @Bean
    public ITokenValidationPort tokenValidationPort() {
        return new JwtAdapter(jwtProperties);
    }

    @Bean
    public INotificationProviderPort twilioNotificationProvider() {
        return new TwilioNotificationAdapter(twilioProperties);
    }

    @Bean
    public INotificationServicePort notificationServicePort(
            INotificationProviderPort notificationProviderPort
    ) {
        return new NotificationUseCase(notificationProviderPort);
    }
}
