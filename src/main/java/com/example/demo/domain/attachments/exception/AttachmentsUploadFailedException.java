package com.example.demo.domain.attachments.exception;

import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.exception.GlobalException;

public class AttachmentsUploadFailedException extends GlobalException {
    public AttachmentsUploadFailedException() {
        super(ErrorCode.ATTACHMENTS_UPLOAD_FAILED);
    }
}
