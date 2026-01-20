package com.example.demo.domain.post.exception;

import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.GlobalException;

public class NotFoundPostException extends GlobalException {
    public NotFoundPostException() {
        super(ErrorCode.NOT_FOUND_POST);
    }
}
