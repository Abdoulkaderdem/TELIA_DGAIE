package com.telia.Lease_management.dto.ifuDto;

import lombok.Data;

@Data
public class AuthResponse {
    private String TOKEN;
    private ResponseDetails RESPONSE_DETAILS;

    @Data
    public static class ResponseDetails {
        private String MESSAGE;
        private String ERREUR;
    }
}
