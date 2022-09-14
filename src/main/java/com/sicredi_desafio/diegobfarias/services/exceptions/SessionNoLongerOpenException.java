package com.sicredi_desafio.diegobfarias.services.exceptions;

public class SessionNoLongerOpenException extends RuntimeException {
    public SessionNoLongerOpenException(String topicId) {
        super(topicId);
    }
}
