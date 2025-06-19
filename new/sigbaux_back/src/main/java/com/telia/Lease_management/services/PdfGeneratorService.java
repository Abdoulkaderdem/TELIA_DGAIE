package com.telia.Lease_management.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;


import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.telia.Lease_management.dto.requests.ContractDto;
import com.telia.Lease_management.dto.requests.RentalRequestDto;
import com.telia.Lease_management.entity.MinisterialStructure;
import com.telia.Lease_management.entity.CNOI.ValidationCommittee;
import com.telia.Lease_management.entity.contract.ApplyForCompensation;
import com.telia.Lease_management.entity.contract.CancelledContrat;
import com.telia.Lease_management.entity.contract.Contract;
import com.telia.Lease_management.entity.contract.Invoice;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.LandLord;
import com.telia.Lease_management.entity.rental_offer.OfferAndCharacteristics;
import com.telia.Lease_management.entity.rental_offer.RentalOffer;
import com.telia.Lease_management.entity.rental_request.RentalRequest;
import com.telia.Lease_management.entity.rental_request.RequestAndCharacteristics;
import com.telia.Lease_management.entity.rental_request.TypeUsage;
import com.telia.Lease_management.repository.contract.ApplyForCompensationRepository;
import com.telia.Lease_management.repository.contract.CancelledContratRepository;
import com.telia.Lease_management.repository.contract.ContractRepository;
import com.telia.Lease_management.repository.rental_request.RentalRequestRepository;
import com.telia.Lease_management.services.rental_request.RentalRequestService;
import com.telia.Lease_management.utils.Converting;
import com.telia.Lease_management.utils.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


@AllArgsConstructor
@Service
@Slf4j
public class PdfGeneratorService {

//     private RentalRequestService rentalRequestService;
    private RentalRequestRepository rentalRequestRepo;
    private ContractRepository contractRepository;
    private ApplyForCompensationRepository aForCompensationRepo;
    private CancelledContratRepository cancelledContratRepo;



