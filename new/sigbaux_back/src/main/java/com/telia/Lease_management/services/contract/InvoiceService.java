package com.telia.Lease_management.services.contract;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.telia.Lease_management.dto.requests.InvoiceDTO;
import com.telia.Lease_management.dto.responses.InvoiceResponse;
import com.telia.Lease_management.dto.sinafoloDto.InvoiceNafoloDto;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.contract.Contract;
import com.telia.Lease_management.entity.contract.Invoice;
import com.telia.Lease_management.repository.contract.ContractRepository;
import com.telia.Lease_management.repository.contract.InvoiceRepository;
import com.telia.Lease_management.services.JmsInvoiceService;
import com.telia.Lease_management.services.SinafoloService;
import com.telia.Lease_management.utils.FilesValidator;
import com.telia.Lease_management.utils.ValidateForms;

import ch.qos.logback.core.spi.ErrorCodes;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class InvoiceService {

    private InvoiceRepository invoiceRepository;
    private ContractRepository contractRepository;
    private SinafoloService sinafoloService;
    private JmsInvoiceService jmsInvoiceService;

    private static final Map<Integer, String> QUARTER_PERIODS = new HashMap<>();
    
    static {
        QUARTER_PERIODS.put(1, "Janvier - Mars");
        QUARTER_PERIODS.put(2, "Avril - Juin");
        QUARTER_PERIODS.put(3, "Juillet - Septembre");
        QUARTER_PERIODS.put(4, "Octobre - Décembre");
    }

    // private final static Path uploadPath = Paths.get("uploadsInvoices");
    private final static Path uploadPath = Paths.get("/opt/leaseManagement/uploadsInvoices/"); 

    
    public InvoiceResponse createInvoice(InvoiceDTO invoiceDTO, MultipartFile file) throws IOException {
        //Check if the contract form is valid
        List<String> errors= ValidateForms.validateInvoice(invoiceDTO);
        if (!errors.isEmpty()){
            log.error("Invoice is not valid {}:", invoiceDTO);
            log.error("Liste des erreurs {}: ", errors);
            throw new IllegalArgumentException("Invoice is not valid !");
        }

        Contract existingContract = contractRepository.findById(invoiceDTO.getContractId())
                .orElseThrow(() -> new EntityNotFoundException("Contract not found"));
               
          //Checking status, if contract is current
        if (existingContract.getStatus() == RentalStatus.DISABLED || existingContract.isTerminated()){
            throw new IllegalArgumentException("Cannot revise a terminated or disabled contract! ");
        }


        // Check if there is an existing invoice with the same interval for the contract
        List<Invoice> existingInvoices = invoiceRepository.findByContractAndStartIntervalAndEndInterval(
            existingContract, invoiceDTO.getStartInterval(), invoiceDTO.getEndInterval());

        if (!existingInvoices.isEmpty()) {
            throw new IllegalArgumentException("An invoice with the same interval already exists for this contract!");
        }

        boolean checkFile= false;
        //Check if file is uploaded
        if (file != null && !file.isEmpty()){
            checkFile= true;       
            createUploadDirIfNotExists();
        }

        Invoice invoiceToSave = new Invoice();
        // Mettez à jour les propriétés de l'entité avec les valeurs du DTO
        invoiceToSave.setContract(existingContract);
        invoiceToSave.setAmount(invoiceDTO.getAmount());
        invoiceToSave.setDueDate(invoiceDTO.getDueDate());
        invoiceToSave.setStartInterval(invoiceDTO.getStartInterval());
        invoiceToSave.setEndInterval(invoiceDTO.getEndInterval());
        invoiceToSave.setDescription(invoiceDTO.getDescription());
        invoiceToSave.setInvoiceReference(invoiceDTO.getInvoiceReference());
        if (checkFile){
            invoiceToSave.setStatus(RentalStatus.ENABLE);
        }

        Invoice savedInvoice = invoiceRepository.save(invoiceToSave); //saving

        saveDocuments(file, savedInvoice);

        log.info("********* SENDING TO jmsInvoiceService.......... ");
        InvoiceNafoloDto invoiceNafoloDto = new InvoiceNafoloDto();
        invoiceNafoloDto.setReferenceContrat(savedInvoice.getInvoiceReference());
        invoiceNafoloDto.setDateFacture(savedInvoice.getDateCreated());
        invoiceNafoloDto.setMontantFacture(savedInvoice.getAmount());
        invoiceNafoloDto.setReferenceContrat(savedInvoice.getContract().getLeaseContractNumber());
        invoiceNafoloDto.setCodeTiers(savedInvoice.getEndInterval());

        jmsInvoiceService.sendInvoiceMessage(invoiceNafoloDto);

        

        System.out.println( "********* SENDING END TO jmsInvoiceService.......... ");

        //Call Nafolo API
        // InvoiceNafoloDto invoiceNafoloDto = new InvoiceNafoloDto();
        // invoiceNafoloDto.setReferenceContrat(savedInvoice.getInvoiceReference());
        // invoiceNafoloDto.setDateFacture(savedInvoice.getDateCreated());
        // invoiceNafoloDto.setMontantFacture(savedInvoice.getAmount());
        // invoiceNafoloDto.setReferenceContrat(savedInvoice.getContract().getLeaseContractNumber());
        // invoiceNafoloDto.setCodeTiers(savedInvoice.getEndInterval());
        
        // sinafoloService.sendInvoiceToSinafolo(invoiceNafoloDto);

        return InvoiceResponse.convertToDto(savedInvoice);
    }

    
    private String saveDocuments(MultipartFile file, Invoice invoiceSaved) {
        // Generate a unique name for the file
        String originalFilename = file.getOriginalFilename();
        String uniqueFilename = UUID.randomUUID().toString() + "-" + originalFilename;
        Path destinationFile = uploadPath.resolve(uniqueFilename);

        try {
            // Save the file to disk
            Files.copy(file.getInputStream(), destinationFile);

            // Update the invoice with the file path
            invoiceSaved.setFilePath("/uploadsInvoices/" + uniqueFilename); 
            invoiceSaved.setStatus(RentalStatus.ENABLE);
            invoiceRepository.save(invoiceSaved); //saving

            return "/uploadsInvoices/" + uniqueFilename;

        } catch (IOException e) {
            throw new InvalidOperationException("Failed to save file: " + e.getMessage(), e);
        }
    }



    public InvoiceResponse updateInvoice(Long id, InvoiceDTO invoiceDTO) {
        Optional<Invoice> existingInvoiceOpt = invoiceRepository.findById(id);

        if (existingInvoiceOpt.isPresent()) {
            Invoice existingInvoice = existingInvoiceOpt.get();
            // Mettre à jour les propriétés de l'entité
            existingInvoice.setAmount(invoiceDTO.getAmount());
            existingInvoice.setDueDate(invoiceDTO.getDueDate());
            existingInvoice.setDescription(invoiceDTO.getDescription());
            existingInvoice.setInvoiceReference(invoiceDTO.getInvoiceReference());

            Invoice updatedInvoice = invoiceRepository.save(existingInvoice);

            return InvoiceResponse.convertToDto(updatedInvoice);
        }

        throw new IllegalArgumentException("Invoice with id: " + id + " not found");
    }


    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }

    
    public InvoiceResponse getInvoiceById(Long id) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(id);

        if (invoiceOpt.isPresent()) {
            Invoice invoice = invoiceOpt.get();
            return InvoiceResponse.convertToDto(invoice);
        }
        throw new IllegalArgumentException("Invoice with id: " + id + " not found");
    }

    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll().stream().map(invoice -> {
            return InvoiceResponse.convertToDto(invoice);
        }).collect(Collectors.toList());
    }


    public List<InvoiceResponse> getInvoicesByContractId(Long contractId) {
        Contract existingContract = contractRepository.findById(contractId)
            .orElseThrow(() -> new EntityNotFoundException("Contract not found"));

        List<Invoice> invoices = invoiceRepository.findByContract(existingContract);
        return invoices.stream()
                .map(InvoiceResponse::convertToDto)
                .collect(Collectors.toList());
    }

    
    
    public Invoice generateInvoice(Long idContract) throws IOException {
        //Check if the contract exits
        Contract existingContract = contractRepository.findById(idContract)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found! "));
               
          //Checking the status
        if (existingContract.getStatus() == RentalStatus.DISABLED || existingContract.isTerminated()){
            throw new IllegalArgumentException("Cannot generate invoice on a 'terminated' or 'disabled' contract! ");
        }
        
        //Calcul amount to pay
        Double amountToPay= (double) (existingContract.getRentAmount()*3);
        //Retreive existing invoices
        List<Invoice> listInvoices = invoiceRepository.findByContract(existingContract);
        //Get the quater to pay
        String getQuaterToPay = getNextQuarterToPay(existingContract, listInvoices);

        String generateInvoicereference = generateInvoicetNumber(getQuaterToPay);

        //Create and save the invoice
        Invoice invoiceToSave = new Invoice();
        invoiceToSave.setContract(existingContract);
        invoiceToSave.setQuaterPayment(getQuaterToPay);
        invoiceToSave.setAmount(amountToPay);
        invoiceToSave.setDueDate(Instant.now());
        invoiceToSave.setDescription(existingContract.getLeaseContractNumber());
        invoiceToSave.setInvoiceReference(generateInvoicereference);
        invoiceToSave.setInvoicesOrder(existingContract.getInvoicesPrinted() + 1);

        Invoice savedInvoice = invoiceRepository.save(invoiceToSave); //Save invoice

        //Update the invoice number printed in the contrcat
        existingContract.setInvoicesPrinted(existingContract.getInvoicesPrinted() + 1);
        contractRepository.save(existingContract);

        return savedInvoice;
    }


    public String getNextQuarterToPay(Contract existingContract, List<Invoice> listInvoices) {

         LocalDate start = existingContract.getStartDate().atZone(ZoneId.systemDefault()).toLocalDate();
        
        // Calculate the starting quarter
        int startQuarter = (start.getMonthValue() - 1) / 3 + 1;

        if (!listInvoices.isEmpty()) {
            // Retrieve the last invoice
            Optional<Invoice> lastInvoice = listInvoices.stream()
                    .max(Comparator.comparing(Invoice::getDueDate));
            if (lastInvoice.isPresent()) {
                String lastQuarter = lastInvoice.get().getQuaterPayment();
                if (lastQuarter != null && !lastQuarter.isEmpty()) {
                    int lastQuarterKey = getQuarterKey(lastQuarter);

                    // Determinate the next quarter
                    if (lastQuarterKey < 4) {
                        return QUARTER_PERIODS.get(lastQuarterKey + 1) + " " + LocalDate.now().getYear();
                    } else {
                        // Move to the next year
                        return QUARTER_PERIODS.get(1) + " " + (LocalDate.now().getYear() + 1);
                    }
                }
            }
        }

        // If no previous invoices, start with the first quarter
        return getQuarter(existingContract.getStartDate());

    }
    

    public String getQuarter(Instant startDate) {
        LocalDate date = startDate.atZone(ZoneId.systemDefault()).toLocalDate();

        int month = date.getMonthValue();
        int year = date.getYear();

        // Détermine le trimestre
        int quarter = (month - 1) / 3 + 1;
        String quaterToPay = QUARTER_PERIODS.get(quarter);

        if (quaterToPay != null) {
            return String.format("%s %d", quaterToPay, year);
        } else {
            throw new IllegalArgumentException("Unknown quarter for month: " + month);
        }
    }


    private int getQuarterKey(String quarterPeriod) {
        for (Map.Entry<Integer, String> entry : QUARTER_PERIODS.entrySet()) {
            if (entry.getValue().equals(quarterPeriod)) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("Unknown quarter period: " + quarterPeriod);
    }

    private String generateInvoicetNumber(String quaterPayment) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = currentDate.format(formatter);

        Random random = new Random();
        int randomNumber = random.nextInt(9000000) + 1000000; // for a 7 digits number
        // int randomNumber = random.nextInt(90) + 10; // Pour un nombre à 2 chiffres

        return formattedDate + "-" + String.valueOf(randomNumber) + "/" + quaterPayment;
    }


    public InvoiceResponse addFileToInvoice(Long idInvoice, MultipartFile file) {
        //Check if the contract form is valid
        Invoice existingInvoice = invoiceRepository.findById(idInvoice)
            .orElseThrow(() -> new EntityNotFoundException("Invoice not found! "));

        existingInvoice.setStatus(RentalStatus.ENABLE);
        invoiceRepository.save(existingInvoice);
        saveDocuments(file, existingInvoice);

        return InvoiceResponse.convertToDto(existingInvoice);
    }

    
    
    public ResponseEntity<Resource> getInvoiceResourcePdf(Long invoiceId) {
        Invoice existingInvoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found"));

        String pathUrl = existingInvoice.getFilePath();
        if (pathUrl == null || pathUrl.isEmpty()) {
            throw new EntityNotFoundException("No pdf found for this invoice! ");
        }

        Path filePath = uploadPath.resolve(Paths.get(pathUrl).getFileName().toString());
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new EntityNotFoundException("Could not read the invoice file! ");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }



    public ResponseEntity<byte[]> getInvoicePdf(Long invoiceId) {
        Invoice existingInvoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        String pathUrl = existingInvoice.getFilePath();
        if (pathUrl == null || pathUrl.isEmpty()) {
            throw new EntityNotFoundException("No PDF found for this invoice!");
        }

        Path filePath = uploadPath.resolve(Paths.get(pathUrl).getFileName().toString());
        try {
            // Lire le fichier en tant que tableau d'octets
            byte[] pdfContents = Files.readAllBytes(filePath);
            
            // Définir les en-têtes HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filePath.getFileName().toString());

            // Retourner la réponse avec le tableau d'octets
            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (IOException e) {
            // Gérer les exceptions en cas d'erreur de lecture
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    
    public ResponseEntity<Resource> getInvoicesResourcePdfByContractId(Long contractId) {
        Contract existingContract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found"));
        
        List<Invoice> listInvoices= invoiceRepository.findByContract(existingContract);

        if (listInvoices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(null);
        }

        // Create a ZIP file containing all invoices
        try {
            Path zipPath = Files.createTempFile("invoices-", ".zip");
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipPath))) {
                for (Invoice invoice : listInvoices) {
                    String filePathUrl = invoice.getFilePath();
                    if (filePathUrl != null && !filePathUrl.isEmpty()) {
                        Path filePath = uploadPath.resolve(Paths.get(filePathUrl).getFileName().toString());
                        try (InputStream fileInputStream = Files.newInputStream(filePath)) {
                            ZipEntry zipEntry = new ZipEntry(filePath.getFileName().toString());
                            zipOutputStream.putNextEntry(zipEntry);
                            fileInputStream.transferTo(zipOutputStream);
                            zipOutputStream.closeEntry();
                        }
                    }
                }
            }

            Resource zipResource = new UrlResource(zipPath.toUri());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"invoices.zip\"")
                    .body(zipResource);

        } catch (IOException e) {
            throw new RuntimeException("Error creating ZIP file: " + e.getMessage(), e);
        }

    }



    public ResponseEntity<byte[]> getInvoicesPdfByContractId(Long contractId) {
        Contract existingContract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found"));

        List<Invoice> listInvoices = invoiceRepository.findByContract(existingContract);

        if (listInvoices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(null);
        }

        try {
            // Create a ZIP file in memory
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
                for (Invoice invoice : listInvoices) {
                    String filePathUrl = invoice.getFilePath();
                    if (filePathUrl != null && !filePathUrl.isEmpty()) {
                        Path filePath = uploadPath.resolve(Paths.get(filePathUrl).getFileName().toString());
                        try (InputStream fileInputStream = Files.newInputStream(filePath)) {
                            ZipEntry zipEntry = new ZipEntry(filePath.getFileName().toString());
                            zipOutputStream.putNextEntry(zipEntry);
                            fileInputStream.transferTo(zipOutputStream);
                            zipOutputStream.closeEntry();
                        }
                    }
                }
            }

            byte[] zipContents = byteArrayOutputStream.toByteArray();
            
            // Set the HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "invoices.zip");

            return new ResponseEntity<>(zipContents, headers, HttpStatus.OK);

        } catch (IOException e) {
            throw new RuntimeException("Error creating ZIP file: " + e.getMessage(), e);
        }
    }



    private void createUploadDirIfNotExists() {
        try {
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create 'Invoice' directory", e);
        }
    }

}
