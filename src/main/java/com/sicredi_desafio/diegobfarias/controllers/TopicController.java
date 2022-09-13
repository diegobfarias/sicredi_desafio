package com.sicredi_desafio.diegobfarias.controllers;

import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicDocumentDTO;
import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicVotesDTO;
import com.sicredi_desafio.diegobfarias.services.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/pauta")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @Operation(description = "API responsável de criar uma nova pauta")
    @PostMapping(value = "/criar")
    public ResponseEntity<TopicDocumentDTO> createNewTopic(@RequestBody TopicDocumentDTO topicDocumentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.createNewTopic(topicDocumentDTO));
    }

    @Operation(description = "Abre uma nova sessão de votação de uma pauta")
    @PutMapping(value = "/{topicId}/nova-sessao-votacao")
    public ResponseEntity<TopicDocumentDTO> openNewVotingTopicSession(
            @RequestParam(value = "startTopic") LocalDateTime startTopic,
            @RequestParam(value = "endTopic") LocalDateTime endTopic,
            @PathVariable Long topicId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.openNewVotingTopicSession(topicId, startTopic, endTopic));
    }

    @Operation(description = "Recebe os votos dos associados em uma pauta")
    @PutMapping(value = "/{topicId}/votar")
    public ResponseEntity computeVotes(
            @RequestParam(value = "associateId") Long associateId,
            @RequestParam(value = "associateVote") String associateVote,
            @PathVariable Long topicId) {
        topicService.computeVotes(topicId, associateId, associateVote);
        return ResponseEntity.accepted().build();
    }

    @Operation(description = "Realiza a contagem dos votos de uma pauta")
    @GetMapping(value = "/{topicId}/contar-votos")
    public ResponseEntity<TopicVotesDTO> countVotes(@PathVariable Long topicId) {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.countVotes(topicId));
    }
}
