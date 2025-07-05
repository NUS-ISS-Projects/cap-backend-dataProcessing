package com.cap.dis.repository;

import com.cap.dis.model.ActionRequestPduRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionRequestPduRepository extends JpaRepository<ActionRequestPduRecord, Long> {
    List<ActionRequestPduRecord> findByTimestampBetween(Long startTime, Long endTime);
}