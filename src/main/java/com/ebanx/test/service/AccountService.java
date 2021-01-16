package com.ebanx.test.service;

import com.ebanx.test.domain.Account;
import com.ebanx.test.dto.AccountDTO;
import com.ebanx.test.exception.NotFoundException;
import com.ebanx.test.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

@Log
@AllArgsConstructor
@Service
public class AccountService {

  private final AccountRepository accountRepository;

  @Transactional
  public void reset() {
    accountRepository.truncateAccount();
  }

  public Object handleEvent(AccountDTO accountDTO) {
    switch (accountDTO.getType()) {
      case "deposit":
        return deposit(accountDTO);
      case "withdraw":
        return withdraw(accountDTO);
      case "transfer":
        return transfer(accountDTO);
      default:
        throw new NotFoundException("Event Not Found");
    }
  }

  public Object deposit(AccountDTO accountDTO) {
    Account account;

    if (!accountRepository.existsById(accountDTO.getDestination())) {
      account = accountDTO.toDomain();
    } else {
      account = accountRepository.getById(accountDTO.getDestination());
      account.setBalance(account.getBalance() + accountDTO.getAmount());
    }
    accountRepository.save(account);
    return populateHashMap(account, null);
  }

  public Object withdraw(AccountDTO accountDTO) {
    if (accountRepository.existsById(accountDTO.getOrigin())) {
      Account account = accountRepository.getById(accountDTO.getOrigin());
      Integer amount = account.getBalance() - accountDTO.getAmount();
      account.setBalance(amount);
      accountRepository.save(account);
      return populateHashMap(null, account);
    }
    throw new NotFoundException("Account Not Found");
  }

  public Object transfer(AccountDTO accountDTO) {
    if (accountRepository.existsById(accountDTO.getOrigin())) {
      Account origin = accountRepository.getById(accountDTO.getOrigin());
      Account destination;
      Integer originAmount = origin.getBalance() - accountDTO.getAmount();
      origin.setBalance(originAmount);
      accountRepository.save(origin);

      if (accountRepository.existsById(accountDTO.getDestination())) {

        destination = accountRepository.getById(accountDTO.getDestination());
        Integer destinationAmount = destination.getBalance() + accountDTO.getAmount();
        destination.setBalance(destinationAmount);

      } else {

        destination = new Account();
        destination.setId(accountDTO.getDestination());
        destination.setBalance(accountDTO.getAmount());
      }
      accountRepository.save(destination);
      return populateHashMap(destination, origin);
    } else {
      throw new NotFoundException("Account Destiny Not Found");
    }
  }

  public Account balance(String id) {
    if (accountRepository.existsById(id)) {
      return accountRepository.getById(id);
    }

    throw new NotFoundException("Account Not Found");
  }

  public Object populateHashMap(Account destination, Account origin) {
    LinkedHashMap<String, Account> response = new LinkedHashMap<>();

    if (destination != null) {
      response.put("destination", destination);
    }

    if (origin != null) {
      response.put("origin", origin);
    }

    return response;
  }
}
