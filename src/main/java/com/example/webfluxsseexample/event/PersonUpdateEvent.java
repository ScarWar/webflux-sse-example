package com.example.webfluxsseexample.event;

import java.util.Date;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class PersonUpdateEvent {

  private final String personId;
  private final Date updateDate;
  private final String location;
}
