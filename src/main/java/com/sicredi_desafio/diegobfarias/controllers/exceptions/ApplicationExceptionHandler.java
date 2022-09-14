package com.sicredi_desafio.diegobfarias.controllers.exceptions;

import com.sicredi_desafio.diegobfarias.controllers.dtos.StandardErrorDTO;
import com.sicredi_desafio.diegobfarias.services.exceptions.*;
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
        log.error("Já existe a pauta com o ID: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(StandardErrorDTO.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Já existe a pauta com o ID: " + e.getMessage())
                .message("Pauta já existente")
                .path(request.getRequestURI())
                .build());
    }

    @ExceptionHandler(CpfNotFoundException.class)
    @ApiResponse(responseCode = "404 NOT FOUND", description = "Acionada quando não encontra o CPF.")
    public ResponseEntity<StandardErrorDTO> handleCpfNotFoundException(CpfNotFoundException e, HttpServletRequest request) {
        log.error("Não foi encontrado o CPF: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StandardErrorDTO.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Não foi encontrado o CPF: " + e.getMessage())
                .message("Erro ao buscar CPF")
                .path(request.getRequestURI())
                .build());
    }

    @ExceptionHandler(AssociateAlreadyVotedException.class)
    @ApiResponse(responseCode = "400 BAD REQUEST", description = "Chamada quando o associado tenta votar em uma pauta na qual já votou")
    public ResponseEntity<StandardErrorDTO> handleAssociateAlreadyVotedException(AssociateAlreadyVotedException e, HttpServletRequest request) {
        log.error("O associado {} já votou nesta pauta", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StandardErrorDTO.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("O associado " + e.getMessage() + " já votou nesta pauta")
                .message("Pauta já votada")
                .path(request.getRequestURI())
                .build());
    }

    @ExceptionHandler(SessionNoLongerOpenException.class)
    @ApiResponse(responseCode = "400 BAD REQUEST", description = "Chamada quando o associado tenta votar e a sessão da pauta já fechou.")
    public ResponseEntity<StandardErrorDTO> handleSessionNoLongerOpenException(SessionNoLongerOpenException e, HttpServletRequest request) {
        log.error("A pauta {} não está mais aberta", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StandardErrorDTO.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("A pauta " + e.getMessage() + " não está mais aberta para votação.")
                .message("Sessão encerrada")
                .path(request.getRequestURI())
                .build());
    }
}
