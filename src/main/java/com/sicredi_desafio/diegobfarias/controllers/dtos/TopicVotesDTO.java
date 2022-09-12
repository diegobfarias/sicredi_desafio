package com.sicredi_desafio.diegobfarias.controllers.dtos;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicVotesDTO implements Serializable {

    private String topicDescription;
    private Long positiveVotes;
    private Long negativeVotes;
}
