package com.example.tania.dao;

import com.example.tania.module.Account;
import com.example.tania.module.Statement;

import java.util.List;


public interface AccountDao {
    /**
     * Find account by account ID.
     *
     * @param accountId
     * @return
     */
    public Account findAccountById(String accountId);
}
