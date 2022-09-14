package com.sicredi_desafio.diegobfarias.services;

import com.sicredi_desafio.diegobfarias.services.client.CpfClient;
import com.sicredi_desafio.diegobfarias.controllers.dtos.CpfDTO;
import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicDocumentDTO;
import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicVotesDTO;
import com.sicredi_desafio.diegobfarias.documents.TopicDocument;
import com.sicredi_desafio.diegobfarias.repositories.TopicRepository;
import com.sicredi_desafio.diegobfarias.services.exceptions.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.time.ZoneOffset.UTC;
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
    private final LocalDateTime OLD_START_DATE = LocalDateTime.now().minusMonths(2);
    private final LocalDateTime OLD_END_DATE = LocalDateTime.now().minusMonths(1);
    private final LocalDateTime END_DATE_PLUS_ONE_DAY = LocalDateTime.now().plusDays(1);
    private final LocalDateTime END_DATE_PLUS_60 = LocalDateTime.now().plusSeconds(60);
    private final String TOPIC_DESCRIPTION = "Pauta teste";
    private final String ASSOCIATE_ID = "12345678912";
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
        Map<String, String> associatesVotes = new HashMap<>();
        associatesVotes.put(ASSOCIATE_ID, ASSOCIATE_VOTE_YES);

        TopicDocumentDTO topicDocumentDTO = TopicDocumentDTO.builder()
                .id(TOPIC_ID)
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

        when(topicRepository.findById(anyString())).thenReturn(empty());
        when(topicRepository.save(any(TopicDocument.class))).thenReturn(topicDocument);

        TopicDocumentDTO newTopic = topicService.createNewTopic(topicDocumentDTO);

        assertEquals(newTopic.getTopicDescription(), topicDocumentDTO.getTopicDescription());
    }

    @Test
    public void givenATopicThatAlreadyExists_whenCreateNewTopic_thenShouldThrowTopicAlreadyExistsException() {
        Map<String, String> associatesVotes = new HashMap<>();
        associatesVotes.put(ASSOCIATE_ID, ASSOCIATE_VOTE_YES);

        TopicDocumentDTO topicDocumentDTO = TopicDocumentDTO.builder()
                .id(TOPIC_ID)
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

        when(topicRepository.findById(anyString())).thenReturn(ofNullable(topicDocument));
        when(topicRepository.save(any(TopicDocument.class))).thenThrow(new TopicAlreadyExistsException(topicDocumentDTO.getTopicDescription()));

        assertThrows(TopicAlreadyExistsException.class, () -> topicService.createNewTopic(topicDocumentDTO));
    }

    @Test
    public void givenAStartAndEndDates_whenOpenNewTopicSession_thenShouldOpenANewTopicSessionSuccessfully() {
        Map<String, String> associatesVotes = new HashMap<>();
        associatesVotes.put(ASSOCIATE_ID, ASSOCIATE_VOTE_YES);

        TopicDocumentDTO topicDocumentDTO = TopicDocumentDTO.builder()
                .id(TOPIC_ID)
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

        when(topicRepository.findById(anyString())).thenReturn(ofNullable(topicDocument));
        when(topicRepository.save(any(TopicDocument.class))).thenReturn(topicDocument);

        TopicDocumentDTO newTopic = topicService.openNewVotingTopicSession(TOPIC_ID, START_DATE, END_DATE_PLUS_ONE_DAY);

        assertEquals(newTopic.getStartTopic(), topicDocumentDTO.getStartTopic());
        assertEquals(newTopic.getEndTopic(), topicDocumentDTO.getEndTopic());
    }

    @Test
    public void givenNoneStartAndEndDates_whenOpenNewTopicSession_thenShouldOpenANewTopicSessionWithDurationOfOneMinute() {
        Map<String, String> associatesVotes = new HashMap<>();
        associatesVotes.put(ASSOCIATE_ID, ASSOCIATE_VOTE_YES);

        TopicDocument topicDocument = TopicDocument.builder()
                .id(TOPIC_ID)
                .topicDescription(TOPIC_DESCRIPTION)
                .startTopic(START_DATE)
                .endTopic(END_DATE_PLUS_60)
                .associatesVotes(associatesVotes)
                .build();

        when(topicRepository.findById(anyString())).thenReturn(ofNullable(topicDocument));
        when(topicRepository.save(any(TopicDocument.class))).thenReturn(topicDocument);

        TopicDocumentDTO newTopic = topicService.openNewVotingTopicSession(anyString(), null, null);

        Long interval = newTopic.getEndTopic().toEpochSecond(UTC) - newTopic.getStartTopic().toEpochSecond(UTC);

        assertEquals(60, interval);
    }

    @Test
    public void givenANotExistingTopic_whenOpenNewTopicSession_thenShouldThrowTopicNotFoundException() {
        when(topicRepository.findById(anyString())).thenReturn(empty());
        when(topicRepository.save(any(TopicDocument.class))).thenThrow(new TopicNotFoundException(TOPIC_ID));

        assertThrows(TopicNotFoundException.class, () -> topicService.openNewVotingTopicSession(TOPIC_ID, START_DATE, END_DATE_PLUS_ONE_DAY));
    }

    @Test
    public void givenATopicAndAnAssociateAndItsVote_whenComputeVotes_thenShouldSaveAssociateVote() {
        Map<String, String> associatesVotes = new HashMap<>();
        associatesVotes.put("1234", ASSOCIATE_VOTE_YES);

        TopicDocument topicDocument = TopicDocument.builder()
                .id(TOPIC_ID)
                .topicDescription(TOPIC_DESCRIPTION)
                .startTopic(START_DATE)
                .endTopic(END_DATE_PLUS_ONE_DAY)
                .associatesVotes(associatesVotes)
                .build();
        CpfDTO cpfDTO = CpfDTO.builder().status(ABLE_TO_VOTE).build();

        when(topicRepository.findById(anyString())).thenReturn(ofNullable(topicDocument));
        when(topicRepository.save(any(TopicDocument.class))).thenReturn(topicDocument);
        when(cpfClient.verifyCpf(anyString())).thenReturn(cpfDTO);

        topicService.computeVotes(TOPIC_ID, ASSOCIATE_ID, ASSOCIATE_VOTE_NO);

        assertEquals(ABLE_TO_VOTE, cpfClient.verifyCpf(anyString()).getStatus());
        verify(topicRepository, times(1)).save(topicDocument);
    }

    @Test
    public void givenATopicAndAnAssociateAndItsVote_whenComputeVotesButSessionNotOpen_thenShouldThrowSessionNoLongerOpenException() {
        Map<String, String> associatesVotes = new HashMap<>();
        associatesVotes.put("1234", ASSOCIATE_VOTE_YES);

        TopicDocument topicDocument = TopicDocument.builder()
                .id(TOPIC_ID)
                .topicDescription(TOPIC_DESCRIPTION)
                .startTopic(OLD_START_DATE)
                .endTopic(OLD_END_DATE)
                .associatesVotes(associatesVotes)
                .build();

        when(topicRepository.findById(anyString())).thenReturn(ofNullable(topicDocument));

        assertThrows(SessionNoLongerOpenException.class, () -> topicService.computeVotes(TOPIC_ID, ASSOCIATE_ID, ASSOCIATE_VOTE_NO));
    }

    @Test
    public void givenATopicAndAnAssociateAndItsVoteThatHasAlreadyVoted_whenComputeVotes_thenShouldThrowAreadyVotedException() {
        Map<String, String> associatesVotes = new HashMap<>();
        associatesVotes.put(ASSOCIATE_ID, ASSOCIATE_VOTE_YES);

        TopicDocument topicDocument = TopicDocument.builder()
                .id(TOPIC_ID)
                .topicDescription(TOPIC_DESCRIPTION)
                .startTopic(START_DATE)
                .endTopic(END_DATE_PLUS_ONE_DAY)
                .associatesVotes(associatesVotes)
                .build();

        when(topicRepository.findById(anyString())).thenReturn(ofNullable(topicDocument));

        assertThrows(AssociateAlreadyVotedException.class, () -> topicService.computeVotes(TOPIC_ID, ASSOCIATE_ID, ASSOCIATE_VOTE_NO));
    }

    @Test
    public void givenATopicAndAnInvalidAssociateAndItsVote_whenComputeVotes_thenShouldThrowCpfNotFoundException() {
        Map<String, String> associatesVotes = new HashMap<>();
        associatesVotes.put(ASSOCIATE_ID, ASSOCIATE_VOTE_YES);

        TopicDocument topicDocument = TopicDocument.builder()
                .id(TOPIC_ID)
                .topicDescription(TOPIC_DESCRIPTION)
                .startTopic(START_DATE)
                .endTopic(END_DATE_PLUS_ONE_DAY)
                .associatesVotes(associatesVotes)
                .build();

        when(topicRepository.findById(anyString())).thenReturn(ofNullable(topicDocument));
        when(cpfClient.verifyCpf(anyString())).thenThrow(new CpfNotFoundException(ASSOCIATE_ID));

        assertThrows(CpfNotFoundException.class, () -> cpfClient.verifyCpf(ASSOCIATE_ID));
    }

    @Test
    public void givenATopicId_whenCountVotes_thenShouldCountTheVotesOfThatTopic() {
        Map<String, String> associatesVotes = new HashMap<>();
        associatesVotes.put(ASSOCIATE_ID, ASSOCIATE_VOTE_YES);
        associatesVotes.put("12345678923", ASSOCIATE_VOTE_NO);
        associatesVotes.put("12345678934", ASSOCIATE_VOTE_NO);

        TopicDocument topicDocument = TopicDocument.builder()
                .id(TOPIC_ID)
                .topicDescription(TOPIC_DESCRIPTION)
                .startTopic(START_DATE)
                .endTopic(END_DATE_PLUS_ONE_DAY)
                .associatesVotes(associatesVotes)
                .build();

        when(topicRepository.findById(anyString())).thenReturn(ofNullable(topicDocument));

        TopicVotesDTO newTopicVotes = topicService.countVotes(TOPIC_ID);

        assertEquals(newTopicVotes.getNegativeVotes(), NEGATIVE_VOTES);
        assertEquals(newTopicVotes.getPositiveVotes(), POSITIVE_VOTES);
    }
}
