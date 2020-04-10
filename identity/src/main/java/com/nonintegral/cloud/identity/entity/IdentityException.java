package com.nonintegral.cloud.identity.entity;

import com.nonintegral.cloud.identity.entity.response.ResponseMessage;

public class IdentityException extends Exception {

    public IdentityException(ResponseMessage message) {
        super(message.toString());
    }

}