    public byte[] generateRentalRequestPdf(Long id) throws IOException, JRException {

        RentalRequest rentalRequest = rentalRequestRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Rental Request not found! "));
        // log.info("*** La liste:   {}", rentalRequest);

        String lang = "fr";
        Map<String, Object> parameters = new HashMap<>();

         // Charger le fichier .jrxml
        InputStream jrxmlInput = getClass().getResourceAsStream("/reports/rental_request.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInput);
        parameters.put("structureName", rentalRequest.getStructure().getName());
        parameters.put("motivationRequest", rentalRequest.getMotivationRequest());
        parameters.put("structureCurrentPosition", rentalRequest.getStructureCurrentPosition());
        parameters.put("agentsNumber", rentalRequest.getAgentsNumber());
        parameters.put("managersNumber", rentalRequest.getManagersNumber());
        parameters.put("leasePortfolioMinistry", rentalRequest.getLeasePortfolioMinistry());
        parameters.put("buildingsOccupancyStatus", rentalRequest.getBuildingsOccupancyStatus());
        
        // Créer une source de données pour JasperReports
        log.info("***  usageData:   {}",  rentalRequest.getListBuildingUsage());
        List<Map<String, Object>> usageData = rentalRequest.getListBuildingUsage().isEmpty() ? 
                new ArrayList<>() : 
                rentalRequest.getListBuildingUsage().stream()
                        .map(usage -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("usageLibCourt", usage.getLibCourt());
                        map.put("usageLibLong", usage.getLibLong());
                        return map;
                        })
                        .collect(Collectors.toList());
                
        log.info("***  rentalRequest.getListRequestAndCharacteristics():   {}",  rentalRequest.getListRequestAndCharacteristics());
        List<RequestAndCharacteristics> listRequestAndCharacteristics = rentalRequest.getListRequestAndCharacteristics().isEmpty()?
                        new ArrayList<>():
                        rentalRequest.getListRequestAndCharacteristics();

        // Convertir les listes en JRBeanCollectionDataSource
        JRBeanCollectionDataSource usageDataSource = new JRBeanCollectionDataSource(usageData);
        JRBeanCollectionDataSource characteristicsDataSource = new JRBeanCollectionDataSource(listRequestAndCharacteristics);

        parameters.put("usageDataSource", usageDataSource);
        parameters.put("characteristicsDataSource", characteristicsDataSource);

        // Générer le PDF
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
        return JasperExportManager.exportReportToPdf(jasperPrint);
        
        
        // ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // PdfWriter writer = new PdfWriter(baos);
        // PdfDocument pdf = new PdfDocument(writer);
        // Document document = new Document(pdf);

        // // Add content to PDF
        // document.add(new Paragraph("Fiche d’expression de besoin de location de bâtiments")
        //         .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
        //         .setFontSize(18));
        
        // document.add(new Paragraph("Identification de la ou des structures à loger: " + rentalRequest.getStructure().getName()));
        // document.add(new Paragraph("Motif (motivation) de la demande de location: " + rentalRequest.getMotivationRequest()));
        // document.add(new Paragraph("Usage du bâtiment à louer: "));

        // Table usageTable = new Table(2);
        // for (TypeUsage usage : rentalRequest.getListBuildingUsage()) {
        //     usageTable.addCell(usage.getLibCourt());
        //     usageTable.addCell(usage.getLibLong());
        // }
        // document.add(usageTable);

        // document.add(new Paragraph("Position actuelle de la ou des structures à loger: " + rentalRequest.getStructureCurrentPosition()));
        // document.add(new Paragraph("Effectif total des agents à loger: " + rentalRequest.getAgentsNumber()));
        // document.add(new Paragraph("Nombre de responsables à loger: " + rentalRequest.getManagersNumber()));
        // document.add(new Paragraph("Caractéristiques souhaitées: "));
        
        // Table characteristicsUsageTable = new Table(2);
        // for (RequestAndCharacteristics characteristic : rentalRequest.getListRequestAndCharacteristics()) {
        //     characteristicsUsageTable.addCell(characteristic.getValues().toString());
        // }
        // document.add(characteristicsUsageTable);

        // document.add(new Paragraph("Portefeuille des baux du ministère: " + rentalRequest.getLeasePortfolioMinistry()));
        // document.add(new Paragraph("Situation d’occupation des bâtiments administratifs: " + rentalRequest.getBuildingsOccupancyStatus()));

        // document.close();

        // return baos.toByteArray();
       

    }



