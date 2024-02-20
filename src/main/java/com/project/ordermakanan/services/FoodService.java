package com.project.ordermakanan.services;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.ordermakanan.dto.response.FoodListResponseDto;
import com.project.ordermakanan.models.Cart;
import com.project.ordermakanan.models.Food;
import com.project.ordermakanan.dto.request.FoodListRequestDto;
import com.project.ordermakanan.dto.response.FoodCategoryDto;
import com.project.ordermakanan.dto.response.FoodListResponse;
import com.project.ordermakanan.repositories.CartRepository;
import com.project.ordermakanan.repositories.FoodRepository;
import com.project.ordermakanan.services.specifications.FoodSpecification;
import com.project.ordermakanan.utils.jwt.JwtUtil;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MessageSource messageSource;

    public ResponseEntity<FoodListResponse> getFoods(FoodListRequestDto foodListRequestDto, Pageable page){
        Specification<Food> foodSpec = FoodSpecification.foodFilter(foodListRequestDto);

        Page<Food> foods = foodRepository.findAll(foodSpec, page);

        List<FoodListResponseDto> foodsDto = foods.stream().map(food -> 
            new FoodListResponseDto(food.getFoodId(), 
                new FoodCategoryDto(food.getCategory().getCategoryId(), food.getCategory().getCategoryName()), 
                food.getFoodName(), 
                food.getPrice(), 
                food.getImageFilename(),
                getIsCart(food.getFoodId(), JwtUtil.getCurrentUser().getUserId())
            )
        ).collect(Collectors.toList());

        long totalData = foodRepository.count(foodSpec);

        String message = messageSource.getMessage("get.food.success", null, Locale.getDefault());

        return ResponseEntity
            .ok()
            .body(new FoodListResponse(totalData, foodsDto, message, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
    }

    private Boolean getIsCart(Integer foodId, Integer userId) {
        Optional<Cart> cart = cartRepository.findCartByFoodAndUser(foodId, userId);
        if (cart.isPresent()) {
            return true;
        }
        return false;

    }
}
