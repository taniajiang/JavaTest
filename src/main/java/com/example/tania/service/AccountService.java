package com.example.tania.service;

import com.example.tania.module.Account;

public interface AccountService {
    /**
     * Get account by ID, if have more same then return the first one.
     *
     * @param accountId
     * @return Account
     */
    Account getAccountById(String accountId);
}
