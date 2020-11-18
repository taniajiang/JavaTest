package com.example.tania.service;

import com.example.tania.dao.StatementDao;
import com.example.tania.module.SearchData;
import com.example.tania.module.Statement;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StatementServiceImpl implements StatementService {
    final static int DECIMAL_PLACES = 2;
    final static int BEFORE_THREE_MONTH = -3;

    @Autowired
    private StatementDao statementDao;

    public SimpleDateFormat simpleDateFormatDB = new SimpleDateFormat("dd.MM.yyyy");
    public SimpleDateFormat simpleDateFormatRequest = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<Statement> getAllStatement() {
        List<Statement> statements = statementDao.findAllStatement();

        List<Statement> filterStatement = new ArrayList<>();
        if (!ListUtils.isEmpty(statements)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, BEFORE_THREE_MONTH);
            Date threeMonthDate = calendar.getTime();

            for (Statement statement : statements) {
                if (StringUtils.isNotBlank(statement.getDateField())) {
                    try {
                        Date date = simpleDateFormatDB.parse(statement.getDateField());
                        if (date.after(threeMonthDate)) {
                            filterStatement.add(statement);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return filterStatement;
    }

    @Override
    public List<Statement> getStatementsById(SearchData searchData) {
        List<Statement> statements = statementDao.findStatementsById(searchData.getAccountId());

        List<Statement> filterStatement = new ArrayList<>();
        if (!ListUtils.isEmpty(statements)) {
            if (needFilterDate(searchData) && !needFilterAmount(searchData)) {
                filterDate(statements, searchData, filterStatement);
            } else if (!needFilterDate(searchData) && needFilterAmount(searchData)) {
                filterAmount(statements, searchData, filterStatement);
            } else if (needFilterDate(searchData) && needFilterAmount(searchData)) {
                filterDate(statements, searchData, filterStatement);

                List<Statement> statementsAfterDateFilter = filterStatement;
                List<Statement> sub_filterStatement = new ArrayList<>();
                filterAmount(statementsAfterDateFilter, searchData, sub_filterStatement);
                return sub_filterStatement;
            }
        }
        if (needFilterDate(searchData) || needFilterAmount(searchData)) {
            return filterStatement;
        }

        return ListUtils.isEmpty(filterStatement) ? statements : filterStatement;
    }

    private void filterDate(List<Statement> statements, SearchData searchData, List<Statement> filterStatement) {
        for (Statement statement : statements) {
            if (StringUtils.isNotBlank(statement.getDateField())) {
                try {
                    Date date = simpleDateFormatDB.parse(statement.getDateField());
                    String fromDate = searchData.getFromDate();
                    String toDate = searchData.getToDate();
                    String selectDate = searchData.getSelectDate();
                    if (StringUtils.isNotBlank(selectDate) && date.compareTo(simpleDateFormatRequest.parse(selectDate)) == 0) {
                        filterStatement.add(statement);
                    }
                    if (StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate) && date.compareTo(simpleDateFormatRequest.parse(fromDate)) >= 0) {
                        filterStatement.add(statement);
                    } else if (StringUtils.isNotBlank(toDate) && StringUtils.isBlank(fromDate) && date.compareTo(simpleDateFormatRequest.parse(toDate)) <= 0) {
                        filterStatement.add(statement);
                    } else if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && date.compareTo(simpleDateFormatRequest.parse(fromDate)) >= 0 && date.compareTo(simpleDateFormatRequest.parse(toDate)) <= 0) {
                        filterStatement.add(statement);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void filterAmount(List<Statement> statements, SearchData searchData, List<Statement> filterStatement) {
        for (Statement statement : statements) {
            if (StringUtils.isNotBlank(statement.getAmount())) {
                BigDecimal amount = new BigDecimal(statement.getAmount());
                BigDecimal inputAmount = StringUtils.isNotBlank(searchData.getInputAmount()) ? new BigDecimal(searchData.getInputAmount()) : null;
                BigDecimal fromAmount = StringUtils.isNotBlank(searchData.getFromAmount()) ? new BigDecimal(searchData.getFromAmount()) : null;
                BigDecimal toAmount = StringUtils.isNotBlank(searchData.getToAmount()) ? new BigDecimal(searchData.getToAmount()) : null;
                if (null != amount) {
                    amount = amount.setScale(DECIMAL_PLACES, RoundingMode.HALF_UP);
                    if (null != inputAmount && amount.compareTo(inputAmount.setScale(DECIMAL_PLACES, RoundingMode.HALF_UP)) == 0) {
                        filterStatement.add(statement);
                    }
                    if (null != fromAmount && null == toAmount && amount.compareTo(fromAmount.setScale(DECIMAL_PLACES, RoundingMode.HALF_UP)) >= 0) {
                        filterStatement.add(statement);
                    } else if (null == fromAmount && null != toAmount && amount.compareTo(toAmount.setScale(DECIMAL_PLACES, RoundingMode.HALF_UP)) <= 0) {
                        filterStatement.add(statement);
                    } else if (null != fromAmount && null != toAmount && amount.compareTo(toAmount.setScale(DECIMAL_PLACES, RoundingMode.HALF_UP)) <= 0 && amount.compareTo(fromAmount.setScale(DECIMAL_PLACES, RoundingMode.HALF_UP)) >= 0) {
                        filterStatement.add(statement);
                    }
                }
            }
        }
    }

    private boolean needFilterDate(SearchData searchData) {
        return StringUtils.isNotBlank(searchData.getSelectDate()) || StringUtils.isNotBlank(searchData.getFromDate()) || StringUtils.isNotBlank(searchData.getToDate());
    }

    private boolean needFilterAmount(SearchData searchData) {
        return StringUtils.isNotBlank(searchData.getInputAmount()) || StringUtils.isNotBlank(searchData.getFromAmount()) || StringUtils.isNotBlank(searchData.getToAmount());
    }

}
