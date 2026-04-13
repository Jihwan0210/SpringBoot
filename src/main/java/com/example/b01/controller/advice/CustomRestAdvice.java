package com.example.b01.controller.advice;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class CustomRestAdvice {
    @ExceptionHandler(BindException.class) //폼 데이터 검증(@Vaild) 실패로 인해 발생하는 BindException을 처리
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED) //HTTP 응답 상태를 417 EXPECTATION_FALILED로 설정
    public ResponseEntity<Map<String, String>> handleBindException(BindException e) {
        log.error(e);
        Map<String, String> errorMap = new HashMap<>();
        if(e.hasErrors()){ //에러 발생시 ㅔㅇ러가 발생한 필드명과 에러 코드가 errroMap에 저장
            BindingResult bindingResult = e.getBindingResult();
            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put(fieldError.getField(), fieldError.getCode());
            });
        }
        return ResponseEntity.badRequest().body(errorMap);
        //필드 오류 정보를 JSON 형태로 변환하고 HTTP 400 Bad Request 응답 생성
    }
}