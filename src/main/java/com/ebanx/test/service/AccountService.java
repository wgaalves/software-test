package com.ebanx.test.service;

import com.ebanx.test.domain.Account;
import com.ebanx.test.dto.AccountDTO;
import com.ebanx.test.exception.NotFoundException;
import com.ebanx.test.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

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

    if (!accountRepository.existsById(accountDTO.getId())) {
      account = accountDTO.toDomain();
    } else {
      account = accountRepository.getById(accountDTO.getId());
      BigDecimal amount = account.getBalance().add(accountDTO.getAmount());
      account.setBalance(amount);
    }
    accountRepository.save(account);
    return populateHashMap(account, null);
  }

  public Object withdraw(AccountDTO accountDTO) {
    if (accountRepository.existsById(accountDTO.getId())) {
      Account account = accountRepository.getById(accountDTO.getId());
      BigDecimal amount = account.getBalance().subtract(accountDTO.getAmount());
      account.setBalance(amount);
      accountRepository.save(accountDTO.toDomain());
      return populateHashMap(null, account);
    }
    throw new NotFoundException("Account Not Found");
  }

  public Object transfer(AccountDTO accountDTO) {
    if (accountRepository.existsById(accountDTO.getId())
        || accountRepository.existsById(accountDTO.getOrigin())) {
      Account origin = accountRepository.getById(accountDTO.getId());
      Account destination = accountRepository.getById(accountDTO.getOrigin());

      BigDecimal originAmount = origin.getBalance().subtract(accountDTO.getAmount());
      BigDecimal destinationAmount = destination.getBalance().add(accountDTO.getAmount());
      origin.setBalance(originAmount);
      destination.setBalance(destinationAmount);

      accountRepository.save(origin);
      accountRepository.save(destination);

      return populateHashMap(destination, origin);
    } else if (!accountRepository.existsById(accountDTO.getId())) {
      throw new NotFoundException("Account Origin Not Found");
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
