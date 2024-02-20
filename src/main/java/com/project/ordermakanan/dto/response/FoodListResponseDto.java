package com.project.ordermakanan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodListResponseDto {
    private int foodId;

    private FoodCategoryDto categories;

    private String nama_makanan;
    
    private int harga;

    private String image_filename;

    private Boolean is_cart;

    private Boolean is_favorite;
}
