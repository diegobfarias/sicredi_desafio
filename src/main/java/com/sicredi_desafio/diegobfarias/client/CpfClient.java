package com.sicredi_desafio.diegobfarias.client;

import com.sicredi_desafio.diegobfarias.controllers.dtos.CpfDTO;
import com.sicredi_desafio.diegobfarias.services.exceptions.CpfNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class CpfClient {

    public CpfDTO verifyCpf(String cpf) {
        log.info("Validando CPF: {}", cpf);

        RestTemplate apiResponse = new RestTemplate();
        CpfDTO cpfDTO;

        try {
            log.info("Realizando a chamada da API de verificação de CPF");
            cpfDTO = apiResponse.getForEntity("https://user-info.herokuapp.com/users/" + cpf, CpfDTO.class).getBody();
        } catch (HttpClientErrorException e) {
            log.error("CPF {} não foi encontrado.", cpf);
            throw new CpfNotFoundException(cpf);
        }

        return cpfDTO;
    }
}
