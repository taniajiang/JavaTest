package com.example.tania.service;

import com.example.tania.module.SearchData;
import com.example.tania.module.Statement;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface StatementService {
    public List<Statement> getAllStatement();

    public List<Statement> getStatementsById(SearchData searchData);
}
