package com.webechannelingsystem.web_echannelingsystem.service.strategy;

import com.webechannelingsystem.web_echannelingsystem.model.Admin;

public interface AuthenticationStrategy {
    boolean authenticate(Admin admin, String password);
}
