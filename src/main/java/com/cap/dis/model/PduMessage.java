package com.cap.dis.model;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class PduMessage {
    private String type;
    private EntityId entityId;
    private Location location;

    @Column(columnDefinition = "BIGINT")
    private long timestamp;

    // Fields for FirePdu
    private EntityId firingEntityId;
    private EntityId targetEntityId;
    private EntityId munitionId;
    
    // Fields for CollisionPdu
    private EntityId issuingEntityId;
    private EntityId collidingEntityId;
    
    // Fields for DataPdu
    private EntityId originatingEntityId;
    private EntityId receivingEntityId;
    
    // Fields for DesignatorPdu
    private EntityId designatingEntityId;
    private EntityId designatedEntityId;
    
    // Fields for ElectronicEmissionsPdu
    private EntityId emittingEntityId;
    
    // Fields for StartResumePdu
    private RealWorldTime realWorldTime;
}