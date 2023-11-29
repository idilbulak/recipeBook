package com.app.recipeBook.service;

import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.recipeBook.model.Ingredient;
import com.app.recipeBook.model.Recipe;
import com.app.recipeBook.repository.RecipeRepository;
import com.app.recipeBook.repository.IngredientRepository;

@Service
public class RecipeBookService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;


    @Autowired
    public RecipeBookService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;

    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }
    
    public void addRecipe(Recipe recipe) {
        if (recipe.getName() == null || recipe.getIsVegetarian() == null || recipe.getNumberOfServings() == null || recipe.getInstructions() == null || recipe.getIngredients() == null) {
            throw new IllegalArgumentException("Recipe parameters cannot be null");
        }

        Optional<Recipe> existingRecipe = recipeRepository.findByName(recipe.getName());
        if (existingRecipe.isPresent()) {
            throw new IllegalStateException("Recipe with the same name already exists");
        }

        for (Ingredient ingredient : recipe.getIngredients()) {
            // Check if ingredient already exists, if not, save it
            Optional<Ingredient> existingIngredient = ingredientRepository.findByName(ingredient.getName());
            if (!existingIngredient.isPresent()) {
                ingredientRepository.save(ingredient);
            } else {
                // Update the ingredient with existing entity
                ingredient.setId(existingIngredient.get().getId());
            }
        }

        recipeRepository.save(recipe);
    }

    public void updateRecipe(Long id, Recipe newRecipe) {
        Recipe existingRecipe = recipeRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Recipe doesn't exist"));

        Set<Ingredient> oldIngredients = new HashSet<>(existingRecipe.getIngredients());

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

        if (newRecipe.getIngredients() != null) {
            Set<Ingredient> updatedIngredients = new HashSet<>();
            for (Ingredient ingredient : newRecipe.getIngredients()) {
                // Check if the ingredient already exists
                Ingredient existingIngredient = ingredientRepository.findByName(ingredient.getName())
                    .orElseGet(() -> ingredientRepository.save(ingredient)); // Save new ingredient if it doesn't exist
                updatedIngredients.add(existingIngredient);
            }
            existingRecipe.setIngredients(updatedIngredients); // Update the recipe's ingredients
        }

        recipeRepository.save(existingRecipe);

        // Check and remove ingredients that are no longer used
        oldIngredients.removeAll(existingRecipe.getIngredients()); // Remove the ingredients that are still used
        for (Ingredient removedIngredient : oldIngredients) {
            if (!recipeRepository.existsByIngredients_Id(removedIngredient.getId())) {
                ingredientRepository.delete(removedIngredient);
            }
        }
    }

    public void removeRecipe(Long id) {
        // Optional<Recipe> existingRecipe = recipeRepository.findById(id);
        // if (!existingRecipe.isPresent()) {
        //     throw new IllegalStateException("Recipe doesn't exists");
        // }

        // recipeRepository.delete(existingRecipe.get());
        Recipe existingRecipe = recipeRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("Recipe doesn't exist"));

        // Get a copy of the ingredients to check later
        Set<Ingredient> ingredientsToCheck = new HashSet<>(existingRecipe.getIngredients());

        // Disassociate ingredients from the recipe
        existingRecipe.getIngredients().clear();
        recipeRepository.save(existingRecipe);

        // Delete the recipe
        recipeRepository.delete(existingRecipe);

        // Check each ingredient if it's no longer associated with any recipe
        for (Ingredient ingredient : ingredientsToCheck) {
            boolean isIngredientUsed = recipeRepository.existsByIngredients_Id(ingredient.getId());
            if (!isIngredientUsed) {
                // Ingredient is not used in any other recipe, so it can be safely deleted
                ingredientRepository.delete(ingredient);
            }
        }
    }


}
