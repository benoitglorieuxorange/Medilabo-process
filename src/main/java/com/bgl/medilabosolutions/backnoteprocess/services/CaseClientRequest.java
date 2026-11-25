package com.bgl.medilabosolutions.backnoteprocess.services;

import com.bgl.medilabosolutions.backnoteprocess.dto.CaseRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Optional;


@Component
public class CaseClientRequest {

    private final RestClient restClient;

    public CaseClientRequest(RestClient caseRestClient) {
        this.restClient = caseRestClient;
    }

    public Optional<CaseRequestDto> getCaseById(Long id) {
        try {
            return Optional.ofNullable(
                    restClient.get()
                            .uri("/api/process/{id}", id)
                            .retrieve()
                            .body(CaseRequestDto.class)
            );
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

}
