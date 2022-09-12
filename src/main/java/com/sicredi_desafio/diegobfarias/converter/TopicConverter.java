package com.sicredi_desafio.diegobfarias.converter;

import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicDocumentDTO;
import com.sicredi_desafio.diegobfarias.documents.TopicDocument;
import org.springframework.stereotype.Component;

@Component
public class TopicConverter {

    public static TopicDocumentDTO toDTO(TopicDocument topicDocument) {
        return TopicDocumentDTO.builder()
                .endTopic(topicDocument.getEndTopic())
                .startTopic(topicDocument.getStartTopic())
                .topicDescription(topicDocument.getTopicDescription())
                .associatesVotes(topicDocument.getAssociatesVotes())
                .build();
    }

    public static TopicDocument toEntity(TopicDocumentDTO topicDocumentDTO) {
        return TopicDocument.builder()
                .endTopic(topicDocumentDTO.getEndTopic())
                .startTopic(topicDocumentDTO.getStartTopic())
                .topicDescription(topicDocumentDTO.getTopicDescription())
                .associatesVotes(topicDocumentDTO.getAssociatesVotes())
                .build();
    }
}
