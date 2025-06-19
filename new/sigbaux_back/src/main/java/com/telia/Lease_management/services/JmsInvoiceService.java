package com.telia.Lease_management.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telia.Lease_management.dto.requests.InvoiceDTO;
import com.telia.Lease_management.dto.responses.InvoiceResponse;
import com.telia.Lease_management.dto.sinafoloDto.InvoiceNafoloDto;
import com.telia.Lease_management.entity.contract.Contract;
import com.telia.Lease_management.entity.contract.Invoice;
import com.telia.Lease_management.repository.contract.InvoiceRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class JmsInvoiceService {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;
    private InvoiceRepository invoiceRepository;


    public void sendInvoiceMessage(InvoiceNafoloDto invoiceToSend) {
        log.info("********* ENTER IN jmsInvoiceService.......... ");

        try {

            // Sérialisation en JSON
            String messageJson = objectMapper.writeValueAsString(invoiceToSend);

            // Préparation du message à envoyer
            String finalMessage = objectMapper.writeValueAsString(Map.of("type", "FACTURE", "message", messageJson));

            // Envoi du message
            jmsTemplate.convertAndSend("queuefacturebail", finalMessage);
            System.out.println("Facture envoyée avec succès : " + finalMessage);
            log.info("********* SENDING END jmsInvoiceService.......... {}", finalMessage);
            log.info("********* SENDING END jmsInvoiceService + INVOICE .......... {}", invoiceToSend);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendInvoiceById(Long invoiceId) {
        log.info("********* SENDING TO sendInvoiceById.......... ");

        Invoice existingInvoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice-V3 not found"));
        

        InvoiceNafoloDto invoiceNafoloDto = new InvoiceNafoloDto();
        invoiceNafoloDto.setReferenceContrat(existingInvoice.getInvoiceReference());
        invoiceNafoloDto.setDateFacture(existingInvoice.getDateCreated());
        invoiceNafoloDto.setMontantFacture(existingInvoice.getAmount());
        invoiceNafoloDto.setReferenceContrat(existingInvoice.getContract().getLeaseContractNumber());
        invoiceNafoloDto.setCodeTiers(existingInvoice.getEndInterval());

        try {

            // Sérialisation en JSON
            String messageJson = objectMapper.writeValueAsString(invoiceNafoloDto);

            // Préparation du message à envoyer
            String finalMessage = objectMapper.writeValueAsString(Map.of("type", "FACTURE", "message", messageJson));

            // Envoi du message
            jmsTemplate.convertAndSend("queuefacturebail", finalMessage);
            System.out.println("Facture envoyée avec succès : " + finalMessage);
            log.info("********* SENDING END jmsInvoiceService.......... {}", finalMessage);
            log.info("********* SENDING END jmsInvoiceService + INVOICE .......... {}", invoiceNafoloDto);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}