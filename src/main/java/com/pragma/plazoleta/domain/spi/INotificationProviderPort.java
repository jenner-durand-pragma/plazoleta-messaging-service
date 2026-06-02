package com.pragma.plazoleta.domain.spi;

import com.pragma.plazoleta.domain.model.SmsNotification;

public interface INotificationProviderPort {

    void deliver(SmsNotification notification);
}
