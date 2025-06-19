package com.telia.Lease_management.dto.requests;

import java.time.Instant;

import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.contract.Invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {
    
    private Long id;
    private Long contractId;
    private Double amount;
    private Instant dueDate;
    private String description;
    private String invoiceReference;
    private String startInterval;
    private String endInterval;
    // private RentalStatus status;  

    
    public static InvoiceDTO convertToDto(Invoice invoice) {
        if (invoice == null) {
            return null;
        }

        InvoiceDTO invoiceDto = new InvoiceDTO();
        invoiceDto.setId(invoice.getId());
        invoiceDto.setContractId(invoice.getContract().getId());
        invoiceDto.setAmount(invoice.getAmount());
        invoiceDto.setDueDate(invoice.getDueDate());
        invoiceDto.setDescription(invoice.getDescription());
        invoiceDto.setInvoiceReference(invoice.getInvoiceReference());
        invoiceDto.setStartInterval(invoice.getStartInterval());
        invoiceDto.setEndInterval(invoice.getEndInterval());
        // invoiceDto.setStatus(invoice.getStatus());

        return invoiceDto;
    }
}
