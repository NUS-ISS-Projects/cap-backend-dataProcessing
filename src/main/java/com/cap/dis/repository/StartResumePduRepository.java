package com.cap.dis.repository;

import com.cap.dis.model.StartResumePduRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StartResumePduRepository extends JpaRepository<StartResumePduRecord, Long> {
    List<StartResumePduRecord> findByTimestampBetween(Long startTime, Long endTime);
}