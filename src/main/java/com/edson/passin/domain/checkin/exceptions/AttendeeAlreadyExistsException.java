package com.edson.passin.domain.checkin.exceptions;

public class AttendeeAlreadyExistsException extends RuntimeException{
    public AttendeeAlreadyExistsException(String message) {
        super(message);
    }
}
