package com.app.recipeBook.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.app.recipeBook.model.Recipe;
import com.app.recipeBook.service.RecipeBookService;

@RestController
public class RecipeBookController {

    private final RecipeBookService recipeBookService;

    public RecipeBookController(RecipeBookService recipeBookService) {
        this.recipeBookService = recipeBookService;
    }

    @GetMapping("/")
    public String recipeBook() {
        return "this is a recipeBook";
    }

    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes() {
        return this.recipeBookService.getAllRecipes();
    }

    @PostMapping("/recipes")
    public void addRecipe(@RequestBody Recipe recipe) {
        this.recipeBookService.addRecipe(recipe);
    }

    @PutMapping("/recipes/{id}")
    public void updateRecipe(@PathVariable Long id, @RequestBody Recipe newRecipe) {
        this.recipeBookService.updateRecipe(id, newRecipe);
    }

    @DeleteMapping("/recipes/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        this.recipeBookService.removeRecipe(id);
    }

}
