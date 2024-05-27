package com.example.task.repository;

import com.example.task.domain.Info;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfoRepository extends JpaRepository<Info, Long> {
    Info findByJsonFileName(String jsonFileName);
}
