package com.app.recipeBook.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.recipeBook.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findById(Long id);
    Optional<Recipe> findByName(String name);
    Boolean existsByIngredients_Id(Long id);
    // List<Recipe> findByNumberOfServings(Integer numberOfServings);
    // List<Recipe> findByIsVegetarian(Boolean isVegetarian);
}
