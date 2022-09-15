package com.sicredi_desafio.diegobfarias.controllers.dtos;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TopicDocumentKafkaDTO implements Serializable {

    private String id;
    private String startTopic;
    private String endTopic;
    private String topicDescription;
    private Map<String, String> associatesVotes;
}