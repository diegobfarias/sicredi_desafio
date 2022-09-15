package com.sicredi_desafio.diegobfarias.controllers.dtos;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TopicDocumentDTO implements Serializable {

    private String id;
    private LocalDateTime startTopic;
    private LocalDateTime endTopic;
    private String topicDescription;
    private Map<String, String> associatesVotes;
}
