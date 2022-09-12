package com.sicredi_desafio.diegobfarias.services.exceptions;

public class SessionNotFoundException extends RuntimeException {

    public SessionNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
