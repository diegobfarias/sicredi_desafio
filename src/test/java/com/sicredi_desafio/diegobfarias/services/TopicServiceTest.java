package com.sicredi_desafio.diegobfarias.services;

import com.sicredi_desafio.diegobfarias.client.CpfClient;
import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicDocumentDTO;
import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicVotesDTO;
import com.sicredi_desafio.diegobfarias.documents.TopicDocument;
import com.sicredi_desafio.diegobfarias.repositories.TopicRepository;
import com.sicredi_desafio.diegobfarias.services.exceptions.TopicAlreadyExistsException;
import com.sicredi_desafio.diegobfarias.services.exceptions.TopicNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TopicServiceTest {

    private final String TOPIC_ID = "25";
    private final Long NEGATIVE_VOTES = 2L;
    private final Long POSITIVE_VOTES = 1L;
    private final LocalDateTime START_DATE = LocalDateTime.now();
    private final LocalDateTime END_DATE_PLUS_ONE_DAY = LocalDateTime.now().plusDays(1);
    private final LocalDateTime END_DATE_PLUS_60 = LocalDateTime.now().plusSeconds(60);
    private final String TOPIC_DESCRIPTION = "Pauta teste";
    private final Long ASSOCIATE_ID = 12345678912L;
    private final String ASSOCIATE_VOTE_YES = "Sim";
    private final String ASSOCIATE_VOTE_NO = "Não";
    private final String ABLE_TO_VOTE = "ABLE_TO_VOTE";

    @InjectMocks
    private TopicService topicService;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private CpfClient cpfClient;


    @Test
    public void givenAValidTopic_whenCreateNewTopic_thenShouldCreateANewTopicSuccessfully() {
        Map<Long, String> associatesVotes = new HashMap<>();
        associatesVotes.put(ASSOCIATE_ID, ASSOCIATE_VOTE_YES);

        TopicDocumentDTO topicDocumentDTO = TopicDocumentDTO.builder()
                .topicDescription(TOPIC_DESCRIPTION)
                .startTopic(START_DATE)
                .endTopic(END_DATE_PLUS_ONE_DAY)
                .associatesVotes(associatesVotes)
                .build();

        TopicDocument topicDocument = TopicDocument.builder()
                .id(TOPIC_ID)
                .topicDescription(TOPIC_DESCRIPTION)
                .startTopic(START_DATE)
                .endTopic(END_DATE_PLUS_ONE_DAY)
                .associatesVotes(associatesVotes)
                .build();

        when(topicRepository.findByTopicDescription(anyString())).thenReturn(empty());
        when(topicRepository.save(any(TopicDocument.class))).thenReturn(topicDocument);

        TopicDocumentDTO newTopic = topicService.createNewTopic(topicDocumentDTO);

        assertEquals(newTopic.getTopicDescription(), topicDocumentDTO.getTopicDescription());
    }

    @Test
    public void givenATopicThatAlreadyExists_whenCreateNewTopic_thenShouldThrowTopicAlreadyExistsException() {
        Map<Long, String> associatesVotes = new HashMap<>();
        associatesVotes.put(ASSOCIATE_ID, ASSOCIATE_VOTE_YES);

        TopicDocumentDTO topicDocumentDTO = TopicDocumentDTO.builder()
                .topicDescription(TOPIC_DESCRIPTION)
                .startTopic(START_DATE)
                .endTopic(END_DATE_PLUS_ONE_DAY)
                .associatesVotes(associatesVotes)
                .build();

        TopicDocument topicDocument = TopicDocument.builder()
                .id(TOPIC_ID)
                .topicDescription(TOPIC_DESCRIPTION)
                .startTopic(START_DATE)
                .endTopic(END_DATE_PLUS_ONE_DAY)
                .associatesVotes(associatesVotes)
                .build();

        when(topicRepository.findByTopicDescription(anyString())).thenReturn(ofNullable(topicDocument));
        when(topicRepository.save(any(TopicDocument.class))).thenThrow(new TopicAlreadyExistsException(topicDocumentDTO.getTopicDescription()));

        assertThrows(TopicAlreadyExistsException.class, () -> {
            topicService.createNewTopic(topicDocumentDTO);
        });
    }

    @Test
    public void givenAStartAndEndDates_whenOpenNewTopicSession_thenShouldOpenANewTopicSessionSuccessfully() {
        Map<Long, String> associatesVotes = new HashMap<>();
        associatesVotes.put(ASSOCIATE_ID, ASSOCIATE_VOTE_YES);

        TopicDocumentDTO topicDocumentDTO = TopicDocumentDTO.builder()
                .topicDescription(TOPIC_DESCRIPTION)
                .startTopic(START_DATE)
                .endTopic(END_DATE_PLUS_ONE_DAY)
                .associatesVotes(associatesVotes)
                .build();

        TopicDocument topicDocument = TopicDocument.builder()
                .id(TOPIC_ID)
                .topicDescription(TOPIC_DESCRIPTION)
                .startTopic(START_DATE)
                .endTopic(END_DATE_PLUS_ONE_DAY)
                .associatesVotes(associatesVotes)
                .build();

        when(topicRepository.findById(anyLong())).thenReturn(ofNullable(topicDocument));
        when(topicRepository.save(any(TopicDocument.class))).thenReturn(topicDocument);

        TopicDocumentDTO newTopic = topicService.openNewVotingTopicSession(TOPIC_ID, START_DATE, END_DATE_PLUS_ONE_DAY);

        assertEquals(newTopic.getStartTopic(), topicDocumentDTO.getStartTopic());
        assertEquals(newTopic.getEndTopic(), topicDocumentDTO.getEndTopic());
    }

    // TODO Corrigir delay da data
    @Test
    public void givenNoneStartAndEndDates_whenOpenNewTopicSession_thenShouldOpenANewTopicSessionWithDurationOfOneMinute() {
        Map<Long, String> associatesVotes = new HashMap<>();
        associatesVotes.put(ASSOCIATE_ID, ASSOCIATE_VOTE_YES);

        TopicDocument topicDocument = TopicDocument.builder()
                .id(TOPIC_ID)
                .topicDescription(TOPIC_DESCRIPTION)
                .startTopic(START_DATE)
                .endTopic(END_DATE_PLUS_60)
                .associatesVotes(associatesVotes)
                .build();

        when(topicRepository.findById(anyLong())).thenReturn(ofNullable(topicDocument));
        when(topicRepository.save(any(TopicDocument.class))).thenReturn(topicDocument);

        TopicDocumentDTO newTopic = topicService.openNewVotingTopicSession(anyString(), null, null);

        assertEquals(START_DATE, newTopic.getStartTopic());
        assertEquals(END_DATE_PLUS_60, newTopic.getEndTopic());
    }

    @Test
    public void givenANotExistingTopic_whenOpenNewTopicSession_thenShouldThrowTopicNotFoundException() {
        Map<Long, String> associatesVotes = new HashMap<>();
        associatesVotes.put(ASSOCIATE_ID, ASSOCIATE_VOTE_YES);

        when(topicRepository.findById(anyLong())).thenReturn(empty());
        when(topicRepository.save(any(TopicDocument.class))).thenThrow(new TopicNotFoundException(TOPIC_ID));

        assertThrows(TopicNotFoundException.class, () -> {
            topicService.openNewVotingTopicSession(TOPIC_ID, START_DATE, END_DATE_PLUS_ONE_DAY);
        });
    }

    @Test
    public void givenATopicAndAnAssociateAndItsVote_whenComputeVotes_thenShouldSaveAssociateVote() {
        Map<Long, String> associatesVotes = new HashMap<>();
        associatesVotes.put(ASSOCIATE_ID, ASSOCIATE_VOTE_YES);

        TopicDocument topicDocument = TopicDocument.builder()
                .id(TOPIC_ID)
                .topicDescription(TOPIC_DESCRIPTION)
                .startTopic(START_DATE)
                .endTopic(END_DATE_PLUS_ONE_DAY)
                .associatesVotes(associatesVotes)
                .build();

        when(topicRepository.findById(anyLong())).thenReturn(ofNullable(topicDocument));
        when(topicRepository.save(any(TopicDocument.class))).thenReturn(topicDocument);
        when(cpfClient.verifyCpf(anyString())).thenReturn(ABLE_TO_VOTE);

        topicService.computeVotes(TOPIC_ID, 63L, ASSOCIATE_VOTE_NO);


        assertEquals(ABLE_TO_VOTE, cpfClient.verifyCpf(String.valueOf(63L)));
        verify(topicRepository, times(1)).save(topicDocument);
    }

    @Test
    public void givenATopicAndAnAssociateAndItsVoteThatHasAlreadyVoted_whenComputeVotes_thenShouldSaveAssociateVote() {
        Map<Long, String> associatesVotes = new HashMap<>();
        associatesVotes.put(ASSOCIATE_ID, ASSOCIATE_VOTE_YES);

        TopicDocument topicDocument = TopicDocument.builder()
                .id(TOPIC_ID)
                .topicDescription(TOPIC_DESCRIPTION)
                .startTopic(START_DATE)
                .endTopic(END_DATE_PLUS_ONE_DAY)
                .associatesVotes(associatesVotes)
                .build();

        when(topicRepository.findById(anyLong())).thenReturn(ofNullable(topicDocument));
        when(topicRepository.save(any(TopicDocument.class))).thenReturn(topicDocument);
        when(cpfClient.verifyCpf(anyString())).thenReturn(ABLE_TO_VOTE);

        topicService.computeVotes(TOPIC_ID, ASSOCIATE_ID, ASSOCIATE_VOTE_NO);


        assertEquals(ABLE_TO_VOTE, cpfClient.verifyCpf(String.valueOf(63L)));
        verify(topicRepository, times(0)).save(topicDocument);
    }

    @Test
    public void givenATopicId_whenCountVotes_thenShouldCountTheVotesOfThatTopic() {
        Map<Long, String> associatesVotes = new HashMap<>();
        associatesVotes.put(ASSOCIATE_ID, ASSOCIATE_VOTE_YES);
        associatesVotes.put(1L, ASSOCIATE_VOTE_NO);
        associatesVotes.put(2L, ASSOCIATE_VOTE_NO);

        TopicDocument topicDocument = TopicDocument.builder()
                .id(TOPIC_ID)
                .topicDescription(TOPIC_DESCRIPTION)
                .startTopic(START_DATE)
                .endTopic(END_DATE_PLUS_ONE_DAY)
                .associatesVotes(associatesVotes)
                .build();

        when(topicRepository.findById(anyLong())).thenReturn(ofNullable(topicDocument));

        TopicVotesDTO newTopicVotes = topicService.countVotes(TOPIC_ID);

        assertEquals(newTopicVotes.getNegativeVotes(), NEGATIVE_VOTES);
        assertEquals(newTopicVotes.getPositiveVotes(), POSITIVE_VOTES);
    }
}
