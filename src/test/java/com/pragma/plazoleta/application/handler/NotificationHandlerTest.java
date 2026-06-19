package com.pragma.plazoleta.application.handler;

import com.pragma.plazoleta.application.dto.request.notification.SendSmsRequestDto;
import com.pragma.plazoleta.application.handler.impl.NotificationHandler;
import com.pragma.plazoleta.application.mapper.INotificationRequestMapper;
import com.pragma.plazoleta.domain.api.INotificationServicePort;
import com.pragma.plazoleta.domain.model.SmsNotification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationHandlerTest {

    @Mock
    private INotificationServicePort notificationServicePort;

    @Spy
    private INotificationRequestMapper notificationRequestMapper = Mappers.getMapper(INotificationRequestMapper.class);

    @InjectMocks
    private NotificationHandler handler;

    @Captor
    private ArgumentCaptor<SmsNotification> notificationCaptor;

    @Test
    void sendSmsShouldMapRequestAndCallService() {
        var requestDto = new SendSmsRequestDto();
        requestDto.setTo("+51938475869");
        requestDto.setMessage("Test message");

        handler.sendSms(requestDto);

        verify(notificationRequestMapper, times(1))
                .toDomain(requestDto);

        verify(notificationServicePort, times(1))
                .sendSms(notificationCaptor.capture());

        var capturedNotification = notificationCaptor.getValue();

        assertEquals(requestDto.getTo(), capturedNotification.getTo());
        assertEquals(requestDto.getMessage(), capturedNotification.getMessage());
    }
}
