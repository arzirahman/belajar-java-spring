package com.project.ordermakanan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LoginResponse {
    private UserData data;
    private String message;
    private int statusCode;
    private String status;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserData {
        private int id;
        private String token;
        private String type;
        private String username;
    }
}