package com.cap.dis.repository;

import com.cap.dis.model.FireEventRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FireEventRepository extends JpaRepository<FireEventRecord, Long> {
}