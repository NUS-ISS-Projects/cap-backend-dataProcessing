package com.cap.dis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultPduParser implements PduParser {
    private static final Logger log = LoggerFactory.getLogger(DefaultPduParser.class);

    @Override
    public void parseAndStore(String jsonMessage) {
        log.warn("Received unhandled PDU type: {}", jsonMessage);
    }
}