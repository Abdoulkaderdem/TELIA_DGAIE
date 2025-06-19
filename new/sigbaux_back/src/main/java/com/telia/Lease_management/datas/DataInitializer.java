package com.telia.Lease_management.datas;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.telia.Lease_management.entity.CNOI.RentPrice;
import com.telia.Lease_management.entity.common.Locality;
import com.telia.Lease_management.entity.common.TypeBuilding;
import com.telia.Lease_management.entity.common.Zone;
import com.telia.Lease_management.entity.rental_offer.RentalCharacteristics;
import com.telia.Lease_management.repository.CNOI.RentPriceRepository;
import com.telia.Lease_management.repository.rental_offer.RentalCharacteristicsRepository;

@Component
public class DataInitializer implements CommandLineRunner{

    @Autowired
    private RentalCharacteristicsRepository rentalCharacteristicsRepo;

    @Autowired
    private RentPriceRepository rentPriceRepository;


    @Override
    public void run(String... args) throws Exception {
        loadRentalCharacteristics();
        loadRentPrices();
    }

    private void loadRentalCharacteristics() {
        List<RentalCharacteristics> characteristics = Arrays.asList(
            new RentalCharacteristics("VENTIL", "VENTILATEUR", 1000.0),
            new RentalCharacteristics("CLIM", "CLIMATISEUR", 6000.0),
            new RentalCharacteristics("SPLIT", "SPLIT_SYSTEM", 8000.0),
            new RentalCharacteristics("BAIN", "BAIGNOIRE", 2000.0),
            new RentalCharacteristics("CE", "CHAUFFE_EAU", 3000.0),
            new RentalCharacteristics("CHE", "CHATEAU_EAU", 8000.0),
            new RentalCharacteristics("PSN", "PISCINE", 18000.0),
            new RentalCharacteristics("ASC", "ASCENSEUR", 75000.0),
            new RentalCharacteristics("GElectro", "GROUPE_ELECTROGENE", 15000.0),
            new RentalCharacteristics("PS", "PLAQUE_SOLAIRE", 15000.0),
            new RentalCharacteristics("CInfo", "CABLE_INFORMATIQUE", 0.01),
            new RentalCharacteristics("PAVE-EspVert", "PAVE-ESPACE_VERT",250.0),
            new RentalCharacteristics("DLG", "DALLAGES", 200.0)
        );

        for (RentalCharacteristics characteristic : characteristics) {
            if (!rentalCharacteristicsRepo.findByLibLong(characteristic.getLibLong()).isPresent()) {
                rentalCharacteristicsRepo.save(characteristic);
            }
        }
    }

