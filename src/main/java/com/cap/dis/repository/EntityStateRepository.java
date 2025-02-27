package com.cap.dis.repository;

import com.cap.dis.model.EntityStateRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityStateRepository extends JpaRepository<EntityStateRecord, Long> {
}