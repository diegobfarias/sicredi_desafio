package com.sicredi_desafio.diegobfarias.services.exceptions;

public class TopicAlreadyExistsException extends RuntimeException {

    public TopicAlreadyExistsException(String topicId) {
        super(topicId);
    }
}
