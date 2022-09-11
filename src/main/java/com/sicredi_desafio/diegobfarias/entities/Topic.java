package com.sicredi_desafio.diegobfarias.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Topic {

    @Id
    private Long id;
    private LocalDateTime startTopic;
    private LocalDateTime endTopic;
    private String topicDescription;
    private Map<Long, String> associatesVotes;
}
