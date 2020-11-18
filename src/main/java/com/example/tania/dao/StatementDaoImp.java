package com.example.tania.dao;

import com.example.tania.module.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

@Repository
public class StatementDaoImp implements StatementDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Statement> findAllStatement() {
        String sql = "select * from statement as s LEFT OUTER JOIN account as a on s.account_Id=a.id";

        List<Statement> statements = jdbcTemplate.query(sql, new Object[] {}, new BeanPropertyRowMapper(Statement.class));
        return statements;
    }

    @Override
    public List<Statement> findStatementsById(String accountId) {
        String sql = "select * from statement as s LEFT OUTER JOIN account as a on (s.account_Id=a.id) where account_id = '" + accountId + "'";

        List<Statement> statements = jdbcTemplate.query(sql, new Object[] {}, new BeanPropertyRowMapper(Statement.class));
        return statements;
    }

}
