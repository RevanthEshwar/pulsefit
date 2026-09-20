package com.pulsefit.attendance.exception;

public class FacilityNotFoundException extends RuntimeException {

    public FacilityNotFoundException(String message) {
        super(message);
    }
}