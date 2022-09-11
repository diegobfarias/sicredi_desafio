package com.sicredi_desafio.diegobfarias.converter;

import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicDTO;
import com.sicredi_desafio.diegobfarias.entities.Topic;
import org.springframework.stereotype.Component;

@Component
public class TopicConverter {

    public static TopicDTO toDTO(Topic topic) {
        return TopicDTO.builder()
                .endTopic(topic.getEndTopic())
                .startTopic(topic.getStartTopic())
                .topicDescription(topic.getTopicDescription())
                .associatesVotes(topic.getAssociatesVotes())
                .build();
    }

    public static Topic toEntity(TopicDTO topicDTO) {
        return Topic.builder()
                .endTopic(topicDTO.getEndTopic())
                .startTopic(topicDTO.getStartTopic())
                .topicDescription(topicDTO.getTopicDescription())
                .associatesVotes(topicDTO.getAssociatesVotes())
                .build();
    }
}
