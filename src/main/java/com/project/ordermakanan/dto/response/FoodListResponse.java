package com.project.ordermakanan.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FoodListResponse {
    private long total;
    private List<FoodListResponseDto> data;
    private String message;
    private int statusCode;
    private String status;
}