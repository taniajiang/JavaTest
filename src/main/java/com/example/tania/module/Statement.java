package com.example.tania.module;

import org.apache.commons.lang.StringUtils;

public class Statement {
    private String id;
    private String accountId;
    private String dateField;
    private String amount;
    private String accountType;
    private String accountNumber;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getAccountId() {
        return accountId;
    }
    
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    
    public String getDateField() {
        return dateField;
    }
    
    public void setDateField(String dateField) {
        this.dateField = dateField;
    }
    
    public String getAmount() {
        return amount;
    }
    
    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        // The account number should be hashed before sent to the user.
        if(StringUtils.isNotBlank(accountNumber)){
            return String.valueOf(accountNumber.hashCode());
        }
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
