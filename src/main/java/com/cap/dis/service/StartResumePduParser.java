package com.cap.dis.service;

import com.cap.dis.model.StartResumePduRecord;
import com.cap.dis.model.PduMessage;
import com.cap.dis.repository.StartResumePduRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StartResumePduParser implements PduParser {
    private static final Logger log = LoggerFactory.getLogger(StartResumePduParser.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StartResumePduRepository repository;

    @Override
    @Transactional
    public void parseAndStore(String jsonMessage) {
        try {
            PduMessage pduMessage = objectMapper.readValue(jsonMessage, PduMessage.class);
            if (pduMessage.getRealWorldTime() == null) {
                log.error("Missing required fields in StartResumePdu: {}", jsonMessage);
                return;
            }
            StartResumePduRecord record = new StartResumePduRecord();
            record.setHour(pduMessage.getRealWorldTime().getHour());
            record.setTimePastHour(pduMessage.getRealWorldTime().getTimePastHour());
            record.setTimestamp(pduMessage.getTimestamp());
            log.info("Attempting to store StartResumePduRecord with timestamp: {}", record.getTimestamp());
            repository.save(record);
            log.info("Stored StartResumePduRecord: {}", record);
        } catch (Exception e) {
            log.error("Failed to parse and store StartResumePdu: {}", jsonMessage, e);
        }
    }
}