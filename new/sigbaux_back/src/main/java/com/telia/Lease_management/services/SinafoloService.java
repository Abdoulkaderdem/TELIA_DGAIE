package com.telia.Lease_management.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telia.Lease_management.dto.sinafoloDto.InvoiceNafoloDto;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SinafoloService {

    @Autowired
    private JmsTemplate jmsTemplate; //InvoiceDto

    public void sendInvoiceToSinafolo(InvoiceNafoloDto invoiceDto) {

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("type", "FACTURE");
        
        // Transformez le DTO de la facture en JSON
        String message = convertFactureToJson(invoiceDto);
        messageMap.put("message", message);

        // jmsTemplate.convertAndSend("factureDeposer", messageMap);
        try {
            jmsTemplate.convertAndSend("factureDeposer", messageMap);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi du message JMS : ", e);
            throw new RuntimeException("Erreur d'envoi de la facture à SIN@folo", e);
        }

    }


    private String convertFactureToJson(InvoiceNafoloDto factureDto) {
        // Utiliser une bibliothèque comme Jackson pour convertir le DTO en JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(factureDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors de la conversion de la facture en JSON", e);
        }
    }

}
