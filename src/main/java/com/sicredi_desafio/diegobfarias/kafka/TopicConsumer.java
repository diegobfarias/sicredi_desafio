package com.sicredi_desafio.diegobfarias.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicDocumentDTO;
import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicDocumentKafkaDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.sicredi_desafio.diegobfarias.converter.TopicConverter.toKafkaDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicConsumer {

    @Value("${topic.name.start.producer}")
    private String topicNameNewTopic;

    @Value("${topic.name.end.producer}")
    private String topicNameEndTopic;

    private final TopicProducer topicProducer;

    @KafkaListener(topics = "${topic.name.start.producer}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(TopicDocumentKafkaDTO topicDocumentKafkaDTO) {

        String endTopic = topicDocumentKafkaDTO.getEndTopic();
        LocalDateTime endDateTopicKafkaDto = LocalDateTime.parse(endTopic);

        if (endDateTopicKafkaDto.isAfter(LocalDateTime.now())) {
            topicProducer.send(topicNameNewTopic, topicDocumentKafkaDTO);
        } else {
            topicProducer.send(topicNameEndTopic, topicDocumentKafkaDTO);
        }
    }
}
