package com.example.webfluxsseexample.model;

import java.util.Date;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class PersonStatusDto {
  private final String personId;
  private final String name;
  private final Date updateDate;
  private final String location;
}
