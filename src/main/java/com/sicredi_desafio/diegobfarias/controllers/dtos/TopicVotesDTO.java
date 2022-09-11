package com.sicredi_desafio.diegobfarias.controllers.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicVotesDTO {

    private String topicDescription;
    private Long positiveVotes;
    private Long negativeVotes;
}
