package com.ebanx.test.dto;

import lombok.Data;

@Data
public class AccountTransferDTO {
    private AccountDTO origin;
    private AccountDTO destiny;
}
