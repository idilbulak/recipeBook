package com.app.recipeBook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.recipeBook.model.Ingredient;
import com.app.recipeBook.model.Recipe;
import com.app.recipeBook.model.ApiResponse;
import com.app.recipeBook.service.RecipeBookService;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class RecipeBookController {

    private final RecipeBookService recipeBookService;

    public RecipeBookController(RecipeBookService recipeBookService) {
        this.recipeBookService = recipeBookService;
    }

    /**
     * Retrieves a list of all recipes available in the recipe book.
     * 
     * @return A ResponseEntity with the list of Recipe objects formatted as JSON.
     */
    @GetMapping("/recipes")
    public ResponseEntity<?> getAllRecipes() {
        List<Recipe> recipes = this.recipeBookService.getAllRecipes();
        if (recipes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("404 NOT_FOUND"));

        }
        return ResponseEntity.status(HttpStatus.OK).body(recipes);
    }

    /**
     * Retrieves a list of all ingredients used across various recipes.
     * 
     * @return A ResponseEntity with the list of Ingredient objects formatted as JSON.
     */
    @GetMapping("/ingredients")
    public ResponseEntity<?> getAllIngredients() {
        List<Ingredient> ingredients = this.recipeBookService.getAllIngredients();
        if (ingredients.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("404 NOT_FOUND"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ingredients);
    }

    /**
     * Filters recipes based on various criteria provided in JSON format.
     * 
     * @param criteriaJsonNode The JSON object containing filtering criteria.
     * @return A ResponseEntity containing the filtered list of recipes or an error message.
     */
    @PostMapping("/recipes/filter")
    public ResponseEntity<?> filterBy(@RequestBody JsonNode criteriaJsonNode) {
        return recipeBookService.filterBy(criteriaJsonNode);
    }

    /**
     * Adds a new recipe to the recipe book.
     * 
     * @param recipe The Recipe object to be added.
     * @return A ResponseEntity indicating the result of the operation (success or error).
     */
    @PostMapping("/recipes")
    public ResponseEntity<?> addRecipe(@RequestBody Recipe recipe) {
        return this.recipeBookService.addRecipe(recipe);
    }

    /**
     * Updates an existing recipe identified by its ID.
     * 
     * @param id The ID of the recipe to be updated.
     * @param newRecipe The Recipe object containing the updated recipe data.
     * @return A ResponseEntity indicating the result of the update operation (success, not found, or error).
     */
    @PutMapping("/recipes/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable Long id, @RequestBody Recipe newRecipe) {
        return this.recipeBookService.updateRecipe(id, newRecipe);
    }

    /**
     * Deletes a recipe identified by its ID.
     * 
     * @param id The ID of the recipe to be deleted.
     * @return A ResponseEntity indicating the result of the deletion (success, not found, or error).
     */
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        return this.recipeBookService.removeRecipe(id);
    }

    /**
     * Catch-all mapping for any unspecified routes.
     * 
     * @param request The HTTP request.
     * @return A ResponseEntity with a 404 Not Found status and a message.
     */
    @RequestMapping(value = "/**")
    public ResponseEntity<?> handleUnmappedRequest(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("404 NOT_FOUND"));
    }
}
