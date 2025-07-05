package com.cap.dis.repository;

import com.cap.dis.model.SetDataPduRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SetDataPduRepository extends JpaRepository<SetDataPduRecord, Long> {
    List<SetDataPduRecord> findByTimestampBetween(Long startTime, Long endTime);
}