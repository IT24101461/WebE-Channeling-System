package com.webechannelingsystem.web_echannelingsystem.strategy;


public interface PasswordValidationStrategy {


    boolean validate(String password);


    String getErrorMessage();
}