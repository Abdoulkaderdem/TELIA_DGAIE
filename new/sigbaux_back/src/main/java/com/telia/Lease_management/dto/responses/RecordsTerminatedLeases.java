package com.telia.Lease_management.dto.responses;

import org.apache.poi.hpsf.Date;

import com.telia.Lease_management.entity.common.Locality;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecordsTerminatedLeases {

    public RecordsTerminatedLeases(Long id, Long id2, String fullNameLandlord2, String leaseContractNumber2,
            String region2, String province2, String commune2, String city2, String district2, String sector2,
            String section2, String ilot2, String plot2, Locality locality2, String name, String name2,
            String reasonForTermination, java.util.Date dateCreated, Long rentAmount) {
        //TODO Auto-generated constructor stub
    }
    private Long idLandlord;
    private Long idContract;
    private String fullNameLandlord;
    private String leaseContractNumber;
    private String region;
    private String province;
    private String commune;
    private String city;
    private String district;
    private String sector;
    private String section;
    private String ilot;
    private String plot;
    private Locality locality;
    private String nameStructure;
    private String nameMinistry;
    private String resiliationDetails;
    private Date resiliationDate;
    private Long amountRent;
}
