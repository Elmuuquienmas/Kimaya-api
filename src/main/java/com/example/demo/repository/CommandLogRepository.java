package com.example.demo.repository;

import com.example.demo.model.CommandLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandLogRepository extends JpaRepository<CommandLog, Long> {
}
