package com.project.ordermakanan.services.specifications;


import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.project.ordermakanan.dto.request.FoodListRequestDto;
import com.project.ordermakanan.models.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodSpecification {
    public static Specification<Food> foodFilter(FoodListRequestDto foodListRequestDto){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();

            if (foodListRequestDto.getFoodName() != null) {
                String foodNameValue = "%" + foodListRequestDto.getFoodName().toLowerCase() + "%";
                Predicate foodNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("foodName")), foodNameValue);
                predicates.add(foodNamePredicate);
            }

            if (foodListRequestDto.getCategoryId() != null) {
                Predicate categoryPredicate = criteriaBuilder.equal(
                    root.get("category").get("categoryId"),
                    foodListRequestDto.getCategoryId());
                predicates.add(categoryPredicate);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

        };
    }
}
