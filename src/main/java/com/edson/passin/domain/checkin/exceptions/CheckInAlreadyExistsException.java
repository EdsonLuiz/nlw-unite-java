package com.edson.passin.domain.checkin.exceptions;

import com.edson.passin.services.CheckInService;

public class CheckInAlreadyExistsException extends RuntimeException{
    public CheckInAlreadyExistsException(String message) {
        super(message);
    }
}
