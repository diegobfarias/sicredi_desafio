package com.sicredi_desafio.diegobfarias.services;

import com.sicredi_desafio.diegobfarias.client.CpfClient;
import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicDTO;
import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicVotesDTO;
import com.sicredi_desafio.diegobfarias.entities.Topic;
import com.sicredi_desafio.diegobfarias.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

import static com.sicredi_desafio.diegobfarias.converter.TopicConverter.toDTO;
import static com.sicredi_desafio.diegobfarias.converter.TopicConverter.toEntity;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final SessionRepository sessionRepository;
    private final CpfClient cpfClient;

    public TopicDTO createNewTopic(TopicDTO topicDto) {
        return toDTO(sessionRepository.save(toEntity(topicDto)));
    }

    public Optional<TopicDTO> openNewVotingTopicSession(Long sessionId, LocalDateTime startTopic, LocalDateTime endTopic) throws FileNotFoundException {
        Optional<Topic> currentVotingTopicSession = sessionRepository.findById(sessionId);

        if (currentVotingTopicSession.isPresent()) {
            currentVotingTopicSession.get().setStartTopic(isNull(startTopic) ? LocalDateTime.now() : startTopic);
            currentVotingTopicSession.get().setEndTopic(isNull(endTopic) ? LocalDateTime.now().plusMinutes(1) : endTopic);
            return Optional.of(toDTO(currentVotingTopicSession.get()));
        } else {
            throw new FileNotFoundException();
        }
    }

    public void computeVotes(Long sessionId, Long associateId, String associateVote) throws Exception {
        Optional<Topic> currentVotingTopicSession = sessionRepository.findById(sessionId);
        String cpf = cpfClient.verifyCpf(String.valueOf(associateId));

        if (currentVotingTopicSession.isPresent() && cpf.equalsIgnoreCase("ABLE_TO_VOTE")) {
            if (!currentVotingTopicSession.get().getAssociatesVotes().containsKey(associateId)) {
                currentVotingTopicSession.get().getAssociatesVotes().put(associateId, associateVote);
            } else {
                // TODO criar exceção personalizada
                throw new RuntimeException();
            }
        } else {
            // TODO criar exceção personalizada
            throw new RuntimeException();
        }
    }

    public TopicVotesDTO countVotes(Long sessionId) throws Exception {
        Optional<Topic> currentVotingTopicSession = sessionRepository.findById(sessionId);

        if (currentVotingTopicSession.isPresent()) {
            return TopicVotesDTO.builder()
                    .topicDescription(currentVotingTopicSession.get().getTopicDescription())
                    .positiveVotes(currentVotingTopicSession.get().getAssociatesVotes().values()
                            .stream().filter(vote -> vote.equalsIgnoreCase("Sim")).count())
                    .negativeVotes(currentVotingTopicSession.get().getAssociatesVotes().values()
                            .stream().filter(vote -> vote.equalsIgnoreCase("Não")).count())
                    .build();
        } else {
            throw new RuntimeException();
        }
    }
}
