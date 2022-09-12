package com.sicredi_desafio.diegobfarias.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "Topic")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TopicDocument implements Serializable {

    @Id
    private Long id;
    private LocalDateTime startTopic;
    private LocalDateTime endTopic;
    private String topicDescription;
    private Map<Long, String> associatesVotes;
}
