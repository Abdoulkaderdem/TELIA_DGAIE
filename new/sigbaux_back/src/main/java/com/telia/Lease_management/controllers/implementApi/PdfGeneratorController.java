package com.telia.Lease_management.controllers.implementApi;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.telia.Lease_management.controllers.interfaceApi.PdfGeneratorApi;
import com.telia.Lease_management.services.PdfGeneratorService;

import java.util.List;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@AllArgsConstructor
@RestController
@Slf4j
public class PdfGeneratorController implements PdfGeneratorApi{
    
    private PdfGeneratorService pdfGeneratorService;

    @Override
    public ResponseEntity<byte[]> printRentalRequest(Long id) {
         try {
            byte[] pdfContents = pdfGeneratorService.generateRentalRequestPdf(id);
            HttpHeaders headers = new HttpHeaders();
            //Create a code to send
            String code= this.generateRandomCode();
                        
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Besoin_Location_" + code + ".pdf");
            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
            
        } catch (IOException e) {
            log.error("IOException: {} ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception: {} ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }


    @Override
    public ResponseEntity<byte[]> generateContractPdf(Long idContract) {
        try {
            byte[] pdfContents = pdfGeneratorService.generateContractPdf(idContract);
            HttpHeaders headers = new HttpHeaders();
            //Create a code to send
            String code= this.generateRandomCode();
                        
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Contrat_Location_" + code + ".pdf");
            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
            
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    @Override
    public ResponseEntity<byte[]> generateContractTerminationNoticePdf(Long idContract, List<String> ampliations) {
        try {
            if (ampliations==null){
                ampliations= new ArrayList<>();               
            }

            byte[] pdfContents = pdfGeneratorService.generateContractTerminationNoticePdf(idContract, ampliations);
            HttpHeaders headers = new HttpHeaders();
            //Create a code to send
            String code= this.generateRandomCode();
                        
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Notice_Resiliation_" + code + ".pdf");
            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
            
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

        
    @Override
    public ResponseEntity<byte[]> generateContractTerminationNoticePdf(Long idContract) {
        try {
            List<String> ampliations = new ArrayList<>();

            byte[] pdfContents = pdfGeneratorService.generateContractTerminationNoticePdf(idContract, ampliations);
            HttpHeaders headers = new HttpHeaders();
            //Create a code to send
            String code= this.generateRandomCode();
                        
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Notice_Resiliation_" + code + ".pdf");
            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
            
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<byte[]> generateContractTerminationIrePdf(Long idContract) {
        try {

            byte[] pdfContents = pdfGeneratorService.generateContractTerminationIrePdf(idContract);
            HttpHeaders headers = new HttpHeaders();
            //Create a code to send
            String code= this.generateRandomCode();
                        
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Notice_Resiliation_" + code + ".pdf");
            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
            
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public String generateRandomCode() {
        return RandomStringUtils.randomAlphanumeric(8);
    }





}
