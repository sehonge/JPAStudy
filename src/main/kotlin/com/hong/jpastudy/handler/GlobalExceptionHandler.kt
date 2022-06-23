package com.hong.jpastudy.handler

import com.hong.jpastudy.dto.ErrorResponse
import com.hong.jpastudy.exception.NoneNoticeException
import com.hong.jpastudy.exception.NotValidUserForm
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@Slf4j
@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NoneNoticeException::class)
    fun handleNoneNoticeException(e: NoneNoticeException): ResponseEntity<ErrorResponse> {
        //TODO("add log")
        return ResponseEntity(ErrorResponse(msg = e.toString()), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(e.toString()), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NotValidUserForm::class)
    fun handleNotValidUserForm(e: NotValidUserForm): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(e.toString()), HttpStatus.BAD_REQUEST)
    }
}
