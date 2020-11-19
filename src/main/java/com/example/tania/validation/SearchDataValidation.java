package com.example.tania.validation;

import com.example.tania.module.SearchData;
import com.example.tania.service.AccountService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("searchDataValidation")
public class SearchDataValidation implements Validator {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Pattern accountIdPattern =  Pattern.compile("^[1-9]+[0-9]*$");
    private Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");

    @Autowired
    private AccountService accountService;

    @Override
    public boolean supports(Class<?> clazz) {
        return SearchData.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final SearchData searchData = (SearchData) target;
        String accountId = searchData.getAccountId();
        String fromDate = searchData.getFromDate();
        String toDate = searchData.getToDate();
        String inputAmount = searchData.getInputAmount();
        String fromAmount = searchData.getFromAmount();
        String toAmount = searchData.getToAmount();

        //Account ID should not be empty, and need match format and exist in DB
        if (StringUtils.isBlank(accountId)) {
            errors.rejectValue("accountId", "Account ID is required");
        }
        if (StringUtils.isNotBlank(accountId) && (!validAccountId(accountId) || null == accountService.getAccountById(accountId))) {
            errors.rejectValue("accountId", "Account ID is not correct");
        }

        // Date validation
        if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
            try {
                Date from = simpleDateFormat.parse(fromDate);
                Date to = simpleDateFormat.parse(toDate);
                if (from.compareTo(to) <= 0) {
                    errors.rejectValue("fromDate", "From date can not after to date");
                }
            } catch (ParseException e) {
                errors.rejectValue("fromDate", "Date format is not currect");
            }
        }

        // Amount validation
        if (StringUtils.isNotBlank(inputAmount)) {
            checkAmount(inputAmount, errors, "inputAmount", "Input amount is not currect");
        }
        if (StringUtils.isNotBlank(fromAmount)) {
            checkAmount(fromAmount, errors, "fromAmount", "From amount is not currect");
        }
        if (StringUtils.isNotBlank(toAmount)) {
            checkAmount(toAmount, errors, "toAmount", "To amount is not currect");
        }
        if(StringUtils.isNotBlank(fromAmount) && StringUtils.isNotBlank(toAmount)){
            BigDecimal from_Amount = StringUtils.isNotBlank(searchData.getFromAmount())? new BigDecimal(searchData.getFromAmount()) : null;
            BigDecimal to_Amount = StringUtils.isNotBlank(searchData.getToAmount())? new BigDecimal(searchData.getToAmount()) : null;
            if(from_Amount.compareTo(to_Amount) >= 0){
                errors.rejectValue("fromAmount", "From amount can not bigger than to amount");
            }
        }
    }

    /**
     * Check account ID format (/^[1-9]+[0-9]*$/)
     *
     * @param accountId
     * @return boolean
     */
    private boolean validAccountId(String accountId) {
        Matcher match = accountIdPattern.matcher(accountId);
        if (match.matches() == false) {
            return false;
        }
        return true;
    }

    /**
     * Check amount format (/^(([1-9]{1}\d*)|([0]{1}))(\.(\d){0,2})?$/)
     *
     * @param fromAmount
     * @param errors
     * @param fromAmount2
     * @param msg
     */
    private void checkAmount(String fromAmount, Errors errors, String fromAmount2, String msg) {
        Matcher match = pattern.matcher(fromAmount);
        if (match.matches() == false) {
            errors.rejectValue(fromAmount2, msg);
        }
    }
}
