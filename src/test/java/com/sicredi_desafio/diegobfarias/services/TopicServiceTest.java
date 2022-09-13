package com.sicredi_desafio.diegobfarias.services;

import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicDocumentDTO;
import com.sicredi_desafio.diegobfarias.repositories.TopicRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class TopicServiceTest {

    private final Long TOPIC_ID = 25L;
    private final LocalDateTime START_DATE = LocalDateTime.now();
    private final LocalDateTime END_DATE_PLUS_ONE_DAY = LocalDateTime.now().plusDays(1);
    private final LocalDateTime END_DATE_PLUS_60 = LocalDateTime.now().plusSeconds(60);
    private final String TOPIC_DESCRIPTION = "Pauta teste";
    private final Long ASSOCIATE_ID = 1L;
    private final String SIM = "Sim";

    @InjectMocks
    private TopicService topicService;

    @Mock
    private TopicRepository topicRepository;


    @Test
    public void givenANewValidTopic_whenCreateNewTopic_thenShouldCreateANewTopicSuccessfully() {
        Map<Long, String> associatesVotes = new HashMap<>();
        associatesVotes.put(ASSOCIATE_ID, SIM);

        TopicDocumentDTO topicDocumentDTO = TopicDocumentDTO.builder()
                .topicDescription(TOPIC_DESCRIPTION)
                .startTopic(START_DATE)
                .endTopic(END_DATE_PLUS_ONE_DAY)
                .associatesVotes(associatesVotes)
                .build();

        Mockito.when(topicService.createNewTopic(topicDocumentDTO)).thenReturn(new TopicDocumentDTO());

    }
}
