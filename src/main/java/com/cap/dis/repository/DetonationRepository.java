package com.cap.dis.repository;

import com.cap.dis.model.DetonationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetonationRepository extends JpaRepository<DetonationRecord, Long> {
}