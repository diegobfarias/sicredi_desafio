package com.sicredi_desafio.diegobfarias.controllers;

import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicDTO;
import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicVotesDTO;
import com.sicredi_desafio.diegobfarias.services.SessionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.empty;

@RestController
@RequestMapping(value = "/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @ApiOperation(value = "Creates a new topic")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created a new topic"),
            @ApiResponse(code = 400, message = "Validation error. Please check parameters"),
            @ApiResponse(code = 500, message = "Internal server error."),
    })
    @PostMapping(value = "/create")
    public ResponseEntity<TopicDTO> createNewTopic(@RequestBody TopicDTO topicDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionService.createNewTopic(topicDTO));
    }

    @PutMapping(value = "/{id}/new-voting-topic-session")
    public ResponseEntity<Optional<TopicDTO>> openNewVotingTopicSession(
            @RequestParam(value = "startTopic") LocalDateTime startTopic,
            @RequestParam(value = "endTopic") LocalDateTime endTopic,
            @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(sessionService.openNewVotingTopicSession(id, startTopic, endTopic));
    }

    @PutMapping(value = "/{id}/compute-votes")
    public ResponseEntity computeVotes(
            @RequestParam(value = "associateId") Long associateId,
            @RequestParam(value = "associateVote") String associateVote,
            @PathVariable Long id) {
        sessionService.computeVotes(id, associateId, associateVote);
        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/{id}/count")
    public ResponseEntity<TopicVotesDTO> countVotes(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(sessionService.countVotes(id));
    }
}
