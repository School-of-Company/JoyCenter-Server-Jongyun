package com.example.demo.domain.attachments.entity.constant;

public enum AttachmentsType {

    IMAGE("images"),
    VIDEO("videos"),
    FILE("files");

    private final String path;

    AttachmentsType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
