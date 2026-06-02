package com.pragma.plazoleta.infrastructure.input.rest;

import com.pragma.plazoleta.application.dto.request.notification.SendSmsRequestDto;
import com.pragma.plazoleta.application.handler.INotificationHandler;
import com.pragma.plazoleta.infrastructure.configuration.security.annotation.IsEmployee;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "SMS notifications")
public class NotificationRestController {

    private final INotificationHandler notificationHandler;

    @IsEmployee
    @PostMapping("/sms")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendSms(@Valid @RequestBody SendSmsRequestDto request) {

    }
}
