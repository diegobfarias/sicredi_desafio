package com.sicredi_desafio.diegobfarias.controllers.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicDTO {

    private LocalDateTime startTopic;
    private LocalDateTime endTopic;
    private String topicDescription;
    private Map<Long, String> associatesVotes;
}
