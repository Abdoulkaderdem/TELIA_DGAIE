package com.telia.Lease_management.controllers.implementApi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import com.telia.Lease_management.dto.sinafoloDto.InvoiceNafoloDto;
import com.telia.Lease_management.services.SinafoloService;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.telia.Lease_management.controllers.interfaceApi.InvoiceApi;
import com.telia.Lease_management.dto.requests.InvoiceDTO;
import com.telia.Lease_management.dto.responses.ContractResponse;
import com.telia.Lease_management.dto.responses.ContractRevisionResponse;
import com.telia.Lease_management.dto.responses.InvoiceResponse;
import com.telia.Lease_management.entity.contract.Invoice;
import com.telia.Lease_management.services.JmsInvoiceService;
import com.telia.Lease_management.services.PdfGeneratorService;
import com.telia.Lease_management.services.contract.InvoiceService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class InvoiceController implements InvoiceApi{

    private final InvoiceService invoiceService;
    private PdfGeneratorService pdfGeneratorService;
    private final JmsInvoiceService jmsInvoiceService;

    @Override
    public ResponseEntity<InvoiceResponse> createInvoice(InvoiceDTO invoiceDto, MultipartFile file) {
         try {
            // Chack if file is a PDF
            if (!isPdfFile(file)) {
                throw new InvalidOperationException("Le fichier doit être au format PDF.");
            }

            InvoiceResponse createdInvoice = invoiceService.createInvoice(invoiceDto, file);
            return ResponseEntity.ok(createdInvoice);
        } catch (IllegalArgumentException e) {
            log.info("Une erreur est survenue: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }catch (Exception e) {
            log.info("Une erreur est survenue : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @Override
    public ResponseEntity<InvoiceResponse> getInvoiceById(Long id) {
        InvoiceResponse invoiceDto = invoiceService.getInvoiceById(id);
        if (invoiceDto != null) {
            return ResponseEntity.ok(invoiceDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByContractId(Long contractId) {
        List<InvoiceResponse> invoices = invoiceService.getInvoicesByContractId(contractId);
        return ResponseEntity.ok(invoices);
    }

    @Override
    public ResponseEntity<InvoiceResponse> updateInvoice(Long id, InvoiceDTO invoiceDto) {
        InvoiceResponse updatedInvoice = invoiceService.updateInvoice(id, invoiceDto);
        if (updatedInvoice != null) {
            return ResponseEntity.ok(updatedInvoice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        List<InvoiceResponse> listInvoices = invoiceService.getAllInvoices();
        return new ResponseEntity<>(listInvoices, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadInvoiceExcel(Long idContract) throws IOException {
        Invoice invoice = invoiceService.generateInvoice(idContract);

        byte[] excelContent = pdfGeneratorService.generateInvoiceExcel(invoice);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(excelContent);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=invoice_" + invoice.getInvoiceReference() + ".xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(inputStream));
    }

    @Override
    public ResponseEntity<InvoiceResponse> addFileToInvoice(Long idInvoice, MultipartFile file) {
        try {
            InvoiceResponse updateInvoice = invoiceService.addFileToInvoice(idInvoice, file);
            return ResponseEntity.ok(updateInvoice);
        } catch (IllegalArgumentException e) {
            log.info("Une erreur est survenue: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }catch (Exception e) {
            log.info("Une erreur est survenue : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // @Override
    // public ResponseEntity<String> sendInvoice(InvoiceDto invoiceDto) {
    //     sinafoloService.sendInvoice(
    //             invoiceDto.getReferenceFacture(),
    //             invoiceDto.getDateFacture(),
    //             invoiceDto.getMontantFacture(),
    //             invoiceDto.getReferenceContrat(),
    //             invoiceDto.getCodeTiers()
    //     );
    //     return ResponseEntity.ok("Invoice sent successfully");
    // }

    @Override
     public ResponseEntity<byte[]> getInvoicePdf(@PathVariable Long invoiceId){
        return invoiceService.getInvoicePdf(invoiceId);
     }

     @Override
    public ResponseEntity<byte[]> getInvoicesPdfByContractId(@PathVariable Long contractId){
        return invoiceService.getInvoicesPdfByContractId(contractId);

    }
    
    //********************************************************************************** */

    private boolean isPdfFile(MultipartFile file) {
        // Verify file extension
        String filename = file.getOriginalFilename();
        return filename != null && filename.endsWith(".pdf");
    }

    @Override
    public String sendInvoice(Long invoiceId) {
        
        // Envoyer la facture via JMS
        jmsInvoiceService.sendInvoiceById(invoiceId);
        log.info("*************** Controller - Facture envoyée avec succès ***************");
        return "Facture envoyée avec succès";
    }



    
    
}
