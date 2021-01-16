package com.ebanx.test.repository;

import com.ebanx.test.domain.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, String> {

    @Modifying
    @Query(
            value = "truncate table account",
            nativeQuery = true
    )
    void truncateAccount();

    Account getById(String s);
}
