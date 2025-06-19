package com.telia.Lease_management.dto.responses;

import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class AuthResponse {
    private int status;
    private String answer;

    public AuthResponse(int status, String answer) {
        this.status = status;
        this.answer = answer;
    }

    public int getStatus() {
        return status;
    }

    public String getAnswer() {
        return answer;
    }
}
