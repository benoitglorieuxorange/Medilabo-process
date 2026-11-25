package com.bgl.medilabosolutions.backnoteprocess.Config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class CaseClientConfig {

    @Bean
    public RestClient caseRestClient(@Value("http://localhost:8082") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }
}
