package com.telia.Lease_management.utils;

import java.util.ArrayList;
import java.util.List;

import com.telia.Lease_management.dto.requests.BuildingDto;
import com.telia.Lease_management.dto.requests.CommitteeMemberDto;
import com.telia.Lease_management.dto.requests.InvoiceDTO;
import com.telia.Lease_management.dto.requests.LandLordDto;
import com.telia.Lease_management.dto.requests.MinisterialStructureDto;
import com.telia.Lease_management.dto.requests.MinistryDto;
import com.telia.Lease_management.dto.requests.RentalCharacteristicsDto;
import com.telia.Lease_management.dto.requests.RentalOfferDto;
import com.telia.Lease_management.dto.requests.RentalRequestDto;
import com.telia.Lease_management.dto.requests.TypeUsageDto;
import com.telia.Lease_management.dto.requests.UserDto;
import com.telia.Lease_management.entity.common.BuildingStanding;
import com.telia.Lease_management.dto.requests.ContractDto;

import org.springframework.util.StringUtils;

public class ValidateForms {
    
    public static List<String> validateUser(UserDto usersDto) {
        List<String> errors = new ArrayList<>();

        if (usersDto == null) {
            errors.add("Fields are empty, please fill them in !");
            return errors;
        }
        if (!StringUtils.hasLength(usersDto.getFirstName())) {
            errors.add("Please fill in the User first name !");
        }
        if (!StringUtils.hasLength(usersDto.getLastName())) {
            errors.add("Please fill in the User Last name !");
        }
        if (!StringUtils.hasLength(usersDto.getPassword())) {
            errors.add("Please fill in the User Password !");
        }
        if (!StringUtils.hasLength(usersDto.getEmail())) {
            errors.add("Please enter the user's e-mail address");
        }
        if (usersDto.getRole() == null) {
            errors.add("Role is missing !");
        }
        if (usersDto.getTypeUser() == null) {
            errors.add("Please Select the user's type");
        }

        return errors;
    }


    public static List<String> validateRentalHouse(RentalOfferDto offerDto) {
        List<String> errors = new ArrayList<>();

        if (offerDto == null) {
            errors.add("Fields are empty, please fill them in !");
            return errors;
        }

        return errors;
    }


    
    public static List<String> validateMinistry(MinistryDto ministryDto) {
        List<String> errors = new ArrayList<>();

        if (ministryDto == null) {
            errors.add("Fields are empty, please fill them in !");
            return errors;
        }
        if (!StringUtils.hasLength(ministryDto.getName())) {
            errors.add("Please fill  the name !");
        }

        return errors;
    }
    
