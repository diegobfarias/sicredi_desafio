package com.sicredi_desafio.diegobfarias.controllers;

import com.sicredi_desafio.diegobfarias.services.client.CpfClient;
import com.sicredi_desafio.diegobfarias.controllers.dtos.CpfDTO;
import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicDocumentDTO;
import com.sicredi_desafio.diegobfarias.controllers.dtos.TopicVotesDTO;
import com.sicredi_desafio.diegobfarias.services.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    private final CpfClient cpfClient;

    @Operation(description = "Endpoint para criar uma nova pauta")
    @ApiResponse(responseCode = "201 CREATED", description = "Pauta criada com sucesso")
    @PostMapping(value = "/criar")
    public ResponseEntity<TopicDocumentDTO> createNewTopic(@RequestBody TopicDocumentDTO topicDocumentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.createNewTopic(topicDocumentDTO));
    }

    @Operation(description = "Abre uma nova sessão de votação de uma pauta")
    @ApiResponse(responseCode = "201 CREATED", description = "Nova sessão de votação criada com sucesso")
    @PutMapping(value = "/{topicId}/nova-sessao-votacao")
    public ResponseEntity<TopicDocumentDTO> openNewVotingTopicSession(
            @RequestParam(value = "startTopic", required = false) String startTopic,
            @RequestParam(value = "endTopic", required = false) String endTopic,
            @PathVariable String topicId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.openNewVotingTopicSession(topicId, LocalDateTime.parse(startTopic), LocalDateTime.parse(endTopic)));
    }

    @Operation(description = "Recebe os votos dos associados em uma pauta")
    @ApiResponse(responseCode = "202 ACCEPTED", description = "Voto do associado computado com sucesso")
    @PutMapping(value = "/{topicId}/votar")
    public ResponseEntity computeVotes(
            @RequestParam(value = "associateId") String associateId,
            @RequestParam(value = "associateVote") String associateVote,
            @PathVariable String topicId) {
        topicService.computeVotes(topicId, associateId, associateVote);
        return ResponseEntity.accepted().build();
    }

    @Operation(description = "Realiza a contagem dos votos de uma pauta")
    @ApiResponse(responseCode = "200 OK", description = "Contagem dos votos realizada.")
    @GetMapping(value = "/{topicId}/contar-votos")
    public ResponseEntity<TopicVotesDTO> countVotes(@PathVariable String topicId) {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.countVotes(topicId));
    }
}
