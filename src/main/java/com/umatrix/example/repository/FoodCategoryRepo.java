package com.umatrix.example.repository;
import com.umatrix.example.models.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FoodCategoryRepo extends JpaRepository<FoodCategory, Long> {
    boolean existsByName(String username);

}
