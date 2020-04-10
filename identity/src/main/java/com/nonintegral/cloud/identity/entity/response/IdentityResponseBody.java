package com.nonintegral.cloud.identity.entity.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
public class IdentityResponseBody {

    private HttpStatus httpStatus;
    private ResponseMessage code;
    private Object body;
    private Date time;

    public IdentityResponseBody(ResponseMessage code, Object body) {
        this.httpStatus = code.getStatus();
        this.code = code;
        this.body = body;
        this.time = new Date();
    }

    @Override
    public String toString() {
        return "IdentityResponseBody{" +
                "statusCode=" + httpStatus.toString() +
                ", code=" + code +
                ", body='" + body + '\'' +
                '}';
    }
}
