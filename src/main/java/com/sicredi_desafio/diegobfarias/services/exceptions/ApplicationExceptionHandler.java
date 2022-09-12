package com.sicredi_desafio.diegobfarias.services.exceptions;

import com.sicredi_desafio.diegobfarias.controllers.exceptions.StandardError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<StandardError> handleSessionNotFoundException(SessionNotFoundException e, HttpServletRequest request) {
        log.error("Erro ao buscar a sessão");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StandardError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Sessão não encontrada")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build());
    }
}
