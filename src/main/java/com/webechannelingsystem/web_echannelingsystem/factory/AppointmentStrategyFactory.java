package com.webechannelingsystem.web_echannelingsystem.factory;

import com.webechannelingsystem.web_echannelingsystem.strategy.AppointmentBookingStrategy;
import com.webechannelingsystem.web_echannelingsystem.strategy.RegularAppointmentStrategy;
import com.webechannelingsystem.web_echannelingsystem.strategy.EmergencyAppointmentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AppointmentStrategyFactory {

    private final Map<String, AppointmentBookingStrategy> strategies;

    @Autowired
    public AppointmentStrategyFactory(RegularAppointmentStrategy regularStrategy,
                                      EmergencyAppointmentStrategy emergencyStrategy) {
        strategies = new HashMap<>();
        strategies.put("REGULAR", regularStrategy);
        strategies.put("EMERGENCY", emergencyStrategy);
    }

    public AppointmentBookingStrategy getStrategy(String appointmentType) {
        AppointmentBookingStrategy strategy = strategies.get(appointmentType.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown appointment type: " + appointmentType);
        }
        return strategy;
    }
}