    @Transactional
    private void loadRentPrices() {
        if (rentPriceRepository.count() == 0) {
            List<RentPrice> prices = Arrays.asList(
                new RentPrice(null, Locality.OUAGA, Zone.ZH, TypeBuilding.B1, 375),
                new RentPrice(null, Locality.OUAGA, Zone.ZCI, TypeBuilding.B1, 415),
                new RentPrice(null, Locality.OUAGA, Zone.ZRA, TypeBuilding.B1, 435),
                new RentPrice(null, Locality.BOBO, Zone.ZH, TypeBuilding.B1, 355),
                new RentPrice(null, Locality.BOBO, Zone.ZCI, TypeBuilding.B1, 375),
                new RentPrice(null, Locality.BOBO, Zone.ZRA, TypeBuilding.B1, 395),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZH, TypeBuilding.B1, 335),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZCI, TypeBuilding.B1, 345),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZRA, TypeBuilding.B1, 355),
                new RentPrice(null, Locality.AUTRES, Zone.ZH, TypeBuilding.B1, 315),
                new RentPrice(null, Locality.AUTRES, Zone.ZCI, TypeBuilding.B1, 320),
                new RentPrice(null, Locality.AUTRES, Zone.ZRA, TypeBuilding.B1, 325),
                new RentPrice(null, Locality.OUAGA, Zone.ZH, TypeBuilding.S1, 610),
                new RentPrice(null, Locality.OUAGA, Zone.ZCI, TypeBuilding.S1, 675),
                new RentPrice(null, Locality.OUAGA, Zone.ZRA, TypeBuilding.S1, 705),
                new RentPrice(null, Locality.BOBO, Zone.ZH, TypeBuilding.S1, 580),
                new RentPrice(null, Locality.BOBO, Zone.ZCI, TypeBuilding.S1, 610),
                new RentPrice(null, Locality.BOBO, Zone.ZRA, TypeBuilding.S1, 640),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZH, TypeBuilding.S1, 550),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZCI, TypeBuilding.S1, 565),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZRA, TypeBuilding.S1, 580),
                new RentPrice(null, Locality.AUTRES, Zone.ZH, TypeBuilding.S1, 520),
                new RentPrice(null, Locality.AUTRES, Zone.ZCI, TypeBuilding.S1, 530),
                new RentPrice(null, Locality.AUTRES, Zone.ZRA, TypeBuilding.S1, 535),
                new RentPrice(null, Locality.OUAGA, Zone.ZH, TypeBuilding.S2, 730),
                new RentPrice(null, Locality.OUAGA, Zone.ZCI, TypeBuilding.S2, 805),
                new RentPrice(null, Locality.OUAGA, Zone.ZRA, TypeBuilding.S2, 840),
                new RentPrice(null, Locality.BOBO, Zone.ZH, TypeBuilding.S2, 695),
                new RentPrice(null, Locality.BOBO, Zone.ZCI, TypeBuilding.S2, 730),
                new RentPrice(null, Locality.BOBO, Zone.ZRA, TypeBuilding.S2, 765),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZH, TypeBuilding.S2, 660),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZCI, TypeBuilding.S2, 680),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZRA, TypeBuilding.S2, 695),
                new RentPrice(null, Locality.AUTRES, Zone.ZH, TypeBuilding.S2, 625),
                new RentPrice(null, Locality.AUTRES, Zone.ZCI, TypeBuilding.S2, 635),
                new RentPrice(null, Locality.AUTRES, Zone.ZRA, TypeBuilding.S2, 645),
                new RentPrice(null, Locality.OUAGA, Zone.ZH, TypeBuilding.S3, 895),
                new RentPrice(null, Locality.OUAGA, Zone.ZCI, TypeBuilding.S3, 985),
                new RentPrice(null, Locality.OUAGA, Zone.ZRA, TypeBuilding.S3, 1030),
                new RentPrice(null, Locality.BOBO, Zone.ZH, TypeBuilding.S3, 850),
                new RentPrice(null, Locality.BOBO, Zone.ZCI, TypeBuilding.S3, 895),
                new RentPrice(null, Locality.BOBO, Zone.ZRA, TypeBuilding.S3, 935),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZH, TypeBuilding.S3, 805),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZCI, TypeBuilding.S3, 830),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZRA, TypeBuilding.S3, 850),
                new RentPrice(null, Locality.AUTRES, Zone.ZH, TypeBuilding.S3, 765),
                new RentPrice(null, Locality.AUTRES, Zone.ZCI, TypeBuilding.S3, 775),
                new RentPrice(null, Locality.AUTRES, Zone.ZRA, TypeBuilding.S3, 785),
                new RentPrice(null, Locality.OUAGA, Zone.ZH, TypeBuilding.S4, 1045),
                new RentPrice(null, Locality.OUAGA, Zone.ZCI, TypeBuilding.S4, 1150),
                new RentPrice(null, Locality.OUAGA, Zone.ZRA, TypeBuilding.S4, 1205),
                new RentPrice(null, Locality.BOBO, Zone.ZH, TypeBuilding.S4, 995),
                new RentPrice(null, Locality.BOBO, Zone.ZCI, TypeBuilding.S4, 1045),
                new RentPrice(null, Locality.BOBO, Zone.ZRA, TypeBuilding.S4, 1095),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZH, TypeBuilding.S4, 945),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZCI, TypeBuilding.S4, 970),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZRA, TypeBuilding.S4, 995),
                new RentPrice(null, Locality.AUTRES, Zone.ZH, TypeBuilding.S4, 900),
                new RentPrice(null, Locality.AUTRES, Zone.ZCI, TypeBuilding.S4, 915),
                new RentPrice(null, Locality.AUTRES, Zone.ZRA, TypeBuilding.S4, 925),
                new RentPrice(null, Locality.OUAGA, Zone.ZH, TypeBuilding.DM, 265),
                new RentPrice(null, Locality.OUAGA, Zone.ZCI, TypeBuilding.DM, 295),
                new RentPrice(null, Locality.OUAGA, Zone.ZRA, TypeBuilding.DM, 305),
                new RentPrice(null, Locality.BOBO, Zone.ZH, TypeBuilding.DM, 250),
                new RentPrice(null, Locality.BOBO, Zone.ZCI, TypeBuilding.DM, 265),
                new RentPrice(null, Locality.BOBO, Zone.ZRA, TypeBuilding.DM, 280),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZH, TypeBuilding.DM, 240),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZCI, TypeBuilding.DM, 245),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZRA, TypeBuilding.DM, 250),
                new RentPrice(null, Locality.AUTRES, Zone.ZH, TypeBuilding.DM, 225),
                new RentPrice(null, Locality.AUTRES, Zone.ZCI, TypeBuilding.DM, 230),
                new RentPrice(null, Locality.AUTRES, Zone.ZRA, TypeBuilding.DM, 235),
                new RentPrice(null, Locality.OUAGA, Zone.ZH, TypeBuilding.D1, 1065),
                new RentPrice(null, Locality.OUAGA, Zone.ZCI, TypeBuilding.D1, 1175),
                new RentPrice(null, Locality.OUAGA, Zone.ZRA, TypeBuilding.D1, 1225),
                new RentPrice(null, Locality.BOBO, Zone.ZH, TypeBuilding.D1, 1010),
                new RentPrice(null, Locality.BOBO, Zone.ZCI, TypeBuilding.D1, 1065),
                new RentPrice(null, Locality.BOBO, Zone.ZRA, TypeBuilding.D1, 1115),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZH, TypeBuilding.D1, 960),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZCI, TypeBuilding.D1, 985),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZRA, TypeBuilding.D1, 1010),
                new RentPrice(null, Locality.AUTRES, Zone.ZH, TypeBuilding.D1, 910),
                new RentPrice(null, Locality.AUTRES, Zone.ZCI, TypeBuilding.D1, 925),
                new RentPrice(null, Locality.AUTRES, Zone.ZRA, TypeBuilding.D1, 935),
                new RentPrice(null, Locality.OUAGA, Zone.ZH, TypeBuilding.D2, 1210),
                new RentPrice(null, Locality.OUAGA, Zone.ZCI, TypeBuilding.D2, 1335),
                new RentPrice(null, Locality.OUAGA, Zone.ZRA, TypeBuilding.D2, 1395),
                new RentPrice(null, Locality.BOBO, Zone.ZH, TypeBuilding.D2, 1150),
                new RentPrice(null, Locality.BOBO, Zone.ZCI, TypeBuilding.D2, 1210),
                new RentPrice(null, Locality.BOBO, Zone.ZRA, TypeBuilding.D2, 1265),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZH, TypeBuilding.D2, 1095),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZCI, TypeBuilding.D2, 1125),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZRA, TypeBuilding.D2, 1150),
                new RentPrice(null, Locality.AUTRES, Zone.ZH, TypeBuilding.D2, 1040),
                new RentPrice(null, Locality.AUTRES, Zone.ZCI, TypeBuilding.D2, 1055),
                new RentPrice(null, Locality.AUTRES, Zone.ZRA, TypeBuilding.D2, 1070),
                new RentPrice(null, Locality.OUAGA, Zone.ZH, TypeBuilding.D3, 1455),
                new RentPrice(null, Locality.OUAGA, Zone.ZCI, TypeBuilding.D3, 1605),
                new RentPrice(null, Locality.OUAGA, Zone.ZRA, TypeBuilding.D3, 1675),
                new RentPrice(null, Locality.BOBO, Zone.ZH, TypeBuilding.D3, 1385),
                new RentPrice(null, Locality.BOBO, Zone.ZCI, TypeBuilding.D3, 1455),
                new RentPrice(null, Locality.BOBO, Zone.ZRA, TypeBuilding.D3, 1525),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZH, TypeBuilding.D3, 1315),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZCI, TypeBuilding.D3, 1350),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZRA, TypeBuilding.D3, 1385),
                new RentPrice(null, Locality.AUTRES, Zone.ZH, TypeBuilding.D3, 1250),
                new RentPrice(null, Locality.AUTRES, Zone.ZCI, TypeBuilding.D3, 1270),
                new RentPrice(null, Locality.AUTRES, Zone.ZRA, TypeBuilding.D3, 1285),
                new RentPrice(null, Locality.OUAGA, Zone.ZH, TypeBuilding.D4, 1695),
                new RentPrice(null, Locality.OUAGA, Zone.ZCI, TypeBuilding.D4, 1865),
                new RentPrice(null, Locality.OUAGA, Zone.ZRA, TypeBuilding.D4, 1950),
                new RentPrice(null, Locality.BOBO, Zone.ZH, TypeBuilding.D4, 1610),
                new RentPrice(null, Locality.BOBO, Zone.ZCI, TypeBuilding.D4, 1695),
                new RentPrice(null, Locality.BOBO, Zone.ZRA, TypeBuilding.D4, 1775),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZH, TypeBuilding.D4, 1530),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZCI, TypeBuilding.D4, 1570),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZRA, TypeBuilding.D4, 1610),
                new RentPrice(null, Locality.AUTRES, Zone.ZH, TypeBuilding.D4, 1455),
                new RentPrice(null, Locality.AUTRES, Zone.ZCI, TypeBuilding.D4, 1475),
                new RentPrice(null, Locality.AUTRES, Zone.ZRA, TypeBuilding.D4, 1495),
                new RentPrice(null, Locality.OUAGA, Zone.ZH, TypeBuilding.N1, 2170),
                new RentPrice(null, Locality.OUAGA, Zone.ZCI, TypeBuilding.N1, 2390),
                new RentPrice(null, Locality.OUAGA, Zone.ZRA, TypeBuilding.N1, 2500),
                new RentPrice(null, Locality.BOBO, Zone.ZH, TypeBuilding.N1, 2065),
                new RentPrice(null, Locality.BOBO, Zone.ZCI, TypeBuilding.N1, 2170),
                new RentPrice(null, Locality.BOBO, Zone.ZRA, TypeBuilding.N1, 2275),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZH, TypeBuilding.N1, 1965),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZCI, TypeBuilding.N1, 2015),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZRA, TypeBuilding.N1, 2065),
                new RentPrice(null, Locality.AUTRES, Zone.ZH, TypeBuilding.N1, 1870),
                new RentPrice(null, Locality.AUTRES, Zone.ZCI, TypeBuilding.N1, 1895),
                new RentPrice(null, Locality.AUTRES, Zone.ZRA, TypeBuilding.N1, 1920),
                new RentPrice(null, Locality.OUAGA, Zone.ZH, TypeBuilding.N2, 2650),
                new RentPrice(null, Locality.OUAGA, Zone.ZCI, TypeBuilding.N2, 2915),
                new RentPrice(null, Locality.OUAGA, Zone.ZRA, TypeBuilding.N2, 3050),
                new RentPrice(null, Locality.BOBO, Zone.ZH, TypeBuilding.N2, 2520),
                new RentPrice(null, Locality.BOBO, Zone.ZCI, TypeBuilding.N2, 2650),
                new RentPrice(null, Locality.BOBO, Zone.ZRA, TypeBuilding.N2, 2775),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZH, TypeBuilding.N2, 2400),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZCI, TypeBuilding.N2, 2460),
                new RentPrice(null, Locality.CHEF_LIEUX, Zone.ZRA, TypeBuilding.N2, 2520),
                new RentPrice(null, Locality.AUTRES, Zone.ZH, TypeBuilding.N2, 2285),
                new RentPrice(null, Locality.AUTRES, Zone.ZCI, TypeBuilding.N2, 2315),
                new RentPrice(null, Locality.AUTRES, Zone.ZRA, TypeBuilding.N2, 2345)
            );

            rentPriceRepository.saveAll(prices);
        }
    }
    
}
