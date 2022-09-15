package com.sicredi_desafio.diegobfarias.services.exceptions;

public class SessionNotOpenException extends RuntimeException {
    public SessionNotOpenException(String topicId) {
        super(topicId);
    }
}
