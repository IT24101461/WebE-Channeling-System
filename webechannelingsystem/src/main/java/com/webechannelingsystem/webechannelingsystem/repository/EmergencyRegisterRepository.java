package com.webechannelingsystem.webechannelingsystem.repository;

import com.webechannelingsystem.webechannelingsystem.model.Emergency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyRegisterRepository extends JpaRepository<Emergency, Long> {

}
