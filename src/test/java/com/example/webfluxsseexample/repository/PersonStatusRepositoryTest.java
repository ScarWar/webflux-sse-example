package com.example.webfluxsseexample.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.webfluxsseexample.model.PersonStatus;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
class PersonStatusRepositoryTest {

  @Autowired
  PersonStatusRepository personStatusRepository;

  @Test
  public void insertDoc() {
    personStatusRepository.insert(new PersonStatus("arrested", "123", new Date()))
        .block();

    assertThat(personStatusRepository.count().block())
        .isEqualTo(1L);
  }
}