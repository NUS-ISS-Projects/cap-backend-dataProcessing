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
}