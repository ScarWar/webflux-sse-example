package com.example.webfluxsseexample.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@RequiredArgsConstructor
@Document
public class PersonStatus {
  @Id private String id;
  private final String personName;
  private final String status;
}
