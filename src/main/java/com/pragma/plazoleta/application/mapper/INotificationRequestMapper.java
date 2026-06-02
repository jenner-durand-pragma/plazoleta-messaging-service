package com.pragma.plazoleta.application.mapper;

import com.pragma.plazoleta.application.dto.request.notification.SendSmsRequestDto;
import com.pragma.plazoleta.domain.model.SmsNotification;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface INotificationRequestMapper {

    SmsNotification toDomain(SendSmsRequestDto dto);

}
