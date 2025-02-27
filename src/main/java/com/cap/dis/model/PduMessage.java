package com.cap.dis.model;

import lombok.Data;

@Data
public class PduMessage {
    private String type;
    private EntityId entityId;
    private Location location;
    private long timestamp;

    // Fields for FirePdu
    private EntityId firingEntityId;
    private EntityId targetEntityId;
    private EntityId munitionId;
}