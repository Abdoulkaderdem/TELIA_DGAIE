package com.telia.Lease_management.controllers.interfaceApi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import com.telia.Lease_management.dto.requests.ContractDto;

import net.sf.jasperreports.engine.JRException;

@RequestMapping("/print")
@CrossOrigin
public interface PdfGeneratorApi {
        
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/{id}/rental-requests") 
    public ResponseEntity<byte[]> printRentalRequest(@PathVariable Long id);

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/{idContract}/generate-contract")
    public ResponseEntity<byte[]> generateContractPdf(@PathVariable Long idContract);
    
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/contract/{idContract}/generate-termination-notice")
    public ResponseEntity<byte[]> generateContractTerminationNoticePdf(@PathVariable Long idContract, @RequestBody List<String> ampliations);

    
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/contract/{idContract}/generate-termination-notice")
    public ResponseEntity<byte[]> generateContractTerminationNoticePdf(@PathVariable Long idContract);
    
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/contract/{idContract}/generate-ire")
    public ResponseEntity<byte[]> generateContractTerminationIrePdf(@PathVariable Long idContract);

}
