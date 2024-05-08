package com.chanceToMe.MoonGuWanGu.common.exception;

import com.chanceToMe.MoonGuWanGu.common.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

  @ExceptionHandler({CustomException.class, MethodArgumentNotValidException.class})
  protected ResponseEntity<ErrorResponseEntity> handleCustomException(Exception e) {
    log.error("controller error", e);
    if (e instanceof CustomException) {
      return ErrorResponseEntity.toResponseEntity(((CustomException) e).getErrorCode());
    } else {
      return ErrorResponseEntity.toResponseEntity(
          (new CustomException(ErrorCode.INVALID, e.getStackTrace()).getErrorCode()));
    }
  }
}