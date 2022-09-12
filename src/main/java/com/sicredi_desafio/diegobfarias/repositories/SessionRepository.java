package com.sicredi_desafio.diegobfarias.repositories;

import com.sicredi_desafio.diegobfarias.documents.TopicDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionRepository extends MongoRepository<TopicDocument, Long> {
}
