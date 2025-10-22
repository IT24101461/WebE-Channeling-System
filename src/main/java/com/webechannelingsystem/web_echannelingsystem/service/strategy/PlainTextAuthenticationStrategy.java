package com.webechannelingsystem.web_echannelingsystem.service.strategy;

import com.webechannelingsystem.web_echannelingsystem.model.Admin;
import org.springframework.stereotype.Component;

@Component
public class PlainTextAuthenticationStrategy implements AuthenticationStrategy{
    @Override
    public boolean authenticate(Admin admin, String password) {
        return admin.getPassword().equals(password);
    }
}
