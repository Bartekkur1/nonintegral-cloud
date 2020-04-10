package com.nonintegral.cloud.identity.entity.request;

import lombok.Data;

@Data
public class AccountRecoverRequest extends TokenRequest {

    private String newPassword;

}
