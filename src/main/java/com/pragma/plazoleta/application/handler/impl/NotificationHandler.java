package com.pragma.plazoleta.application.handler.impl;

import com.pragma.plazoleta.application.dto.request.notification.SendSmsRequestDto;
import com.pragma.plazoleta.application.handler.INotificationHandler;
import com.pragma.plazoleta.application.mapper.INotificationRequestMapper;
import com.pragma.plazoleta.domain.api.INotificationServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationHandler implements INotificationHandler {

    private final INotificationServicePort notificationServicePort;
    private final INotificationRequestMapper notificationRequestMapper;

    @Override
    public void sendSms(SendSmsRequestDto request) {
    }
}
