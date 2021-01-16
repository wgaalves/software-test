package com.ebanx.test.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Account {

  @Id private String id;

  private Integer balance;
}
