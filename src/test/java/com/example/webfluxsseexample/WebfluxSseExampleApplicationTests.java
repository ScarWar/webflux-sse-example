package com.example.webfluxsseexample;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.BodySpec;

@WebFluxTest(WebfluxSseExampleApplication.class)
class WebfluxSseExampleApplicationTests {

  @Autowired
  WebTestClient webTestClient;

  @Test
  void contextLoads() {
  }

  @Test
  public void givenParams_whenSent_ShouldBeReturnInStream() {
    BodySpec<String, ?> stringBodySpec = webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/sse-server/sse-stream")
            .queryParam("names", "Ariel", "Yarden", "Noa")
            .build())
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
        .expectBody(String.class)
        .consumeWith(stringEntityExchangeResult ->
            System.out.println(stringEntityExchangeResult.getResponseBody()));

    System.out.printf("Response = %s", stringBodySpec.returnResult()
        .getResponseBody());
    ;
  }


}
