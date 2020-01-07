package com.example.webfluxsseexample.controller;

import static org.mockito.Mockito.doReturn;

import com.example.webfluxsseexample.event.PersonUpdateEvent;
import com.example.webfluxsseexample.service.PersonStatusUpdateService;
import java.util.UUID;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.BodySpec;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@WebFluxTest(PersonStatusController.class)
class PersonStatusControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  PersonStatusUpdateService personStatusUpdateService;

  @Test
  public void givenParams_whenSent_ShouldBeReturnInStream() {
    doReturn(Flux.fromArray(Arrays.array(PersonUpdateEvent.builder()
            .personId(UUID.randomUUID().toString())
            .location("Ariel")
            .build(),
        PersonUpdateEvent.builder()
            .location("Aviad")
            .build())))
        .when(personStatusUpdateService).getChangeStream();

    BodySpec<String, ?> bodySpec = webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/person-status/updates")
            .queryParam("names", "Ariel", "Yarden", "Noa")
            .build())
        .accept(MediaType.TEXT_EVENT_STREAM)
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
        .expectBody(String.class).value(sentEvent -> {
//          assertThat(sentEvent.data().getName()).isEqualTo("ariel");
//          assertThat(sentEvent.data().getLocation()).isEqualTo("Ariel");
          System.out.println("sentEvent = " + sentEvent);
        });
    String response = bodySpec.returnResult().getResponseBody();

    System.out.printf("Response = %s", response);
  }

  @Test
  public void x() {
    doReturn(Flux.fromArray(Arrays.array(PersonUpdateEvent.builder()
            .personId(UUID.randomUUID().toString())
            .location("Ariel")
            .build(),
        PersonUpdateEvent.builder()
            .location("Aviad")
            .build())))
        .when(personStatusUpdateService).getChangeStream();

  }
}