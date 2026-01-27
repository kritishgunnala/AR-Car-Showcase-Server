package com.arcarshowcaseserver.service;

import com.arcarshowcaseserver.dto.CarQueryResponse;
import com.arcarshowcaseserver.dto.MakeDTO;
import com.arcarshowcaseserver.dto.ModelDTO;
import com.arcarshowcaseserver.dto.TrimDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class CarQueryClient {

    private final RestTemplate restTemplate;
    private static final String BASE_URL = "https://www.carqueryapi.com/api/0.3/";

    public List<MakeDTO> getMakes(Integer year) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("cmd", "getMakes");
        if (year != null) {
            builder.queryParam("year", year);
        }

        String url = builder.toUriString();
        log.info("Calling CarQueryAPI: {}", url);

        ResponseEntity<CarQueryResponse<List<MakeDTO>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(getHeaders()),
                new ParameterizedTypeReference<CarQueryResponse<List<MakeDTO>>>() {}
        );

        log.info("Response status: {}", response.getStatusCode());
        log.info("Response body: {}", response.getBody());

        return response.getBody() != null ? response.getBody().getMakes() : List.of();
    }

    public List<ModelDTO> getModels(String make, Integer year) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("cmd", "getModels")
                .queryParam("make", make);
        if (year != null) {
            builder.queryParam("year", year);
        }

        ResponseEntity<CarQueryResponse<List<ModelDTO>>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(getHeaders()),
                new ParameterizedTypeReference<CarQueryResponse<List<ModelDTO>>>() {}
        );

        return response.getBody() != null ? response.getBody().getModels() : List.of();
    }

    public List<TrimDTO> getTrims(String make, String model, Integer year) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("cmd", "getTrims")
                .queryParam("make", make);
        if (model != null) {
            builder.queryParam("model", model);
        }
        if (year != null) {
            builder.queryParam("year", year);
        }

        ResponseEntity<CarQueryResponse<List<TrimDTO>>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(getHeaders()),
                new ParameterizedTypeReference<CarQueryResponse<List<TrimDTO>>>() {}
        );

        return response.getBody() != null ? response.getBody().getTrims() : List.of();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        return headers;
    }
}
