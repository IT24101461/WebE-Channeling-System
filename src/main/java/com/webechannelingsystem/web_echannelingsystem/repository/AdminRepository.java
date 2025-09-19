package com.webechannelingsystem.web_echannelingsystem.repository;

import com.webechannelingsystem.web_echannelingsystem.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
}
