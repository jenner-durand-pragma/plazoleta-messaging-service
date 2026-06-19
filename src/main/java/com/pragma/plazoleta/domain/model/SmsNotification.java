package com.pragma.plazoleta.domain.model;

import com.pragma.plazoleta.domain.exception.user.InvalidPhoneException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsNotification {

    private String to;
    private String message;

    private static final Pattern PHONE_REGEX = Pattern.compile("^\\+?\\d{1,13}$");

    public void checkToAsPhone() {
        if (to == null || !PHONE_REGEX.matcher(to).matches()) {
            throw new InvalidPhoneException();
        }
    }
}