    public static List<String> validateMinisterialStructure(MinisterialStructureDto stuctureDto) {
        List<String> errors = new ArrayList<>();

        if (stuctureDto == null) {
            errors.add("Fields are empty, please fill them in !");
            return errors;
        }
        if (!StringUtils.hasLength(stuctureDto.getName())) {
            errors.add("Please fill  the name !");
        }

        return errors;
    }

    
    public static List<String> validateRentalRequest(RentalRequestDto requestDto) {
        List<String> errors = new ArrayList<>();

        if (requestDto == null) {
            errors.add("Rental request is null.");
            return errors;
        }

        // Check for null or empty fields
        if (!StringUtils.hasLength(requestDto.getDescription())) {
            errors.add("Description is required.");
        }

        if (!StringUtils.hasLength(requestDto.getMotivationRequest())) {
            errors.add("Motivation request is required.");
        }

        if (!StringUtils.hasLength(requestDto.getStructureCurrentPosition())) {
            errors.add("Current structure position is required.");
        }

        if (!StringUtils.hasLength(requestDto.getAgentsNumber())) {
            errors.add("Number of agents is required.");
        }

        if (!StringUtils.hasLength(requestDto.getManagersNumber())) {
            errors.add("Number of managers is required.");
        }

        // Check for geographical location fields
        if (!StringUtils.hasLength(requestDto.getRegion())) {
            errors.add("Region is required.");
        }

        if (!StringUtils.hasLength(requestDto.getProvince())) {
            errors.add("Province is required.");
        }

        if (!StringUtils.hasLength(requestDto.getCommune())) {
            errors.add("Commune is required.");
        }

        if (!StringUtils.hasLength(requestDto.getCity())) {
            errors.add("City is required.");
        }

        if (!StringUtils.hasLength(requestDto.getDistrict())) {
            errors.add("District is required.");
        }

        // if (!StringUtils.hasLength(requestDto.getSector())) {
        //     errors.add("Sector is required.");
        // }

        // Check for geographic location of choice fields
        if (!StringUtils.hasLength(requestDto.getRegionDesired())) {
            errors.add("Desired region is required.");
        }

        if (!StringUtils.hasLength(requestDto.getProvinceDesired())) {
            errors.add("Desired province is required.");
        }

        if (!StringUtils.hasLength(requestDto.getCommuneDesired())) {
            errors.add("Desired commune is required.");
        }

        if (!StringUtils.hasLength(requestDto.getCityDesired())) {
            errors.add("Desired city is required.");
        }

        if (!StringUtils.hasLength(requestDto.getDistrictDesired())) {
            errors.add("Desired district is required.");
        }
            
        if (requestDto.getNumberOfBathroom() < 0) {
            errors.add("Please provide a correct NumberOfBathroom!");
        }
        if (requestDto.getNumberOfRoom() <= 0) {
            errors.add("Please provide a correct getNumberOfRoom!");
        }
        if (requestDto.getNumberOfRoomMeeting()  < 0) {
            errors.add("Please provide a correct getNumberOfRoomMeeting!");
        }

        // if (!StringUtils.hasLength(requestDto.getSectorDesired())) {
        //     errors.add("Desired sector is required.");
        // }

        // Check for other fields
        if (!StringUtils.hasLength(requestDto.getLeasePortfolioMinistry())) {
            errors.add("Lease portfolio ministry is required.");
        }

        if (!StringUtils.hasLength(requestDto.getBuildingsOccupancyStatus())) {
            errors.add("Buildings occupancy status is required.");
        }

        if (requestDto.getListBuildingUsageDto() == null || requestDto.getListBuildingUsageDto().isEmpty()) {
            errors.add("Building usage must be provided.");
        }

        if (requestDto.getStructure() == null) {
            errors.add("Ministerial structure is required.");
        } else {
            // Validate the structure
            errors.addAll(validateMinisterialStructure(requestDto.getStructure()));
        }

        // if (requestDto.getListRequestAndCharacteristicsDtos() == null || requestDto.getListRequestAndCharacteristicsDtos().isEmpty()) {
        //     errors.add("At least one request characteristic must be provided.");
        // }

        return errors;
    }

    
    public static List<String> validateRentalCharacteristics (RentalCharacteristicsDto rentalCharacteristicsDto) {
        List<String> errors = new ArrayList<>();

        if (rentalCharacteristicsDto == null) {
            errors.add("Fields are empty, please fill them in !");
            return errors;
        }
         
        if (!StringUtils.hasLength(rentalCharacteristicsDto.getLibCourt())) {
            errors.add("Please fill  the Libele !");
        }
        
        if (!StringUtils.hasLength(rentalCharacteristicsDto.getLibLong())) {
            errors.add("Please fill  the Libele !");
        }
          
        if (rentalCharacteristicsDto.getUnitPrice() == null || rentalCharacteristicsDto.getUnitPrice() <= 0.0) {
            errors.add("Please fill the Unit Price!");
        }

        return errors;
    }
    

