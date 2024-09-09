package com.research.agrivision.api.adapter.api.request;

import com.research.hexa.core.Request;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequest implements Request {
    private String email;
    private String password;
}
