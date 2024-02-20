package com.project.ordermakanan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ordermakanan.dto.request.FoodListRequestDto;
import com.project.ordermakanan.dto.request.PageRequest;
import com.project.ordermakanan.dto.response.FoodListResponse;
import com.project.ordermakanan.services.FoodService;

@RestController
@RequestMapping("/food-order")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @GetMapping("/foods")
    public ResponseEntity<FoodListResponse> getUsers(
        @ModelAttribute FoodListRequestDto foodListRequestDto,
        PageRequest pageRequest
    ){
        return foodService.getFoods(foodListRequestDto, pageRequest.getPage());
    }
}
