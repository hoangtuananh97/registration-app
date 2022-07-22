package com.example.registrationapp.web.exception;

public class SalaryInvalidateException extends RuntimeException{

    public SalaryInvalidateException() {
        super();
    }

    public SalaryInvalidateException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SalaryInvalidateException(final String message) {
        super(message);
    }

    public SalaryInvalidateException(final Throwable cause) {
        super(cause);
    }

}
