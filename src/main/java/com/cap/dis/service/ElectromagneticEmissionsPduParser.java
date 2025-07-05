package com.cap.dis.service;

import com.cap.dis.model.ElectromagneticEmissionsPduRecord;
import com.cap.dis.model.PduMessage;
import com.cap.dis.repository.ElectromagneticEmissionsPduRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ElectromagneticEmissionsPduParser implements PduParser {
    private static final Logger log = LoggerFactory.getLogger(ElectromagneticEmissionsPduParser.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ElectromagneticEmissionsPduRepository repository;

    @Override
    @Transactional
    public void parseAndStore(String jsonMessage) {
        try {
            PduMessage pduMessage = objectMapper.readValue(jsonMessage, PduMessage.class);
            if (pduMessage.getEmittingEntityId() == null) {
                log.error("Missing required fields in ElectromagneticEmissionsPdu: {}", jsonMessage);
                return;
            }
            ElectromagneticEmissionsPduRecord record = new ElectromagneticEmissionsPduRecord();
            record.setEmittingSite(pduMessage.getEmittingEntityId().getSite());
            record.setEmittingApplication(pduMessage.getEmittingEntityId().getApplication());
            record.setEmittingEntity(pduMessage.getEmittingEntityId().getEntity());
            record.setTimestamp(pduMessage.getTimestamp());
            log.info("Attempting to store ElectromagneticEmissionsPduRecord with timestamp: {}", record.getTimestamp());
            repository.save(record);
            log.info("Stored ElectromagneticEmissionsPduRecord: {}", record);
        } catch (Exception e) {
            log.error("Failed to parse and store ElectromagneticEmissionsPdu: {}", jsonMessage, e);
        }
    }
}