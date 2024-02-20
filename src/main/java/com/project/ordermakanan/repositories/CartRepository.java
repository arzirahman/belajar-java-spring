package com.project.ordermakanan.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.ordermakanan.models.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Query("SELECT c FROM Cart c WHERE c.user.userId = :userId AND c.food.foodId = :foodId")
    Optional<Cart> findCartByFoodAndUser(int foodId, int userId);
}