    public static List<String> validateCommitteeMember(CommitteeMemberDto memberDto) {
        List<String> errors = new ArrayList<>();

        if (memberDto == null) {
            errors.add("Fields are empty, please fill them !");
            return errors;
        }
        if (!StringUtils.hasLength(memberDto.getFirstName())) {
            errors.add("Please fill in the member first name !");
        }
        if (!StringUtils.hasLength(memberDto.getLastName())) {
            errors.add("Please fill in the member Last name !");
        }
        if (!StringUtils.hasLength(memberDto.getPhoneNumber())) {
            errors.add("Please fill in the member phone !");
        }
        if (!StringUtils.hasLength(memberDto.getMatricule())) {
            errors.add("Please fill in the Matricule !");
        }

        return errors;
       
    }


    
    public static List<String> validateTypeUsages(TypeUsageDto typeUsageDto) {
        List<String> errors = new ArrayList<>();

        if (typeUsageDto == null) {
            errors.add("Fields are empty, please fill them in !");
            return errors;
        }
        if (!StringUtils.hasLength(typeUsageDto.getLibCourt())) {
            errors.add("Please fill  court Libelle !");
        }

        if (!StringUtils.hasLength(typeUsageDto.getLibLong())) {
            errors.add("Please fill  Long Libelle !");
        }

        return errors;
    }

        
    public static List<String> validateInvoice(InvoiceDTO invoiceDTO) {
        List<String> errors = new ArrayList<>();

        if (invoiceDTO == null) {
            errors.add("Fields are empty, please fill them in !");
            return errors;
        }

        if (invoiceDTO.getContractId()==null) {
            errors.add("Please fill  the contract ID !");
        }

        if (invoiceDTO.getAmount()==null) {
            errors.add("Please fill  the amount !");
        }
        if (invoiceDTO.getDueDate()==null) {
            errors.add("Please fill  the Date !");
        }
        if (!StringUtils.hasLength(invoiceDTO.getInvoiceReference())) {
            errors.add("Please fill  the invoice Reference !");
        }
        if (!StringUtils.hasLength(invoiceDTO.getStartInterval())) {
            errors.add("Please fill  the invoice Reference !");
        }
        if (!StringUtils.hasLength(invoiceDTO.getEndInterval())) {
            errors.add("Please fill  the invoice Reference !");
        }


        return errors;
    }

       
    public static List<String> validateContract(ContractDto contractDto) {
        List<String> errors = new ArrayList<>();

        if (contractDto == null) {
            errors.add("Fields are empty, please fill them in !");
            return errors;
        }

        if (contractDto.getBuildingId()==null) {
            errors.add("Please fill  the building ID !");
        }

        if (contractDto.getStartDate()==null) {
            errors.add("Please fill  the start date !");
        }
        if (contractDto.getContractPeriodicity()<1) {
            errors.add("Periodicity is incorrect !");
        }
        if (!StringUtils.hasLength(contractDto.getBankAccountNumber())) {
            errors.add("Please fill  the Bank account number !");
        }
        if (!StringUtils.hasLength(contractDto.getPresident())) {
            errors.add("Please President is missing !");
        }
        if (!StringUtils.hasLength(contractDto.getReporter())) {
            errors.add("Please Reporter is missing  !");
        }
        if (!StringUtils.hasLength(contractDto.getContractingAuthority())) {
            errors.add("Please Contracting Authority is missing  !");
        }


        return errors;
    }
    
       
    public static List<String> validateBuilding(BuildingDto buildingDto) {
        List<String> errors = new ArrayList<>();

        if (buildingDto == null) {
            errors.add("Fields are empty, please fill them in !");
            return errors;
        }
//        if (!StringUtils.hasLength(buildingDto.getBuildingValue())) {
//            errors.add("Please provide the building value!");
//        }
    
        if (!StringUtils.hasLength(buildingDto.getRegion())) {
            errors.add("Please provide the region!");
        }
    
        if (!StringUtils.hasLength(buildingDto.getProvince())) {
            errors.add("Please provide the province!");
        }
    
        if (!StringUtils.hasLength(buildingDto.getCommune())) {
            errors.add("Please provide the commune!");
        }
    
        if (!StringUtils.hasLength(buildingDto.getCity())) {
            errors.add("Please provide the city!");
        }
    
        if (!StringUtils.hasLength(buildingDto.getDistrict())) {
            errors.add("Please provide the district!");
        }
    
        if (buildingDto.getNumberOfBathroom() < 0) {
            errors.add("Please provide correct NumberOfBathroom!");
        }
        if (buildingDto.getNumberOfRoom() <= 0) {
            errors.add("Please provide correct NumberOfRoom!");
        }
        if (buildingDto.getNumberOfRoomMeeting()  < 0) {
            errors.add("Please provide correct NumberOfRoomMeeting!!");
        }
    
       // Validate listBuildingStanding
        if (buildingDto.getListBuildingStanding() == null || buildingDto.getListBuildingStanding().isEmpty()) {
            errors.add("Building standing list cannot be null or empty!");
        } else {
            for (BuildingStanding standing : buildingDto.getListBuildingStanding()) {
                if (standing == null) {
                    errors.add("Building standing list contains an invalid value!");
                    break;
                }
            }
        }

        // Validate typePropertyTitle
        if (buildingDto.getTypePropertyTitle() == null) {
            errors.add("Type of property title must be provided and valid!");
        }     
        
        return errors;
    }
    

    public static List<String> validateLandLord(LandLordDto landLordDto) {
         List<String> errors = new ArrayList<>();

        if (landLordDto == null) {
            errors.add("Landlord details are empty, please provide the required information!");
            return errors;
        }

        if (!StringUtils.hasLength(landLordDto.getIfu())) {
            errors.add("Please provide the IFU (Identification Fiscal Unique)!");
        }

        if (landLordDto.getTypeLandLord() == null) {
            errors.add("Please specify the type of landlord!");
        }

        if (landLordDto.getQualityApplicant() == null) {
            errors.add("Please specify the quality of the applicant!");
        }

        if (!StringUtils.hasLength(landLordDto.getFirstname())) {
            errors.add("Please provide the first name!");
        }

        if (!StringUtils.hasLength(landLordDto.getLastname())) {
            errors.add("Please provide the last name!");
        }

        if (!StringUtils.hasLength(landLordDto.getCompanyName()) && 
            !StringUtils.hasLength(landLordDto.getFirstname()) && 
            !StringUtils.hasLength(landLordDto.getLastname())) {
            errors.add("Please provide either the company name or the first and last names!");
        }

        // if (!StringUtils.hasLength(landLordDto.getBp())) {
        //     errors.add("Please provide the BP (Box Postal)!");
        // }

        if (!StringUtils.hasLength(landLordDto.getPhoneNumber())) {
            errors.add("Please provide the phone number!");
        }

//        if (!StringUtils.hasLength(landLordDto.getWhatsapp())) {
//            errors.add("Please provide the WhatsApp number!");
//        }

//        if (!StringUtils.hasLength(landLordDto.getEmailAdress())) {
//            errors.add("Please provide the email address!");
//        } else if (!landLordDto.getEmailAdress().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
//            errors.add("Please provide a valid email address!");
//        }
        
        if (!StringUtils.hasLength(landLordDto.getResidencePlace())) {
            errors.add("Please provide the residence place!");
        }

        return errors;
    }
        
}
