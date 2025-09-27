package com.webechannelingsystem.webechannelingsystem.service;

import com.webechannelingsystem.webechannelingsystem.model.Doctor;
import com.webechannelingsystem.webechannelingsystem.model.Emergency;
import com.webechannelingsystem.webechannelingsystem.repository.DoctorRepository;
import com.webechannelingsystem.webechannelingsystem.repository.EmergencyRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmergencyRegisterService {

    private final EmergencyRegisterRepository emergencyRegisterRepository;

    @Autowired
    public EmergencyRegisterService(EmergencyRegisterRepository emergencyRegisterRepository) {
        this.emergencyRegisterRepository = emergencyRegisterRepository;
    }

    public Emergency save(Emergency emergency){
        return emergencyRegisterRepository.save(emergency);
    }
}


