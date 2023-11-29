package com.app.recipeBook.service;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.recipeBook.model.Recipe;
import com.app.recipeBook.repository.RecipeRepository;

@Service
public class RecipeBookService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeBookService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }
    
    public void addRecipe(Recipe recipe) {
        if (recipe.getName() == null || recipe.getIsVegetarian() == null || recipe.getNumberOfServings() == null || recipe.getInstructions() == null) {
            throw new IllegalArgumentException("Recipe parameters cannot be null");
        }

        Optional<Recipe> existingRecipe = recipeRepository.findByName(recipe.getName());
        if (existingRecipe.isPresent()) {
            throw new IllegalStateException("Recipe with the same name already exists");
        }

        recipeRepository.save(recipe);
    }

    public void updateRecipe(Long id, Recipe newRecipe) {
        Recipe existingRecipe = recipeRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Recipe doesn't exist"));

        if (newRecipe.getName() != null && !newRecipe.getName().isEmpty()) {
            existingRecipe.setName(newRecipe.getName());
        }

        if (newRecipe.getIsVegetarian() != null) {
            existingRecipe.setIsVegetarian(newRecipe.getIsVegetarian());
        }

        if (newRecipe.getNumberOfServings() != null) {
            existingRecipe.setNumberOfServings(newRecipe.getNumberOfServings());
        }

        if (newRecipe.getInstructions() != null && !newRecipe.getInstructions().isEmpty()) {
            existingRecipe.setInstructions(newRecipe.getInstructions());
        }

        recipeRepository.save(existingRecipe);
    }

    public void removeRecipe(Long id) {
        Optional<Recipe> existingRecipe = recipeRepository.findById(id);
        if (!existingRecipe.isPresent()) {
            throw new IllegalStateException("Recipe doesn't exists");
        }

        recipeRepository.delete(existingRecipe.get());
    }


}
