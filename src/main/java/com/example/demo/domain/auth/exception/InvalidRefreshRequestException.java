package com.example.demo.domain.auth.exception;

import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.GlobalException;

public class InvalidRefreshRequestException extends GlobalException {
    public InvalidRefreshRequestException() {
        super(ErrorCode.INVALID_REFRESH_TOKEN);
    }
}
