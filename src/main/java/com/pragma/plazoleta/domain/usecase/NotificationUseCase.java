package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.api.INotificationServicePort;
import com.pragma.plazoleta.domain.model.SmsNotification;
import com.pragma.plazoleta.domain.spi.INotificationProviderPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationUseCase implements INotificationServicePort {

    private final INotificationProviderPort notificationProviderPort;

    @Override
    public void sendSms(SmsNotification notification) {
        notification.checkToAsPhone();

        notificationProviderPort.deliver(notification);
    }
}
