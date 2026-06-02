package com.pragma.plazoleta.application.handler;

import com.pragma.plazoleta.application.dto.request.notification.SendSmsRequestDto;

public interface INotificationHandler {

    void sendSms(SendSmsRequestDto request);

}
