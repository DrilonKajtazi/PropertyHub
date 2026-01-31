package com.newcompany.exception.model;

import org.springframework.http.HttpStatus;

public class UserNotLoggedInException extends RuntimeException {

    public UserNotLoggedInException() {
        super("User is not logged in!");
    }

    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
