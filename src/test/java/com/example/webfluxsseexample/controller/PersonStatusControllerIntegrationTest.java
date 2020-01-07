package com.example.webfluxsseexample.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import com.example.webfluxsseexample.model.PersonStatusDto;
import com.example.webfluxsseexample.service.PersonStatusUpdateService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PersonStatusControllerIntegrationTest {

  @LocalServerPort
  private int port;

  @MockBean
  PersonStatusUpdateService updateService;

  private WebClient webClient;

  @BeforeAll
  public void setup() {
    webClient = WebClient.create("http://localhost:" + port);
  }

  @Test
  public void shouldHandleTwoConnections() {
    EmitterProcessor<ServerSentEvent<PersonStatusDto>> processor = EmitterProcessor.create();
    doReturn(processor)
        .when(updateService).getChangeStream();

    Flux<ServerSentEvent<PersonStatusDto>> serverSentEventFlux1 = webClient.get()
        .uri("/person-status/updates")
        .accept(MediaType.TEXT_EVENT_STREAM)
        .retrieve()
        .bodyToFlux(new ParameterizedTypeReference<>() {
        });

    Flux<ServerSentEvent<PersonStatusDto>> serverSentEventFlux2 = webClient.get()
        .uri("/person-status/updates")
        .accept(MediaType.TEXT_EVENT_STREAM)
        .retrieve()
        .bodyToFlux(new ParameterizedTypeReference<>() {
        });

    processor.onNext(ServerSentEvent.builder(PersonStatusDto.builder()
        .location("Ariel")
        .build())
        .build());

    processor.onNext(ServerSentEvent.builder(PersonStatusDto.builder()
        .location("Ariel2")
        .build())
        .build());

    checkSubscriber(serverSentEventFlux1);
    checkSubscriber(serverSentEventFlux2);
  }

  private void checkSubscriber(Flux<ServerSentEvent<PersonStatusDto>> serverSentEventFlux1) {
    StepVerifier.create(serverSentEventFlux1)
        .consumeNextWith(sentEvent ->
            assertThat(sentEvent.data().getLocation()).isEqualTo("Ariel"))
        .consumeNextWith(sentEvent ->
            assertThat(sentEvent.data().getLocation()).isEqualTo("Ariel2"));
  }
}
