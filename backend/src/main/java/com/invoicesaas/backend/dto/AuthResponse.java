package com.invoicesaas.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String tokenType;
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private UUID workspaceId;
    private String workspaceName;
}