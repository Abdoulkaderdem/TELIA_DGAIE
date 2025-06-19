package com.telia.Lease_management.utils;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.telia.Lease_management.services.contract.ContractService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@EnableScheduling
@Service
public class Scheduler {

    private ContractService contractService;

    
    //Run every day at midnight
    @Scheduled(cron="0 0 0 * * ?") 
    public void scheduleContractCheck() {
        contractService.checkContractStatus();
    }
    
}
