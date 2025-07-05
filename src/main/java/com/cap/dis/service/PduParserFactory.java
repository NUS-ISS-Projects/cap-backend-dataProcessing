package com.cap.dis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PduParserFactory {
    private static final Logger log = LoggerFactory.getLogger(PduParserFactory.class);
    private Map<String, PduParser> parsers;

    @Autowired
    private EntityStatePduParser entityStatePduParser;
    @Autowired
    private FirePduParser firePduParser;
    @Autowired
    private DetonationPduParser detonationPduParser;
    @Autowired
    private CollisionPduParser collisionPduParser;
    @Autowired
    private DataPduParser dataPduParser;
    @Autowired
    private ActionRequestPduParser actionRequestPduParser;
    @Autowired
    private StartResumePduParser startResumePduParser;
    @Autowired
    private SetDataPduParser setDataPduParser;
    @Autowired
    private DesignatorPduParser designatorPduParser;
    @Autowired
    private ElectromagneticEmissionsPduParser electromagneticEmissionsPduParser;
    @Autowired
    private DefaultPduParser defaultPduParser;

    @PostConstruct
    public void init() {
        parsers = new HashMap<>();
        parsers.put("EntityStatePdu", entityStatePduParser);
        parsers.put("FirePdu", firePduParser);
        parsers.put("DetonationPdu", detonationPduParser);
        parsers.put("CollisionPdu", collisionPduParser);
        parsers.put("DataPdu", dataPduParser);
        parsers.put("ActionRequestPdu", actionRequestPduParser);
        parsers.put("StartResumePdu", startResumePduParser);
        parsers.put("SetDataPdu", setDataPduParser);
        parsers.put("DesignatorPdu", designatorPduParser);
        parsers.put("ElectronicEmissionsPdu", electromagneticEmissionsPduParser);
        log.info("Initialized PduParserFactory with parsers: {}", parsers.keySet());
    }

    public PduParser getParser(String pduType) {
        PduParser parser = parsers.getOrDefault(pduType, defaultPduParser);
        log.debug("Selected parser for type '{}': {}", pduType, parser.getClass().getSimpleName());
        return parser;
    }
}