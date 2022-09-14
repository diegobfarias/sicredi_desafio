package com.sicredi_desafio.diegobfarias.controllers.exceptions;

import com.sicredi_desafio.diegobfarias.controllers.dtos.StandardErrorDTO;
import com.sicredi_desafio.diegobfarias.services.exceptions.TopicAlreadyExistsException;
import com.sicredi_desafio.diegobfarias.services.exceptions.TopicNotFoundException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @ExceptionHandler(TopicNotFoundException.class)
    @ApiResponse(responseCode = "404 NOT FOUND", description = "Pauta não encontrada")
    public ResponseEntity<StandardErrorDTO> handleTopicNotFoundException(TopicNotFoundException e, HttpServletRequest request) {
        log.error("Não foi encontrada a pauta: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StandardErrorDTO.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Não foi encontrada a pauta: " + e.getMessage())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build());
    }

    @ExceptionHandler(TopicAlreadyExistsException.class)
    @ApiResponse(responseCode = "409 CONFLICT", description = "Pauta já existente")
    public ResponseEntity<StandardErrorDTO> handleTopicAlreadyExistsException(TopicAlreadyExistsException e, HttpServletRequest request) {
        log.error("Já existe a pauta com a descrição: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(StandardErrorDTO.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Já existe a pauta com a descrição: " + e.getMessage())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build());
    }
}
