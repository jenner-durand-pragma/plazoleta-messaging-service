package com.pragma.plazoleta.domain.model;

import com.pragma.plazoleta.domain.exception.user.InvalidPhoneException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsNotification {

    private String to;
    private String message;
}
