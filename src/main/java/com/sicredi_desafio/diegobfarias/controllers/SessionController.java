package com.sicredi_desafio.diegobfarias.controllers;

import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicDocumentDTO;
import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicVotesDTO;
import com.sicredi_desafio.diegobfarias.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping(value = "/create")
    public ResponseEntity<TopicDocumentDTO> createNewTopic(@RequestBody TopicDocumentDTO topicDocumentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionService.createNewTopic(topicDocumentDTO));
    }

    @PutMapping(value = "/{topicId}/new-voting-topic-session")
    public ResponseEntity<TopicDocumentDTO> openNewVotingTopicSession(
            @RequestParam(value = "startTopic") LocalDateTime startTopic,
            @RequestParam(value = "endTopic") LocalDateTime endTopic,
            @PathVariable Long topicId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(sessionService.openNewVotingTopicSession(topicId, startTopic, endTopic));
    }

    @PutMapping(value = "/{topicId}/compute-votes")
    public ResponseEntity computeVotes(
            @RequestParam(value = "associateId") Long associateId,
            @RequestParam(value = "associateVote") String associateVote,
            @PathVariable Long topicId) {
        sessionService.computeVotes(topicId, associateId, associateVote);
        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/{topicId}/count")
    public ResponseEntity<TopicVotesDTO> countVotes(@PathVariable Long topicId) {
        return ResponseEntity.status(HttpStatus.OK).body(sessionService.countVotes(topicId));
    }
}
