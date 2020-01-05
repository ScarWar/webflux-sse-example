package com.example.webfluxsseexample.controller;

import com.example.webfluxsseexample.event.PersonUpdateEvent;
import com.example.webfluxsseexample.model.PersonStatusDto;
import com.example.webfluxsseexample.service.PersonStatusUpdateService;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/person-status")
public class PersonStatusController {

  private final PersonStatusUpdateService personStatusUpdateService;

  public PersonStatusController(
      PersonStatusUpdateService personStatusUpdateService) {
    this.personStatusUpdateService = personStatusUpdateService;
  }

  @GetMapping(value = "/updates", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ServerSentEvent<PersonStatusDto>> echoNames(
      @RequestParam("names") final List<String> names) {
    return personStatusUpdateService.getChangeStream().filter(personUpdateEvent ->
        names.contains(personUpdateEvent.getLocation())
    ).map(this::translateToUserEvent);
  }

  private ServerSentEvent<PersonStatusDto> translateToUserEvent(
      PersonUpdateEvent personUpdateEvent) {
    return ServerSentEvent.builder(PersonStatusDto.builder()
        .personId(personUpdateEvent.getPersonId())
        .location(personUpdateEvent.getLocation())
        .updateDate(personUpdateEvent.getUpdateDate())
        .name("Ariel")
        .build()
    ).build();
  }
}

