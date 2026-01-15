package com.example.demo.domain.auth.exception;

import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.GlobalException;

public class UnauthorizedException extends GlobalException {
    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}
