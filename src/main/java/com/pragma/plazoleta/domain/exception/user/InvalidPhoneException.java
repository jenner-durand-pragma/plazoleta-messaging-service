package com.pragma.plazoleta.domain.exception.user;

import com.pragma.plazoleta.domain.exception.BusinessRuleException;

public class InvalidPhoneException extends BusinessRuleException {

    private static final String ERROR_MESSAGE = "Phone number must be valid.";

    public InvalidPhoneException() {
        super(ERROR_MESSAGE);
    }
}
