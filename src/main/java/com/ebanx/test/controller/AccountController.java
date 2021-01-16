package com.ebanx.test.controller;

import com.ebanx.test.domain.Account;
import com.ebanx.test.dto.AccountDTO;
import com.ebanx.test.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import javax.validation.Valid;

@AllArgsConstructor
@Controller
public class AccountController {

    private AccountService accountService;

    @PostMapping( value = "/reset")
    public ResponseEntity<String> reset() {
        accountService.reset();
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @GetMapping(produces = "application/json", value = "/balance")
    public ResponseEntity<Integer> balance(@Valid @RequestParam String account_id) {

        return ResponseEntity.status(HttpStatus.OK).body(accountService.balance(account_id).getBalance());
    }

    @PostMapping(consumes = "application/json", produces = "application/json", value = "/event")
    public ResponseEntity<Object> events( @RequestBody AccountDTO event) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.handleEvent(event));
    }
}
