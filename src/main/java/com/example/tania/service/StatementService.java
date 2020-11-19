package com.example.tania.service;

import com.example.tania.module.SearchData;
import com.example.tania.module.Statement;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface StatementService {
    /**
     * Get all statement
     *
     * @return List<Statement>
     */
    public List<Statement> getAllStatement();

    /**
     * Get statement By ID
     *
     * @param searchData
     * @return
     */
    public List<Statement> getStatementsById(SearchData searchData);
}
