package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.exception.user.InvalidPhoneException;
import com.pragma.plazoleta.domain.model.SmsNotification;
import com.pragma.plazoleta.domain.spi.INotificationProviderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationUseCaseTest {

    @Mock
    private INotificationProviderPort notificationProviderPort;

    @InjectMocks
    private NotificationUseCase notificationUseCase;

    private SmsNotification validNotification;

    @BeforeEach
    void setUp() {
        validNotification = SmsNotification.builder()
                .to("+51938475869")
                .message("Your order #12 is ready. PIN: 482910")
                .build();
    }

    @Test
    @DisplayName("Should deliver SMS when phone number is valid in send sms")
    void shouldDeliverSmsWhenPhoneNumberIsValidInSendSms() {
        notificationUseCase.sendSms(validNotification);

        verify(notificationProviderPort).deliver(validNotification);
    }

    @Test
    @DisplayName("Should accept phone without leading plus sign in send sms")
    void shouldAcceptPhoneWithoutLeadingPlusSignInSendSms() {
        var notification = SmsNotification.builder()
                .to("51938475869")
                .message("Ready")
                .build();

        notificationUseCase.sendSms(notification);

        verify(notificationProviderPort).deliver(notification);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"+51ABC666666", "+1234567890123456"})
    @DisplayName("Should throw InvalidPhoneException when phone is invalid in sendSms")
    void shouldThrowInvalidPhoneExceptionWhenPhoneIsInvalidInSendSms(String invalidPhone) {
        var notification = SmsNotification.builder()
                .to(invalidPhone)
                .message("x")
                .build();

        assertThatThrownBy(() -> notificationUseCase.sendSms(notification))
                .isInstanceOf(InvalidPhoneException.class);

        verify(notificationProviderPort, never()).deliver(any());
    }
}
