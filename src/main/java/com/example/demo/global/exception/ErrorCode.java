package com.example.demo.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // OAuth 관련 에러
    OAUTH_PROVIDER_NOT_SUPPORTED(400, "지원하지 않는 OAuth Provider입니다."),
    OAUTH_EMAIL_NOT_FOUND(400, "OAuth Provider로부터 이메일을 가져올 수 없습니다."),
    OAUTH_AUTHENTICATION_FAILED(401, "OAuth 인증에 실패했습니다."),

    // JWT 관련 에러
    JWT_INVALID(401, "유효하지 않은 JWT 토큰입니다."),
    INVALID_REFRESH_TOKEN(401, "만료된 JWT 토큰입니다."),
    JWT_UNSUPPORTED(401, "지원되지 않는 JWT 토큰입니다."),
    JWT_MALFORMED(401, "잘못된 형식의 JWT 토큰입니다."),
    JWT_SIGNATURE_INVALID(401, "JWT 서명이 유효하지 않습니다."),
    UNAUTHORIZED(401, "이메일 또는 비밀번호가 잘못되었습니다."),
    INVALID_TOKEN_TYPE(401, "Access Token으로는 재발급할 수 없습니다."),

    // 첨부파일 관련 에러
    NOT_FOUND_ATTACHMENTS(404, "존재하지 않는 첨부파일입니다."),
    ATTACHMENTS_UPLOAD_FAILED(500, "첨부파일 업로드에 실패했습니다."),
    ATTACHMENTS_DELETE_FAILED(500, "첨부파일 삭제에 실패했습니다."),
    // 토큰 관련 에러
    REFRESH_TOKEN_NOT_FOUND(404, "Refresh Token을 찾을 수 없습니다."),
    REFRESH_TOKEN_EXPIRED(401, "만료된 Refresh Token입니다."),
    REFRESH_TOKEN_INVALID(401, "유효하지 않은 Refresh Token입니다."),

    // 사용자 관련 에러
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(409, "이미 존재하는 사용자입니다."),
    USER_UNAUTHORIZED(403, "권한이 없습니다."),

    //  서버 에러
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다."),
    BAD_REQUEST(400, "잘못된 요청입니다.");


    private final int status;
    private final String message;
}
