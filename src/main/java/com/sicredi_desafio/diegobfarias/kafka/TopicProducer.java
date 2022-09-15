package com.sicredi_desafio.diegobfarias.kafka;

import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicDocumentKafkaDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TopicProducer {

    @Autowired
    private KafkaTemplate<String, TopicDocumentKafkaDTO> kafkaTemplate;

    public void send(String topicName, TopicDocumentKafkaDTO topicDocumentKafkaDTO) {
        kafkaTemplate.send(topicName, topicDocumentKafkaDTO);
    }
}
