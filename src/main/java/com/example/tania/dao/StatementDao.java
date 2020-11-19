package com.example.tania.dao;

import com.example.tania.module.Statement;
import java.util.List;

public interface StatementDao {
    /**
     * Find all statement per month.
     *
     * @return List<Statement>
     */
    public List<Statement> findAllStatement();

    /**
     * Find statements by account ID.
     *
     * @param accountId
     * @return List<Statement>
     */
    public List<Statement> findStatementsById(String accountId);
}
