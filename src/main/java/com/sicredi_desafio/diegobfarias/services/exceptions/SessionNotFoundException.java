package com.sicredi_desafio.diegobfarias.services.exceptions;

public class SessionNotFoundException extends RuntimeException {

    public SessionNotFoundException(Long topicId) {
        super(String.valueOf(topicId));
    }
}
