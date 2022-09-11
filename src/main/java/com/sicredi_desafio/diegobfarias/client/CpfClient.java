package com.sicredi_desafio.diegobfarias.client;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CpfClient {
    final String cpfVerifyUrl = "https://user-info.herokuapp.com/users/";
    RestTemplate restTemplate = new RestTemplateBuilder().rootUri(cpfVerifyUrl).build();

    public String verifyCpf(String cpf) {
        return restTemplate.getForObject(cpf, String.class);
    }
}
