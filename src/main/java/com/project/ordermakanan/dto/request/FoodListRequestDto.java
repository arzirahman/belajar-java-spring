package com.project.ordermakanan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodListRequestDto {
    private String foodName;
    private Integer categoryId;
    private String sortBy;
    private int pageSize;
    private int pageNumber;
}
