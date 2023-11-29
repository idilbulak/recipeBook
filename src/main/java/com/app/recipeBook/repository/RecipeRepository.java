package com.app.recipeBook.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.recipeBook.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findById(Long id);
    Optional<Recipe> findByName(String name);
    Boolean existsByIngredients_Id(Long id);

}
