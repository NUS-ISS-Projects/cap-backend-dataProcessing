package com.cap.dis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class SetDataPduRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int originatingSite;
    private int originatingApplication;
    private int originatingEntity;

    @Column(columnDefinition = "BIGINT")
    private long timestamp;
}