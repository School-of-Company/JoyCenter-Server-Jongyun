package com.example.demo.domain.attachments.exception;

import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.GlobalException;

public class AttachmentsDeleteFailedException extends GlobalException{
    public AttachmentsDeleteFailedException() {
        super(ErrorCode.ATTACHMENTS_DELETE_FAILED);
    }
}
