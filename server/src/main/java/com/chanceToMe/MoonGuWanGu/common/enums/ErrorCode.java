package com.chanceToMe.MoonGuWanGu.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //Database
    DUPLICATED_KEY(HttpStatus.BAD_REQUEST, "DB-001", "a key is duplicated"),

    //common
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "C-001", "unknown error"),
    INVALID(HttpStatus.BAD_REQUEST, "C-002", "validation error");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
