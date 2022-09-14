package com.sicredi_desafio.diegobfarias.services;

import com.sicredi_desafio.diegobfarias.services.client.CpfClient;
import com.sicredi_desafio.diegobfarias.controllers.dtos.CpfDTO;
import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicDocumentDTO;
import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicVotesDTO;
import com.sicredi_desafio.diegobfarias.documents.TopicDocument;
import com.sicredi_desafio.diegobfarias.services.exceptions.AssociateAlreadyVotedException;
import com.sicredi_desafio.diegobfarias.services.exceptions.SessionNoLongerOpenException;
import com.sicredi_desafio.diegobfarias.services.exceptions.TopicAlreadyExistsException;
import com.sicredi_desafio.diegobfarias.services.exceptions.TopicNotFoundException;
import com.sicredi_desafio.diegobfarias.repositories.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.sicredi_desafio.diegobfarias.Constants.*;
import static com.sicredi_desafio.diegobfarias.converter.TopicConverter.toDTO;
import static com.sicredi_desafio.diegobfarias.converter.TopicConverter.toEntity;
import static java.time.ZoneOffset.UTC;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopicService {

    private final TopicRepository topicRepository;
    private final CpfClient cpfClient;

    public TopicDocumentDTO createNewTopic(TopicDocumentDTO topicDocumentDto) {
        log.info("Criando nova pauta, descrição: {}", topicDocumentDto.getTopicDescription());
        if (verifiyIfTopicAlreadyExists(topicDocumentDto)) {
            throw new TopicAlreadyExistsException(toEntity(topicDocumentDto).getId());
        }
        return toDTO(topicRepository.save(toEntity(topicDocumentDto)));
    }

    private Boolean verifiyIfTopicAlreadyExists(TopicDocumentDTO topicDocumentDTO) {
        return topicRepository.findById(toEntity(topicDocumentDTO).getId()).isPresent();
    }

    private TopicDocument findTopicById(String topicId) {
        log.info("Buscando pauta pela id: {}", topicId);
        return topicRepository.findById(topicId).orElseThrow(
                () -> new TopicNotFoundException(topicId));
    }

    public TopicDocumentDTO openNewVotingTopicSession(String topicId, LocalDateTime startTopic, LocalDateTime endTopic) {
        log.info("Abrindo nova sessão de votação para a pauta {} com tempo de início em {} e fim em {}", topicId, startTopic, endTopic);
        TopicDocument currentVotingTopicSession = findTopicById(topicId);

        currentVotingTopicSession.setStartTopic(isNull(startTopic) ? LocalDateTime.now() : startTopic);
        currentVotingTopicSession.setEndTopic(isNull(endTopic) ? LocalDateTime.now().plusMinutes(1L) : endTopic);
        return toDTO(topicRepository.save(currentVotingTopicSession));
    }

    public void computeVotes(String topicId, String associateId, String associateVote) {
        TopicDocument currentVotingTopicSession = findTopicById(topicId);

        if (currentVotingTopicSession.getAssociatesVotes().containsKey(associateId)) {
            throw new AssociateAlreadyVotedException(associateId);
        } else if (isSessionStillOpen(topicId)) {
            throw new SessionNoLongerOpenException(topicId);
        } else if (verifyIfIsAbleToVote(associateId)) {
            log.info("Computando e salvando voto do associado {} para a pauta {}", associateId, topicId);
            currentVotingTopicSession.getAssociatesVotes().put(associateId, associateVote);
            topicRepository.save(currentVotingTopicSession);
        }
    }

    private Boolean isSessionStillOpen(String topicId) {
        log.info("Verificando se a sessão {} ainda está aberta.", topicId);
        return LocalDateTime.now().toEpochSecond(UTC) > findTopicById(topicId).getEndTopic().toEpochSecond(UTC);
    }

    private Boolean verifyIfIsAbleToVote(String associateId) {
        log.info("Validando se está liberado para votar o ID do associado: {}", associateId);
        CpfDTO cpf = cpfClient.verifyCpf(associateId);
        log.info("CPF {} encontrado com valor {}", associateId, cpf.getStatus());
        return cpf.getStatus().equalsIgnoreCase(ABLE_TO_VOTE);
    }

    public TopicVotesDTO countVotes(String topicId) {
        TopicDocument currentVotingTopicSession = findTopicById(topicId);
        log.info("Contando os votos da pauta: {}", topicId);

        return TopicVotesDTO.builder()
                .topicDescription(currentVotingTopicSession.getTopicDescription())
                .positiveVotes(currentVotingTopicSession.getAssociatesVotes().values()
                        .stream().filter(vote -> vote.equalsIgnoreCase(SIM)).count())
                .negativeVotes(currentVotingTopicSession.getAssociatesVotes().values()
                        .stream().filter(vote -> vote.equalsIgnoreCase(NÃO)).count())
                .build();
    }
}
