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
public class TopicDocumentDTO implements Serializable {

    private LocalDateTime startTopic;
    private LocalDateTime endTopic;
    private String topicDescription;
    private Map<Long, String> associatesVotes;
}
