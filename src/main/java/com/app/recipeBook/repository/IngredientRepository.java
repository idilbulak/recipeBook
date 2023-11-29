package com.app.recipeBook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.recipeBook.model.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findById(Long id);
    Optional<Ingredient> findByName(String name);
}
