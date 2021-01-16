package com.ebanx.test.dto;

import com.ebanx.test.domain.Account;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDTO {

  @JsonProperty("destination")
  private String id;

  private BigDecimal amount;
  private String type;
  private String origin;

  public Account toDomain() {
    return new Account(this.id, this.amount);
  }
}
