package com.project.ordermakanan.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AddCartResponse {
    private long total;
    private FoodListResponseDto data;
    private String message;
    private int statusCode;
    private String status;
}