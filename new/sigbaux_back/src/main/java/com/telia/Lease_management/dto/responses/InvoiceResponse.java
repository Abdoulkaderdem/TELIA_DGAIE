package com.telia.Lease_management.dto.responses;

import java.time.Instant;

import com.telia.Lease_management.dto.requests.InvoiceDTO;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.contract.Invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponse {
        
    private Long id;
    private Long contractId;
    private Double amount;
    private Instant dueDate;
    private String description;
    private String invoiceReference;
    private RentalStatus status;
    private String startInterval;
    private String endInterval;
    private String filePath;
     
    public static InvoiceResponse convertToDto(Invoice invoice) {
        if (invoice == null) {
            return null;
        }

        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setId(invoice.getId());
        invoiceResponse.setContractId(invoice.getContract().getId());
        invoiceResponse.setAmount(invoice.getAmount());
        invoiceResponse.setDueDate(invoice.getDueDate());
        invoiceResponse.setDescription(invoice.getDescription());
        invoiceResponse.setInvoiceReference(invoice.getInvoiceReference());
        invoiceResponse.setStatus(invoice.getStatus());
        invoiceResponse.setStartInterval(invoice.getStartInterval());
        invoiceResponse.setEndInterval(invoice.getEndInterval());
        invoiceResponse.setFilePath(invoice.getFilePath());

        return invoiceResponse;
    }
}
