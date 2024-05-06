package com.chanceToMe.MoonGuWanGu.common.exception;

import com.chanceToMe.MoonGuWanGu.common.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

  ErrorCode errorCode;
  StackTraceElement[] stackTraceElement;
}