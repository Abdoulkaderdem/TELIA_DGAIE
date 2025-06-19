package com.telia.Lease_management.controllers.interfaceApi;

import java.io.IOException;
import java.util.List;

import com.telia.Lease_management.dto.sinafoloDto.InvoiceNafoloDto;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.telia.Lease_management.dto.requests.InvoiceDTO;
import com.telia.Lease_management.dto.responses.ContractResponse;
import com.telia.Lease_management.dto.responses.InvoiceResponse;

@CrossOrigin
@RequestMapping("/invoice")
//@PreAuthorize("hasAnyAuthority('ROLE_DAIE', 'ROLE_SUPER')")
public interface InvoiceApi {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/create",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvoiceResponse> createInvoice(@ModelAttribute InvoiceDTO invoiceDto, @RequestParam(value = "file", required = true) MultipartFile file);

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable Long id);
    
    @GetMapping("/{invoiceId}/pdf")
    public ResponseEntity<byte[]> getInvoicePdf(@PathVariable Long invoiceId);
    
    @GetMapping("/contract/{contractId}/listpdf")
    public ResponseEntity<byte[]>getInvoicesPdfByContractId(@PathVariable Long contractId);

    @GetMapping("/contract/{contractId}")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByContractId(@PathVariable Long contractId);

    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvoiceResponse> updateInvoice(@PathVariable Long id, @RequestBody InvoiceDTO invoiceDto);

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices();

    @GetMapping("/{idContract}/to-excel")
    public ResponseEntity<InputStreamResource> downloadInvoiceExcel(@PathVariable Long idContract) throws IOException;
    
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(value = "/add/{idInvoice}/file",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvoiceResponse> addFileToInvoice(@PathVariable Long idInvoice, @RequestParam(value = "file", required = true) MultipartFile file);


    // @PostMapping("/send/sinafolo")
    // public ResponseEntity<String> sendInvoice(@RequestBody InvoiceDto invoiceDto);

    @PostMapping("/{invoiceId}/send")
    public String sendInvoice(@PathVariable Long invoiceId);
    
}
