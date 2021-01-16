package com.ebanx.test.dto;

import com.ebanx.test.domain.Account;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccountDTO {

  @JsonProperty("destination")
  private String destination;

  private Integer amount;
  private String type;
  private String origin;

  public Account toDomain() {
    return new Account(this.destination, this.amount);
  }
}
