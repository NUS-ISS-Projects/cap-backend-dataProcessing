package com.cap.dis.model;

import lombok.Data;

@Data
public class PduMessage {
    private String type;
    private EntityID entityId;
    private Location location;
    private long timestamp;
}
