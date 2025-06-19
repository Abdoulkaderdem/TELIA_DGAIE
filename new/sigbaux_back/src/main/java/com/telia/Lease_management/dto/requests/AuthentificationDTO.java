package com.telia.Lease_management.dto.requests;

public class AuthentificationDTO {
    private String username;
    private String password;

    public AuthentificationDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Vous pouvez également ajouter equals(), hashCode() et toString() si nécessaire
}
