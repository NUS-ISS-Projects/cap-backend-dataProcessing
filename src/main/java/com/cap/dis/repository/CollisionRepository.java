package com.cap.dis.repository;

import com.cap.dis.model.CollisionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollisionRepository extends JpaRepository<CollisionRecord, Long> {
}