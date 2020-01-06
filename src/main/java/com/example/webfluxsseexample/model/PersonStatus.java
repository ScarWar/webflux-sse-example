package com.example.webfluxsseexample.model;

import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Document
public class PersonStatus {

  @Id
  private String id;
  @NotNull
  private String status;
  @NotNull
  private String personId;
  @NotNull
  private Date updateDate;
}
