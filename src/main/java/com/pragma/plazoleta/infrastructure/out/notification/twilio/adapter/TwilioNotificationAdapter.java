package com.pragma.plazoleta.infrastructure.out.notification.twilio.adapter;

import com.pragma.plazoleta.domain.model.SmsNotification;
import com.pragma.plazoleta.domain.spi.INotificationProviderPort;
import com.pragma.plazoleta.infrastructure.out.notification.twilio.configuration.TwilioProperties;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

@Slf4j
public class TwilioNotificationAdapter implements INotificationProviderPort {

    private final TwilioProperties twilioProperties;

    public TwilioNotificationAdapter(TwilioProperties twilioProperties) {
        this.twilioProperties = twilioProperties;
    }

    @PostConstruct
    void initTwilio() {
        Twilio.init(twilioProperties.getAccountSid(), twilioProperties.getAuthToken());

        log.info("Twilio SDK initialized");
    }

    @Override
    public void deliver(SmsNotification notification) {
        try {
            var sent = Message.creator(
                    new PhoneNumber(notification.getTo()),
                    new PhoneNumber(twilioProperties.getFromNumber()),
                    notification.getMessage()
            ).create();

            log.info("Twilio SMS delivered, sid=[{}] to=[{}]",
                    sent.getSid(), notification.getTo());
        } catch (ApiException ex) {
            log.error(
                    "Twilio API rejected the message to=[{}] code=[{}]",
                    notification.getTo(),
                    ex.getCode(),
                    ex
            );

            throw ex;
        }
    }
}
