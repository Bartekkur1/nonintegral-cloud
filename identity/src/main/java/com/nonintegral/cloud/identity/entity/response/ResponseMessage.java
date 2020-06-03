package com.nonintegral.cloud.identity.entity.response;

import org.springframework.http.HttpStatus;

public enum ResponseMessage {
    ACCOUNT_CREATED(HttpStatus.OK, "Account created successfully"),
    ACCOUNT_ACTIVATED(HttpStatus.OK, "Account activated successfully"),
    ACCOUNT_CREATION_FAILED(HttpStatus.BAD_REQUEST, "Account creation failed"),
    LOGGED_IN(HttpStatus.OK, "Logged in"),
    DATA_VALID(HttpStatus.BAD_REQUEST, "Data validated successfully"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "Given email address is invalid"),
    EMAIL_TAKEN(HttpStatus.BAD_REQUEST, "Given email address is already taken"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "Given password is invalid"),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "Login failed"),
    INVALID_TOKEN(HttpStatus.FORBIDDEN, "Given token is invalid"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    PASSWORD_CHANGED(HttpStatus.OK, "Your password has been successfully changed"),
    RECOVERY_LINK_SENT(HttpStatus.OK, "Recovery token was sent to your email address"),
    USER_REMOVED(HttpStatus.OK, "User removed"),
    USER_BANNED(HttpStatus.OK, "User banned"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found"),
    OK(HttpStatus.OK, "OK");

    private HttpStatus httpStatus;
    private String message;

    ResponseMessage(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return this.httpStatus;
    }
    public String getMessage() { return this.message; }
}
