package com.cap.dis.repository;

import com.cap.dis.model.DesignatorPduRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DesignatorPduRepository extends JpaRepository<DesignatorPduRecord, Long> {
    List<DesignatorPduRecord> findByTimestampBetween(Long startTime, Long endTime);
}