     public byte[] generateContractPdf(Long idContract) throws IOException {

        //Retreive contract
        Contract contract = contractRepository.findById(idContract)
                .orElseThrow(() -> new RuntimeException("Contract not found! "));
        
        //Get siignatories
        String president= contract.getPresident() != null ? contract.getPresident(): " ";
        String reporter= contract.getReporter() != null ? contract.getReporter(): " ";
        String contractingAuthority = contract.getContractingAuthority() != null ? contract.getContractingAuthority(): " ";

        //Get amount rent number to word
        String amountRentToWord= Converting.NumberToWords(contract.getRentAmount());
        // log.info("amountRentToWord******************:  {}, amountRentToWord");

        //Getting items........
        Building building= contract.getBuilding(); //Get the the Building
        RentalOffer rentalOffer = building.getRentalOffer();
        RentalRequest rentalRequest= building.getRentalRequest();
        LandLord landLord= rentalOffer.getLandLord(); //Get the landlord
        MinisterialStructure structure = rentalRequest.getStructure(); //Get the structure

        String startDateFormated = DateUtils.formatInstantToDate(contract.getStartDate());
                
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        

        document.add(new Paragraph("CONTRAT DE BAIL N°" + contract.getLeaseContractNumber())
                 .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(20)
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("Entre: " + landLord.getLastname() + " " + landLord.getFirstname() + ", Adresse: " + landLord.getResidencePlace() + ", désigné dans ce qui suit par la dénomination le «Bailleur» d’une part,"));
        document.add(new Paragraph("et"));
        document.add(new Paragraph(" " + structure.getName() + ", désigné dans ce qui suit par la dénomination le « Preneur » d’autre part,"));
        document.add(new Paragraph("il a été arrêté et convenu ce qui suit :"));
        document.add(new Paragraph(" "));

        // ARticle 1 :................
        document.add(new Paragraph("ARTICLE 1: Objet")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(14));
        document.add(new Paragraph("Le bailleur donne à bail au preneur qui accepte l’immeuble de type: " + building.getTypeBuilding() + 
            " Bâti sur la Parcelle: " + building.getPlot() + ", du Lot: " + building.getIlot() + ", de la Section: " + building.getSection() + 
            ", du Secteur: " + building.getSector() + ", Geolocalisatiion:___" + building.getGeolocation() + ", pour les besoins du: " + 
            structure.getMinistry().getName() + "."));

        // ARticle 2 :................
        document.add(new Paragraph("ARTICLE 2: Description")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(14));
        document.add(new Paragraph("• Les immeubles, d’une superficie totale exploitable de: " + building.getBuildingArea() 
                + " m², se composent de :"));
        Table descriptionTable = new Table(2);
        for (OfferAndCharacteristics characteristics : building.getListOfferAndCharacteristics()) {
            descriptionTable.addCell(characteristics.getCharacteristics().getLibLong());
            descriptionTable.addCell(characteristics.getValues().toString());
        }
        document.add(descriptionTable);
        document.add(new Paragraph("• Branchements : ONEA, SONABEL ; ONATEL : fonctionnels").setMarginBottom(10));
        document.add(new Paragraph("Tel au surplus que lesdits lieux s’étendent et comportent sans aucune exception ni réserve de la part du preneur qui déclare les connaître pour les avoir vus et dont une plus ample description figure sur une fiche technique."));
      
        // ARticle 3 :................
        document.add(new Paragraph("ARTICLE 3: Usage et durée de la location")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(14));
        document.add(new Paragraph("La présente location est faite à usage de:  ").setMarginBottom(10));
        Table usageTable = new Table(1);
        for (TypeUsage typeUsage : rentalRequest.getListBuildingUsage()) {
            usageTable.addCell(typeUsage.getLibLong());
        }
        document.add(usageTable);
        document.add(new Paragraph(" pour une durée de: " + contract.getContractPeriodicity() 
                + " mois, renouvelable par tacite reconduction à compter du:  "
                + startDateFormated + ".").setMarginBottom(10));

        // ARticle 4 :................
        document.add(new Paragraph("ARTICLE 4 : Loyer")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(14));
        document.add(new Paragraph("La présente location est faite et acceptée moyennant un loyer mensuel de: " 
                + contract.getRentAmount() + " FCFA ( "
                + amountRentToWord
                + " FCFA) payable trimestriellement après dépôt d’une facture et à terme échu au compte du bailleur ou de son représentant ouvert à " 
                + "(établissement financier): "+ "................................. " + " à "+ "(localité): " + "................................. " 
                + ", sous le (numéro du compte bancaire):__ " 
                + contract.getBankAccountNumber() + ".").setMarginBottom(10));

        // ARticle 5 :................
         document.add(new Paragraph("ARTICLE 5 : Révision du loyer")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(14));
        document.add(new Paragraph("Le loyer fixé à l’article 4 est susceptible de révision à la demande de l’une ou l’autre des parties dans les conditions suivantes :"));
        document.add(new Paragraph("- l’aménagement, l’extension de l’immeuble ou l’installation de nouvelles commodités autorisés par le preneur ;"));
        document.add(new Paragraph("- la réduction de la superficie exploitable ou la suppression de certaines commodités ;"));
        document.add(new Paragraph("- la révision du barème indicatif des loyers applicable aux baux administratifs.").setMarginBottom(10));
        

        // ARticle 6 :................
        document.add(new Paragraph("ARTICLE 6 : Charges et Conditions")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(14));
        document.add(new Paragraph("Le présent bail est consenti et accepté aux conditions générales suivantes, que les parties s’obligent à exécuter, chacune en ce qui la concerne :"));
        document.add(new Paragraph("6.1 : A la Charge du Bénéficiaire"));
        document.add(new Paragraph("a) Le bénéficiaire occupera l’immeuble loué et en usera en bon locataire suivant la destination prévue ci-dessus et s’engage à ne pas modifier cette destination ;"));
        document.add(new Paragraph("b) Le bénéficiaire est tenu de maintenir les lieux loués en bon état, en accomplissant à temps toutes les réparations locatives et de menu entretien. Il s’agit :"));
        document.add(new Paragraph("• des réparations portant sur les parties extérieures destinées à l’usage privatif du bénéficiaire telles que les portes, les fenêtres, les panneaux de verre, les boulons et serrures ;"));
        document.add(new Paragraph("• des réparations des parties intérieures telles que les portes intérieures, les retouches de peinture et les travaux de ferronnerie ;"));
        document.add(new Paragraph("• de la réparation ou du remplacement des accessoires électriques tels que les luminaires, les prises et les interrupteurs à l’exclusion des dismatics, des disjoncteurs et des rhéostats qui demeurent à la charge du bailleur ;"));
        document.add(new Paragraph("• de la réparation ou du remplacement des installations sanitaires du local telles que le mécanisme de la chasse d’eau du WC, le mécanisme de la baignoire et du lavabo, la robinetterie et la colonne de douche ;"));
        document.add(new Paragraph("• du nettoyage courant du local et de l’entretien courant des espaces verts").setMarginBottom(10));
        document.add(new Paragraph("c) Le bénéficiaire s’engage à prévenir immédiatement le bailleur de toute détérioration qu’il constaterait et qui nécessiterait des réparations incombant à ce dernier ;"));
        document.add(new Paragraph("d) Le bénéficiaire ne pourra faire aucune modification ni transformation dans l’état ou la disposition des lieux sans autorisation préalable et expresse du bailleur ;e"));
        document.add(new Paragraph("e) Le bénéficiaire devra laisser le bailleur ou son représentant visiter les lieux loués à des heures raisonnables, chaque fois que le bailleur le jugera utile. Cependant ces visites ne devraient pas perturber le preneur et ce dernier devra en être averti au moins 48 heures à l’avance ;"));
        
        document.add(new Paragraph("6.2 : A la charge du Bailleur"));
        document.add(new Paragraph("a) Le bailleur s’engage à faire procéder à ses frais, toutes les grosses réparations devenues nécessaires constatées sur :"));
        document.add(new Paragraph("• les murs et les voûtes, le rétablissement des poutres et des couvertures entières, l’étanchéité, le décollement et la fissuration du béton et le ravalement des façades ;"));
        document.add(new Paragraph("• les installations et les équipements électriques, de plomberie et d’assainissement ;"));
        document.add(new Paragraph("• les digues, les murs de soutènement et les cheminées et autres ouvrages qui sont à la charge du bailleur ;"));
        document.add(new Paragraph("b) Le bailleur s’engage à remettre en état les parties communes dans le cas où des dégâts sont causés par le preneur ou par ses visiteurs").setMarginBottom(10));
        document.add(new Paragraph("• les parties extérieures destinées à l’usage commun telles que l’entrée principale, les ouvertures extérieures et la façade;"));
        document.add(new Paragraph("• l’adduction d’eau potable, le raccordement électrique extérieur, la tuyauterie et le câblage ;"));
        document.add(new Paragraph("• le curage des puits, des fosses d’aisance et des canalisations servant à l’écoulement des eaux ;"));
        document.add(new Paragraph("• l’entretien, la réparation et le remplacement des ascenseurs ;"));
        document.add(new Paragraph("• l’entretien, la réparation et le changement de la climatisation ;"));
        document.add(new Paragraph("• l’entretien, la réparation et le changement des brasseurs d’air ;"));
        document.add(new Paragraph("• l’entretien, la réparation et le changement du groupe électrogène ;"));
        document.add(new Paragraph("• les équipements viciés ou endommagés par la vétusté, les catastrophes naturelles, les intempéries ou la faute du bailleur ;"));
        document.add(new Paragraph("• le rafraichissement de la peinture intérieure, le nettoyage, la désinfection des locaux au cas où le bâtiment sous bail fait l’objet d’une réaffectation à un autre bénéficiaire."));
        document.add(new Paragraph("En tout état de cause l’entretien, la réparation et le changement de tout équipement entrant dans la détermination du loyer incombent au bailleur."));
        document.add(new Paragraph("c) Les grosses réparations et les réparations locatives sont assurées par le bailleur sans pouvoir en contrepartie demander une indemnité ou une augmentation de loyer."));
        document.add(new Paragraph("Le cahier de charges applicable aux baux administratifs précise de manière exhaustive les obligations des différentes parties."));


          // ARticle 7 :................
        document.add(new Paragraph("ARTICLE 7 : Etat des lieux-Restitution des lieux loués")
            .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
            .setFontSize(14));
        document.add(new Paragraph("Avant l’entrée en jouissance du bâtiment par le preneur, un état des lieux détaillé sera dressé contradictoirement entre les parties en deux exemplaires."));
        document.add(new Paragraph("Un état des lieux sera également dressé à la fin du bail, au moment de la libération des lieux par le preneur, immédiatement avant la remise des clés."));

        // ARticle 8 :................
        document.add(new Paragraph("ARTICLE 8 : Prélèvement sur loyer")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(14));
        document.add(new Paragraph("Conformément aux textes en vigueur, le preneur opérera par loyer payé une retenue correspondant au montant de l’Impôt sur les Revenus Fonciers (IRF) qu’il versera au service des impôts.").setMarginBottom(10));
        
        
        // ARticle 9 :................
        document.add(new Paragraph("ARTICLE 9 : Droits d’enregistrement")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(14));
        document.add(new Paragraph("Les formalités d’enregistrement et de timbre du présent contrat sont à la charge et à la diligence du bailleur.") 
                .setMarginBottom(10));
        document.add(new Paragraph("L’enregistrement du contrat de bail est annuel auprès des services des impôts du ressort territorial compétent.")
                .setMarginBottom(10));


        // ARticle 10 :................
        document.add(new Paragraph("ARTICLE 10 : Litiges")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(14));
        document.add(new Paragraph("Pour tout litige qui viendrait à se produire dans l’exécution du présent contrat, les parties privilégieront le règlement amiable. En cas d’échec le litige sera soumis aux instances compétentes du Burkina Faso.") 
                .setMarginBottom(10));

        
        // ARticle 11 :................
        document.add(new Paragraph("ARTICLE 10 : Modification du contrat")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(14));
        document.add(new Paragraph("Les modifications apportées au présent contrat feront l’objet d’avenant ou de nouveau contrat.") 
                .setMarginBottom(10));

        
        // ARticle 12 :................
        document.add(new Paragraph("ARTICLE 12 : Résiliation")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(14));
        document.add(new Paragraph("Chacune des parties a la faculté de ne pas renouveler le présent contrat. La résiliation du contrat de bail est subordonnée à la notification d’un préavis de trois (03) mois. Lorsqu’elle est à l’initiative du bailleur, le préavis est fixé à six (06) mois."));
        document.add(new Paragraph("Toutefois, la résiliation se fera sans délai:"));
        document.add(new Paragraph("• en cas de force majeure ;"));
        document.add(new Paragraph("• en cas de non-respect des obligations par l’une ou l’autre des parties, après une mise en demeure restée sans suite."));
        document.add(new Paragraph("Les notifications sont adressées au preneur qui les soumettra éventuellement à la (CNOI ou instance compétente), avec accusé de réception."));
        document.add(new Paragraph("En cas de résiliation du contrat de bail, une indemnité forfaitaire de remise en état (IRE) est versée au bailleur conformément à l’arrêté n°2017-159/MINEFID/SG/DGAIE du 26 mai 2017.").setMarginBottom(10));
        
        
        // ARticle 13 :................
        document.add(new Paragraph("ARTICLE 13 : Election du domicile")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(14));
        document.add(new Paragraph("Pour l’exécution des présentes, les parties font élection de domicile en leurs adresses respectives précisées ci-dessous : "));
        document.add(new Paragraph("• le bailleur à (adresse): " + landLord.getResidencePlace()));
        document.add(new Paragraph("• le preneur en ses bureaux (Ministère/Institution/EPE/CT, à (lieu): " + ".........................."));
        document.add(new Paragraph(" "));
        
        // Finalisation :................
        document.add(new Paragraph("Autorisé par le (Conseil des Ministres/instance compétente): " +"............................."
                + " suivant rapport/délibération (numéro): " + "......................................"));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        // Centered paragraphs with appropriate margins
        document.add(new Paragraph("Le Bailleur(1):  "+ landLord.getLastname() + " "+ landLord.getFirstname())
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10)
                .setMarginTop(40));
        document.add(new Paragraph(" ")
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Rapporteur de la CNOI/ Instance compétente: " + reporter)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10)
                .setMarginTop(20));
        document.add(new Paragraph(" ")
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Le Président de la CNOI/Instance: " + president)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10)
                .setMarginTop(20));
        document.add(new Paragraph(" ")
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Le Preneur (Autorité contractante): " + contractingAuthority)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10)
                .setMarginTop(20));
       
