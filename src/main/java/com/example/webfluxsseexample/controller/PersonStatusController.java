package com.example.webfluxsseexample.controller;

import com.example.webfluxsseexample.event.PersonUpdateEvent;
import com.example.webfluxsseexample.model.PersonStatus;
import com.example.webfluxsseexample.model.PersonStatusDto;
import com.example.webfluxsseexample.repository.PersonStatusRepository;
import com.example.webfluxsseexample.service.PersonStatusUpdateService;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/person-status")
public class PersonStatusController {

  private final PersonStatusUpdateService personStatusUpdateService;
  private PersonStatusRepository personStatusRepository;

  public PersonStatusController(
      PersonStatusUpdateService personStatusUpdateService,
      PersonStatusRepository personStatusRepository) {
    this.personStatusUpdateService = personStatusUpdateService;
    this.personStatusRepository = personStatusRepository;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<PersonStatus> addStatus() {
    return personStatusRepository.insert(PersonStatus.builder()
        .personId(UUID.randomUUID().toString())
        .status("foo")
        .updateDate(new Date())
        .build());
  }

  @GetMapping(value = "/updates", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ServerSentEvent<PersonStatusDto>> streamUpdates(
      @RequestParam("names") final List<String> names) {
    return personStatusUpdateService.getChangeStream()
        .filter(personUpdateEvent ->
            names.contains(personUpdateEvent.getLocation()))
        .map(this::translateToUserEvent);
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

