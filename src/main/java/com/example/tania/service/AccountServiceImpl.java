package com.example.tania.service;

import com.example.tania.dao.AccountDao;
import com.example.tania.module.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountDao accountDao;

    @Override
    public Account getAccountById(String accountId) {
        return accountDao.findAccountById(accountId);
    }
}
