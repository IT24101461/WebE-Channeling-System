package com.webechannelingsystem.webechannelingsystem.service;

import com.webechannelingsystem.webechannelingsystem.model.Doctor;
import com.webechannelingsystem.webechannelingsystem.model.Emergency;
import com.webechannelingsystem.webechannelingsystem.repository.DoctorRepository;
import com.webechannelingsystem.webechannelingsystem.repository.EmergencyRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<Emergency> getAllCases() {
        return emergencyRegisterRepository.findAll();
    }

    public void deleteCase(Long id) {
        emergencyRegisterRepository.deleteById(id);
    }

    public void confirmCase(Long id) {
        Optional<Emergency> emergencyOptional = emergencyRegisterRepository.findById(id);
        Emergency emergency = emergencyOptional.get();
        emergency.setStatus("CONFIRMED");
        emergencyRegisterRepository.save(emergency);
    }
}


