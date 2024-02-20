package com.project.ordermakanan.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.project.ordermakanan.models.FavoriteFood;

@Repository
@EnableJpaRepositories
public interface FavoriteFoodRepository extends JpaRepository<FavoriteFood, Integer> {
    @Query("SELECT f FROM FavoriteFood f WHERE f.user.userId = :userId AND f.food.foodId = :foodId")
    Optional<FavoriteFood> findFavoriteFoodByFoodAndUser(int foodId, int userId);
}