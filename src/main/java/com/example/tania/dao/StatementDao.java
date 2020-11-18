package com.example.tania.dao;

import com.example.tania.module.Statement;
import java.util.List;


public interface StatementDao {
    public List<Statement> findAllStatement();

    public List<Statement> findStatementsById(String accountId);
}
