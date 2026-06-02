package com.pragma.plazoleta.domain.api;

import com.pragma.plazoleta.domain.model.SmsNotification;

public interface INotificationServicePort {

    void sendSms(SmsNotification notification);
}
