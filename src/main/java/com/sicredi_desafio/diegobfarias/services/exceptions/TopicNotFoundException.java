package com.sicredi_desafio.diegobfarias.services.exceptions;

public class TopicNotFoundException extends RuntimeException {

    public TopicNotFoundException(Long topicId) {
        super(String.valueOf(topicId));
    }
}
