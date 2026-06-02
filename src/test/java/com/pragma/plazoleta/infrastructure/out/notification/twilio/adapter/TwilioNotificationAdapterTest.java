package com.pragma.plazoleta.infrastructure.out.notification.twilio.adapter;

import com.pragma.plazoleta.domain.model.SmsNotification;
import com.pragma.plazoleta.infrastructure.out.notification.twilio.configuration.TwilioProperties;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TwilioNotificationAdapterTest {

    @Mock
    private TwilioProperties twilioProperties;

    @InjectMocks
    private TwilioNotificationAdapter adapter;

    private SmsNotification notification;

    @BeforeEach
    void setUp() {
        notification = new SmsNotification("+1234567890", "Test message");
    }

    @Test
    void initTwilioShouldInitializeTwilioSdk() {
        when(twilioProperties.getAccountSid()).thenReturn("fake-sid");
        when(twilioProperties.getAuthToken()).thenReturn("fake-token");

        try (MockedStatic<Twilio> mockedTwilio = mockStatic(Twilio.class)) {
            adapter.initTwilio();

            mockedTwilio.verify(() -> Twilio.init(
                    "fake-sid",
                    "fake-token"
            ), times(1));
        }
    }

    @Test
    void deliverShouldSendMessageSuccessfully() {
        when(twilioProperties.getFromNumber()).thenReturn("+0987654321");

        Message mockedMessage = mock(Message.class);
        when(mockedMessage.getSid()).thenReturn("SM123456789");

        MessageCreator mockedCreator = mock(MessageCreator.class);
        when(mockedCreator.create()).thenReturn(mockedMessage);

        try (MockedStatic<Message> mockedStaticMessage = mockStatic(Message.class)) {
            mockedStaticMessage.when(() -> Message.creator(
                    any(PhoneNumber.class),
                    any(PhoneNumber.class),
                    eq(notification.getMessage())
            )).thenReturn(mockedCreator);

            assertDoesNotThrow(() -> adapter.deliver(notification));

            verify(mockedCreator, times(1)).create();
        }
    }

    @Test
    void deliverShouldThrowApiExceptionWhenTwilioApiRejects() {
        when(twilioProperties.getFromNumber()).thenReturn("+0987654321");

        var mockedCreator = mock(MessageCreator.class);
        var apiException = new ApiException(
                "Mocked Twilio API Error",
                400,
                "Some info",
                400,
                null
        );

        when(mockedCreator.create()).thenThrow(apiException);

        try (MockedStatic<Message> mockedStaticMessage = mockStatic(Message.class)) {
            mockedStaticMessage.when(() -> Message.creator(
                    any(PhoneNumber.class),
                    any(PhoneNumber.class),
                    anyString()
            )).thenReturn(mockedCreator);

            assertThrows(ApiException.class, () -> adapter.deliver(notification));
        }
    }
}
