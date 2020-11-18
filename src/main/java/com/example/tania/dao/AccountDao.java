package com.example.tania.dao;

import com.example.tania.module.Account;
import com.example.tania.module.Statement;

import java.util.List;

public interface AccountDao {
    public Account findAccountById(String accountId);
}
