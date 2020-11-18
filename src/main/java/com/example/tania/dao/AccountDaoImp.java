package com.example.tania.dao;

import com.example.tania.module.Account;
import com.example.tania.module.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.ListUtils;

import java.util.List;

@Repository
public class AccountDaoImp implements AccountDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Account findAccountById(String accountId) {
        String sql = "select * from account where id = '" + accountId + "'";

        List<Account> accounts = jdbcTemplate.query(sql, new Object[] {}, new BeanPropertyRowMapper(Account.class));
        return !ListUtils.isEmpty(accounts) ? accounts.get(0) : null;
    }
}