        document.add(new Paragraph(" ")
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph(" ")
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph(" ")
                .setTextAlignment(TextAlignment.CENTER));

        // Signature centered
        document.add(new Paragraph("Fait à Ouagadougou, le: ")
                .setFontSize(12)
                .setMarginTop(20)
                .setTextAlignment(TextAlignment.CENTER));
        
        
        document.add(new Paragraph(" ")
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph(" ")
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph(" ")
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("(1) Faire précéder la signature de la mention manuscrite « Lu et approuvé » ")
                .setMarginBottom(10)
                .setMarginTop(20)
                .setTextAlignment(TextAlignment.CENTER));
                
        // Fermer le document
        document.close();

        return baos.toByteArray();

     }

    

     public byte[] generateInvoiceExcel(Invoice invoiceToGenerate) throws IOException {
        Contract contract = invoiceToGenerate.getContract();
        Building building = contract.getBuilding();
        LandLord landLord = building.getRentalOffer().getLandLord();
        RentalRequest rentalRequest= building.getRentalRequest();
        MinisterialStructure structure = rentalRequest.getStructure();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Invoice");

            // Set up styles
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            CellStyle dateStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));

            int rowIdx = 0;

            // Nom et Prénom(s) du bailleur
            Row row = sheet.createRow(rowIdx++);
            Cell cell = row.createCell(0);
            cell.setCellValue("Nom et Prénom(s) du bailleur: " + landLord.getLastname() + " " + landLord.getFirstname());

            // (Tél :)
            row = sheet.createRow(rowIdx++);
            cell = row.createCell(0);
            cell.setCellValue("Tél :");
            cell = row.createCell(1);
            cell.setCellValue(landLord.getPhoneNumber());

            // (Numéro de compte bancaire et de domiciliation)
            row = sheet.createRow(rowIdx++);
            cell = row.createCell(0);
            cell.setCellValue("Numéro de compte bancaire et de domiciliation: " );
            cell = row.createCell(1);
            cell.setCellValue(contract.getBankAccountNumber());

            // (Lieu d’établissement de la facture), le (jour, mois et année)
            row = sheet.createRow(rowIdx++);
            cell = row.createCell(0);
            cell.setCellValue("Ouagadougou, le: ");
            cell = row.createCell(1);
            cell.setCellValue(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            row = sheet.createRow(rowIdx++);
            // FACTURE N° :
            row.createCell(0).setCellValue("FACTURE N° :");
            row.createCell(1).setCellValue(invoiceToGenerate.getInvoiceReference());

            row = sheet.createRow(rowIdx++);
            // Suivant contrat de bail N° (numéro du contrat de bail) du (date d’approbation par l’autorité contractante)
            row.createCell(0).setCellValue("Suivant contrat de bail N° " + invoiceToGenerate.getContract().getLeaseContractNumber() + " du " +
                invoiceToGenerate.getContract().getStartDate().atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            row = sheet.createRow(rowIdx++);
            // DOIT : Ministère/Institution/EPE/CT
            row.createCell(0).setCellValue("DOIT : " +  structure.getMinistry().getName());

            row = sheet.createRow(rowIdx++);
            // Pour la location de l’immeuble à usage de ...
            row.createCell(0).setCellValue("Pour la location de l’immeuble à usage de ...");
            int i = rentalRequest.getListBuildingUsage().size();
            for (TypeUsage usage: rentalRequest.getListBuildingUsage()) {
                cell = row.createCell(i);
                cell.setCellValue(usage.getLibLong());
            }

            // Table header
            row = sheet.createRow(rowIdx++);
            String[] headers = {"N°d’ordre", "Période", "Nombre de mois", "Loyer mensuel", "Montant"};
            for (int j = 0; j < headers.length; j++) {
                cell = row.createCell(j);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Table content
            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(invoiceToGenerate.getInvoicesOrder());
            row.createCell(1).setCellValue(invoiceToGenerate.getQuaterPayment());
            row.createCell(2).setCellValue(3);
            row.createCell(3).setCellValue(invoiceToGenerate.getContract().getRentAmount());
            row.createCell(4).setCellValue(invoiceToGenerate.getAmount());

            row = sheet.createRow(rowIdx++);
            // TOTAL
            row.createCell(3).setCellValue("TOTAL");
            row.createCell(4).setCellValue(invoiceToGenerate.getAmount());

            row = sheet.createRow(rowIdx++);
            // Arrêtée la présente facture à la somme de ...
            row.createCell(0).setCellValue("Arrêtée la présente facture à la somme de : " + invoiceToGenerate.getAmount());

            row = sheet.createRow(rowIdx++);
            // Signatures
            row.createCell(0).setCellValue("L’occupant");
            row.createCell(4).setCellValue("Le Bailleur");

            // Auto-size columns
            for (int k = 0; k < headers.length; k++) {
                sheet.autoSizeColumn(i);
            }

            // Write the output to a byte array
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }
        }
    }



    public byte[] generateContractTerminationNoticePdf(Long idContract, List<String> ampliations) throws IOException{
        //Retreive contract
        Contract contract = contractRepository.findById(idContract)
            .orElseThrow(() -> new RuntimeException("Contract not found! "));

        //Treat datas
        if (ampliations!= null && !ampliations.isEmpty()){
                contract.setAmpliations(ampliations);
                contractRepository.save(contract);
        }

        //Getting items........
        String startDateFormated = DateUtils.formatInstantToDate(contract.getStartDate());
        String endDateFormated = DateUtils.formatInstantToDate(contract.getEndDate());

        Building building= contract.getBuilding(); //Get the the Building
        RentalOffer rentalOffer = building.getRentalOffer();
        RentalRequest rentalRequest= building.getRentalRequest();
        LandLord landLord= rentalOffer.getLandLord(); //Get the landlord
        MinisterialStructure structure = rentalRequest.getStructure(); //Get the structure
                
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        for (int i = 0; i <= 10; i++) {
                document.add(new Paragraph(" "));
        }

        document.add(new Paragraph("Ouagadougou le, " )
                 .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("le Ministre " )
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("A" )
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Monsieur: " + landLord.getLastname() + " " + landLord.getFirstname())
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph(landLord.getPhoneNumber())                
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph(landLord.getResidencePlace())    
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER));
        for (int i = 0; i <= 2; i++) {
                document.add(new Paragraph(" "));
        }

        document.add(new Paragraph("Objet: Préavis de résiliation de contrat de bail N° " + contract.getLeaseContractNumber()));  
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Monsieur,")    
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)));

        document.add(new Paragraph("Suivant contrat de bail N° " + contract.getLeaseContractNumber() + ", votre bâtiment du type "
                + building.getTypeBuilding() + " sis à " + building.getCity()
                + " a été pris  en location  par l'Etat  pour  les  besoins  du  " + structure.getName()));

        document.add(new Paragraph("Cependant, suite à une réorganisation, votre bâtiment sera libéré par les services bénéficiaires."));

        document.add(new Paragraph("A cet effet, j'ai l'honneur, par la présente, de vous notifier un préavis de résiliation du contrat"
                 +" de bail y relatif, courant "
                 +startDateFormated +" au " + endDateFormated + " inclus, et ce, conformément aux dispositions de l'article 4 dudit contrat de bail."));

        document.add(new Paragraph("Veuillez agréer, Monsieur, l'expression de mes salutations distinguées."));

        for (int i = 0; i <= 10; i++) {
                document.add(new Paragraph(" "));
        }
            

        document.add(new Paragraph("Ampliations: ")    
                 .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)));
        
        if (contract.getAmpliations() != null && !contract.getAmpliations().isEmpty()) {
                for (int j = 0; j < contract.getAmpliations().size(); j++) {
                        document.add(new Paragraph(contract.getAmpliations().get(j)));
                }
        }

        // Fermer le document
        document.close();

        return baos.toByteArray();

    }



        public byte[] generateContractTerminationIrePdf(Long idContract) throws IOException{
                //Retreive contract
                Contract contract = contractRepository.findById(idContract)
                        .orElseThrow(() -> new RuntimeException("Contract not found! "));
                
                //Retreive Apply for Compensation from Landlord
                ApplyForCompensation applyForCompensation = aForCompensationRepo.findByContract(contract)
                        .orElseThrow(() -> new RuntimeException("The Apply for Landlord not found! "));

                //Retreive terminate Contract instance
                CancelledContrat cancelledContrat = cancelledContratRepo.findByContract(contract)
                        .orElseThrow(() -> new RuntimeException("Contract is not Terminated! Ending the contract first. "));
                //Getting items........
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                //Get amount rent number to word
                Double indemnityAmount= cancelledContrat.getIndemnityAmount();
                Long longValue = indemnityAmount.longValue();
                String amountIreToWord= Converting.NumberToWords(longValue);
               
                String applyDateFormated = sdf.format(applyForCompensation.getDateCreated()); 

                Building building= contract.getBuilding(); //Get the the Building
                RentalOffer rentalOffer = building.getRentalOffer();
                RentalRequest rentalRequest= building.getRentalRequest();
                LandLord landLord= rentalOffer.getLandLord(); //Get the landlord
                MinisterialStructure structure = rentalRequest.getStructure(); //Get the structure
                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PdfWriter writer = new PdfWriter(baos);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                for (int i = 0; i <= 10; i++) {
                        document.add(new Paragraph(" "));
                }

                document.add(new Paragraph("Ouagadougou le, " )
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                        .setTextAlignment(TextAlignment.CENTER));

                document.add(new Paragraph("Le Ministre Délégué, " )
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setFontSize(14)
                        .setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("chargé du budget" )
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setFontSize(14)
                        .setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("A" )
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                        .setFontSize(14)
                        .setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("Monsieur: " + landLord.getLastname() + " " + landLord.getFirstname())
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setFontSize(14)
                        .setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph(landLord.getPhoneNumber())                
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setFontSize(14)
                        .setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph(landLord.getResidencePlace())    
                        .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                        .setFontSize(14)

                .setTextAlignment(TextAlignment.CENTER));
                for (int i = 0; i <= 2; i++) {
                document.add(new Paragraph(" "));
                }

                document.add(new Paragraph("Objet: Octroi d'une indemnité de remise en état"));  
                document.add(new Paragraph(" "));

                document.add(new Paragraph("Monsieur,"));

                document.add(new Paragraph("Suivant contrat de bail N° " + contract.getLeaseContractNumber() 
                        + ", l'État burkinabé avait pris en location votre bâtiment situé à "
                        + building.getCity()
                        + "au profit du  " + structure.getName()));

                document.add(new Paragraph("Suite à la résiliation dudit contrat de bail, vous avez, par lettre du "
                        + applyDateFormated + ", sollicité l'indemnité de remise en état de votre bâtiment."));

                document.add(new Paragraph("Conformément aux dispositions de l'arrêté n°2017-159/MINEFID/SG/DG.AE du 26 mai 2017,"
                        + "relatif aux modalités de détermination et de libération de l'indemnité de remise en état des bâtiments" 
                        + "loués par l'État, le montant de l'indemnité allouée pour la remise en état de votre bâtiment s'est établi à: "
                        + amountIreToWord + "( "+ longValue + " ) francs CFA."));

                document.add(new Paragraph("Aussi, j'ai l'honneur par le presente, de vous informer que le déblocage du montant sumentionné sera fait à votre profit."));

                document.add(new Paragraph("Veuillez agréer, Monsieur, l'expression de ma considération distinguée."));

                // Fermer le document
                document.close();

                return baos.toByteArray();
        }

     
    
}
