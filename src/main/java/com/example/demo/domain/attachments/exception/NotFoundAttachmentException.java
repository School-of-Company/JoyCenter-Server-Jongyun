package com.example.demo.domain.attachments.exception;

import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.GlobalException;

public class NotFoundAttachmentException extends GlobalException {
    public NotFoundAttachmentException() {
        super(ErrorCode.NOT_FOUND_ATTACHMENTS);
    }
}
