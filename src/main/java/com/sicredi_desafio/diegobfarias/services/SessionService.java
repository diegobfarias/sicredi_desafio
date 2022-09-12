package com.sicredi_desafio.diegobfarias.services;

import com.sicredi_desafio.diegobfarias.client.CpfClient;
import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicDTO;
import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicVotesDTO;
import com.sicredi_desafio.diegobfarias.entities.Topic;
import com.sicredi_desafio.diegobfarias.services.exceptions.ApplicationExceptionHandler;
import com.sicredi_desafio.diegobfarias.services.exceptions.SessionNotFoundException;
import com.sicredi_desafio.diegobfarias.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public TopicDTO findTopicById(Long topicId) {
        return toDTO(sessionRepository.findById(topicId).orElseThrow(
                () -> new SessionNotFoundException("Sessão não encontrada " + topicId)));
    }

    public Optional<TopicDTO> openNewVotingTopicSession(Long topicId, LocalDateTime startTopic, LocalDateTime endTopic) {
        TopicDTO currentVotingTopicSession = findTopicById(topicId);

        currentVotingTopicSession.setStartTopic(isNull(startTopic) ? LocalDateTime.now() : startTopic);
        currentVotingTopicSession.setEndTopic(isNull(endTopic) ? LocalDateTime.now().plusMinutes(1) : endTopic);
        return Optional.of(currentVotingTopicSession);
    }

    public void computeVotes(Long topicId, Long associateId, String associateVote) {
        TopicDTO currentVotingTopicSession = findTopicById(topicId);
        String cpf = cpfClient.verifyCpf(String.valueOf(associateId));

        if (!currentVotingTopicSession.getAssociatesVotes().containsKey(associateId) && verifyIfIsAbleToVote(associateId)) {
            currentVotingTopicSession.getAssociatesVotes().put(associateId, associateVote);
        }
    }

    private Boolean verifyIfIsAbleToVote(Long associateId) {
        String cpf = cpfClient.verifyCpf(String.valueOf(associateId));
        return cpf.equalsIgnoreCase("ABLE_TO_VOTE");
    }

    public TopicVotesDTO countVotes(Long topicId) {
        TopicDTO currentVotingTopicSession = findTopicById(topicId);

        return TopicVotesDTO.builder()
                .topicDescription(currentVotingTopicSession.getTopicDescription())
                .positiveVotes(currentVotingTopicSession.getAssociatesVotes().values()
                        .stream().filter(vote -> vote.equalsIgnoreCase("Sim")).count())
                .negativeVotes(currentVotingTopicSession.getAssociatesVotes().values()
                        .stream().filter(vote -> vote.equalsIgnoreCase("Não")).count())
                .build();
    }
}
