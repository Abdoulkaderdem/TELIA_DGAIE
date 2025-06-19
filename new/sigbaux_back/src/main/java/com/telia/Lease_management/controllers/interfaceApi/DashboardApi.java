package com.telia.Lease_management.controllers.interfaceApi;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.telia.Lease_management.dto.responses.DashboardCountsResponse;

@CrossOrigin
@RequestMapping("/dashboard")
public interface DashboardApi {
    
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DashboardCountsResponse> getDashboardCounts();
}
