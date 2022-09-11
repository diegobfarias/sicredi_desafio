package com.sicredi_desafio.diegobfarias.repositories;

import com.sicredi_desafio.diegobfarias.entities.Topic;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Topic, Long> {
}
