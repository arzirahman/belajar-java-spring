package com.project.ordermakanan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.project.ordermakanan.models.Food;

@Repository
@EnableJpaRepositories
public interface FoodRepository extends JpaRepository<Food, Integer>, JpaSpecificationExecutor<Food> {

}
