package com.sicredi_desafio.diegobfarias.config;

import com.sicredi_desafio.diegobfarias.SicrediChallengeApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

// TODO add url server cloud
@OpenAPIDefinition(tags = {
        @Tag(name = "/v1/sicredi", description = "Grupo de API's para gerenciamento de assembleias de votação")
},
        info = @Info(
                title = "Desafio técnico - Sicredi",
                version = "1.0.0",
                contact = @Contact(
                        name = "Diego Bergonsi de Farias",
                        url = "https://www.linkedin.com/in/diegobfarias/"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080")
        }
)
public class OpenApiConfig extends SicrediChallengeApplication {
}
