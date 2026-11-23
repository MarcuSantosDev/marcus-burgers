package com.marcusantosproject.backend.services;

import com.marcusantosproject.backend.dto.address.ViaCepResponse;
import com.marcusantosproject.backend.services.exceptions.BadRequest;
import com.marcusantosproject.backend.services.exceptions.ResourceNotFound;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class ViaCepService {
    private final RestClient restClient;

    public ViaCepService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://viacep.com.br/ws")
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public ViaCepResponse findByZipCode(String zipCode) {

        zipCode = zipCode.replaceAll("\\D", "");

        if (!zipCode.matches("\\d{8}")) {
            throw new BadRequest("CEP inválido");
        }

        ViaCepResponse response;
        try {
            response = restClient.get()
                    .uri("/{zipCode}/json/", zipCode)
                    .retrieve()
                    .body(ViaCepResponse.class);
        } catch (RestClientException exception) {
            throw new BadRequest("Não foi possível consultar o CEP agora");
        }

        if (response == null || Boolean.TRUE.equals(response.error())) {
            throw new ResourceNotFound("CEP não encontrado");
        }

        return new ViaCepResponse(
                response.zipCode(),
                normalize(response.street()),
                normalize(response.complement()),
                normalize(response.neighborhood()),
                normalize(response.city()),
                normalize(response.state()),
                response.error());
    }

    private String normalize(String value) {
        if (value == null)
            return null;
        return value
                .replace("Ã¡", "á").replace("Ã ", "à").replace("Ã¢", "â")
                .replace("Ã£", "ã").replace("Ã¤", "ä")
                .replace("Ã©", "é").replace("Ãª", "ê").replace("Ã­", "í")
                .replace("Ã³", "ó").replace("Ã´", "ô").replace("Ãµ", "õ")
                .replace("Ãº", "ú").replace("Ã§", "ç")
                .replace("Ã", "í").replace("Â", "")
                .replace("JoÃ£o Pessoa", "João Pessoa");
    }
}
