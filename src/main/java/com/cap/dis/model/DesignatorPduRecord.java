package com.cap.dis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class DesignatorPduRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int designatingSite;
    private int designatingApplication;
    private int designatingEntity;
    
    private int designatedSite;
    private int designatedApplication;
    private int designatedEntity;

    @Column(columnDefinition = "BIGINT")
    private long timestamp;
}