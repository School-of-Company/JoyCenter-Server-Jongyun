package com.example.demo.domain.member.exception;

import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.GlobalException;

public class NotFoundMemberException extends GlobalException{
    public NotFoundMemberException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
