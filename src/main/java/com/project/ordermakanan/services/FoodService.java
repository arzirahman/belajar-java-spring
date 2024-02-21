package com.project.ordermakanan.services;

import java.text.MessageFormat;
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
import com.project.ordermakanan.models.FavoriteFood;
import com.project.ordermakanan.models.Food;
import com.project.ordermakanan.models.User;
import com.project.ordermakanan.dto.request.AddCartRequest;
import com.project.ordermakanan.dto.request.FoodListRequestDto;
import com.project.ordermakanan.dto.response.CartResponse;
import com.project.ordermakanan.dto.response.FoodCategoryDto;
import com.project.ordermakanan.dto.response.FoodListResponse;
import com.project.ordermakanan.repositories.CartRepository;
import com.project.ordermakanan.repositories.FavoriteFoodRepository;
import com.project.ordermakanan.repositories.FoodRepository;
import com.project.ordermakanan.repositories.UserRepository;
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

    @Autowired
    private FavoriteFoodRepository favoriteFoodRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<FoodListResponse> getFoods(FoodListRequestDto foodListRequestDto, Pageable page){
        Specification<Food> foodSpec = FoodSpecification.foodFilter(foodListRequestDto);

        Page<Food> foods = foodRepository.findAll(foodSpec, page);

        int userId = JwtUtil.getCurrentUser().getUserId();

        List<FoodListResponseDto> foodsDto = foods.stream().map(food -> 
            new FoodListResponseDto(food.getFoodId(), 
                new FoodCategoryDto(food.getCategory().getCategoryId(), food.getCategory().getCategoryName()), 
                food.getFoodName(), 
                food.getPrice(), 
                food.getImageFilename(),
                getIsCart(food.getFoodId(), userId),
                getIsFavorite(food.getFoodId(), userId)
            )
        ).collect(Collectors.toList());

        long totalData = foods.getTotalElements();

        String message = messageSource.getMessage("get.food.success", null, Locale.getDefault());

        return ResponseEntity
            .ok()
            .body(new FoodListResponse(totalData, foodsDto, message, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
    }

    public ResponseEntity<CartResponse> toggleFavorite(Integer foodId){
        if(foodId == null){
            String message = messageSource.getMessage("foodId.required", null, Locale.getDefault());
            return ResponseEntity
                .badRequest()
                .body(new CartResponse(0, null, message, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase()));
        }

        int userId = JwtUtil.getCurrentUser().getUserId();

        Optional<Food> optionalFood = foodRepository.findById(foodId);
        if (!optionalFood.isPresent()) {
            String errorMessage = messageSource.getMessage("food.not.found", null, Locale.getDefault());
            return ResponseEntity
                .badRequest()
                .body(new CartResponse(0, null, errorMessage, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase()));
        }

        Optional<FavoriteFood> optionalFavoriteFood = favoriteFoodRepository.findFavoriteFoodByFoodAndUser(foodId, userId);
        if (optionalFavoriteFood.isPresent()){
            FavoriteFood favoriteFood = optionalFavoriteFood.get();
            favoriteFood.setIsFavorite(!favoriteFood.getIsFavorite());
            favoriteFoodRepository.save(favoriteFood);
        } else {
            FavoriteFood newFavoriteFood = FavoriteFood.builder()
                .food(foodRepository.findById(foodId).orElseThrow())
                .user(userRepository.findById(userId).orElseThrow())
                .isFavorite(true)
                .build();
            favoriteFoodRepository.save(newFavoriteFood);
        }

        String message = messageSource.getMessage(getIsFavorite(foodId, userId) ? "add.favorite" : "delete.favorite", null, Locale.getDefault());
        String formatMessage = MessageFormat.format(message, optionalFood.get().getFoodName());
        return ResponseEntity
        .ok()
        .body(new CartResponse(1, null, formatMessage, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
    }
    
    public ResponseEntity<CartResponse> addCart(AddCartRequest request){
        if(request.getFoodId() == null){
            String message = messageSource.getMessage("foodId.required", null, Locale.getDefault());
            return ResponseEntity
                .badRequest()
                .body(new CartResponse(0, null, message, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase()));
        }

        int userId = JwtUtil.getCurrentUser().getUserId();
        int foodId = request.getFoodId();

        Optional<Food> optionalFood = foodRepository.findById(foodId);
        if (!optionalFood.isPresent()) {
            String errorMessage = messageSource.getMessage("food.not.found", null, Locale.getDefault());
            return ResponseEntity
                .badRequest()
                .body(new CartResponse(0, null, errorMessage, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase()));
        }

        Food food = optionalFood.get();

        Cart cart = Cart.builder().food(food).user(User.builder().userId(userId).build()).build();

        cartRepository.save(cart);

        FoodListResponseDto foodDto = new FoodListResponseDto(food.getFoodId(), 
            new FoodCategoryDto(food.getCategory().getCategoryId(), food.getCategory().getCategoryName()), 
            food.getFoodName(), 
            food.getPrice(), 
            food.getImageFilename(),
            getIsCart(food.getFoodId(), JwtUtil.getCurrentUser().getUserId()),
            getIsFavorite(foodId, userId)
        );

        String message = messageSource.getMessage("add.cart.success", null, Locale.getDefault());
        String formatMessage = MessageFormat.format(message, food.getFoodName());
        return ResponseEntity
        .ok()
        .body(new CartResponse(1, foodDto, formatMessage, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
    }

    public ResponseEntity<CartResponse> deleteCart(Integer foodId){
        if(foodId == null){
            String message = messageSource.getMessage("foodId.required", null, Locale.getDefault());
            return ResponseEntity
                .badRequest()
                .body(new CartResponse(0, null, message, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase()));
        }

        int userId = JwtUtil.getCurrentUser().getUserId();

        Optional<Cart> optionalCart = cartRepository.findCartByFoodAndUser(foodId, userId);
        if (!optionalCart.isPresent()) {
            String errorMessage = messageSource.getMessage("food.not.found", null, Locale.getDefault());
            return ResponseEntity
                .badRequest()
                .body(new CartResponse(0, null, errorMessage, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase()));
        }

        cartRepository.delete(optionalCart.get());

        Food food = optionalCart.get().getFood();
        FoodListResponseDto foodDto = new FoodListResponseDto(food.getFoodId(), 
            new FoodCategoryDto(food.getCategory().getCategoryId(), food.getCategory().getCategoryName()), 
            food.getFoodName(), 
            food.getPrice(), 
            food.getImageFilename(),
            getIsCart(food.getFoodId(), JwtUtil.getCurrentUser().getUserId()),
            getIsFavorite(foodId, userId)
        );

        String message = messageSource.getMessage("delete.cart.success", null, Locale.getDefault());
        String formatMessage = MessageFormat.format(message, food.getFoodName());
        return ResponseEntity
            .ok()
            .body(new CartResponse(0, foodDto, formatMessage, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
    }

    private Boolean getIsCart(Integer foodId, Integer userId) {
        Optional<Cart> cart = cartRepository.findCartByFoodAndUser(foodId, userId);
        if (cart.isPresent()) {
            return true;
        }
        return false;
    }

    private Boolean getIsFavorite(Integer foodId, Integer userId){
        Optional<FavoriteFood> favoriteFood = favoriteFoodRepository.findFavoriteFoodByFoodAndUser(foodId, userId);
        if (favoriteFood.isPresent()){
            return favoriteFood.get().getIsFavorite();
        } else return false;
    }
}
