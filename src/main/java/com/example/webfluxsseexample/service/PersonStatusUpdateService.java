package com.example.webfluxsseexample.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.example.webfluxsseexample.event.PersonUpdateEvent;
import com.example.webfluxsseexample.model.PersonStatus;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class PersonStatusUpdateService {

  private final ReactiveMongoTemplate reactiveTemplate;
  private Flux<ChangeStreamEvent<PersonStatus>> changeStream;

  public PersonStatusUpdateService(
      ReactiveMongoTemplate reactiveTemplate) {
    this.reactiveTemplate = reactiveTemplate;
  }

  @PostConstruct
  void initUpdateStream() {
    this.changeStream = reactiveTemplate
        .changeStream("personStatus", ChangeStreamOptions.builder()
            .filter(newAggregation(match(where("operationType").is("insert"))))
            .build(), PersonStatus.class);
  }

  public Flux<PersonUpdateEvent> getChangeStream() {
    return changeStream.map(event -> {
      PersonStatus body = event.getBody();
      Objects.requireNonNull(body);
      return PersonUpdateEvent.builder()
          .personId(body.getId())
          .updateDate(Date.from(Instant.now()))
          .location("ariel")
          .build();
    });
  }
}
