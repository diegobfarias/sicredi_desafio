package com.sicredi_desafio.diegobfarias.repositories;

import com.sicredi_desafio.diegobfarias.documents.TopicDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TopicRepository extends MongoRepository<TopicDocument, String> {

}
