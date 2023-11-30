package com.app.recipeBook.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.app.recipeBook.model.Ingredient;
import com.app.recipeBook.model.Recipe;
import com.app.recipeBook.repository.RecipeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import com.app.recipeBook.repository.IngredientRepository;

@Service
public class RecipeBookService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    private EntityManager entityManager;

    /**
     * Constructs a new RecipeBookService with the given repositories.
     *
     * @param recipeRepository The repository for managing recipe data.
     * @param ingredientRepository The repository for managing ingredient data.
     */
    @Autowired
    public RecipeBookService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;

    }

    /**
     * Retrieves a list of all recipes.
     *
     * @return A list of all recipes.
     */
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    /**
     * Retrieves a list of all ingredients.
     *
     * @return A list of all ingredients.
     */
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }
    
    /**
     * Adds a new recipe to the database.
     * 
     * This method performs several checks before adding the recipe:
     * 1. It ensures that all required fields of the Recipe object are not null.
     * 2. It checks if a recipe with the same name already exists in the database.
     * 3. For each ingredient in the recipe, it checks if the ingredient already exists in the database.
     *    - If it does, it uses the existing ingredient.
     *    - If it doesn't, it saves the new ingredient.
     * 4. Finally, it saves the recipe to the database.
     * 
     * @param recipe The Recipe object to be added.
     * @return ResponseEntity with appropriate HTTP status and message.
     * - HttpStatus.CREATED (201) if the recipe is successfully created.
     * - HttpStatus.BAD_REQUEST (400) if the recipe parameters are invalid.
     * - HttpStatus.CONFLICT (409) if a recipe with the same name already exists.
     * - HttpStatus.INTERNAL_SERVER_ERROR (500) for any other errors.
     */
    public ResponseEntity<?> addRecipe(Recipe recipe) {
        try {
            if (recipe.getName() == null || recipe.getIsVegetarian() == null || recipe.getNumberOfServings() == null || recipe.getInstructions() == null || recipe.getIngredients() == null) {
                throw new IllegalArgumentException("Recipe parameters cannot be null");
            }
            
            Optional<Recipe> existingRecipe = recipeRepository.findByName(recipe.getName());
            if (existingRecipe.isPresent()) {
                throw new IllegalStateException("Recipe with the same name already exists");
            }
            
            for (Ingredient ingredient : recipe.getIngredients()) {
                Optional<Ingredient> existingIngredient = ingredientRepository.findByName(ingredient.getName());
                if (!existingIngredient.isPresent()) {
                    ingredientRepository.save(ingredient);
                } else {
                    ingredient.setId(existingIngredient.get().getId());
                }
            }
            recipeRepository.save(recipe);
            return ResponseEntity.status(HttpStatus.CREATED).body("201 CREATED");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid recipe parameters");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Recipe with the same name already exists");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    /**
     * Updates a recipe that is in the database by its ID.
     * 
     * Updates an existing recipe with new information. This method allows changing the recipe's
     * name, whether it is vegetarian, the number of servings, instructions, and ingredients.
     * If an ingredient is new, it gets added to the ingredients table. Ingredients no longer
     * associated with any recipe are removed from the ingredients table.
     *
     * @param id The ID of the recipe to be updated.
     * @param newRecipe The new recipe data to be applied. This object can contain partial
     *                  information (e.g., only the fields that need to be updated).
     * @return ResponseEntity with appropriate HTTP status and message.
     * - HttpStatus.OK (200) if the recipe is successfully updated.
     * - HttpStatus.NO_CONTENT(204) if the recipe doesn't exist.
     * - HttpStatus.INTERNAL_SERVER_ERROR (500) for any other errors.
     */
    public ResponseEntity<?> updateRecipe(Long id, Recipe newRecipe) {
        try {
            Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
            if (!optionalRecipe.isPresent()) {
                throw new IllegalStateException("Recipe doesn't exist");
            }
            Recipe existingRecipe = optionalRecipe.get();
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
                    Ingredient existingIngredient = ingredientRepository.findByName(ingredient.getName())
                        .orElseGet(() -> ingredientRepository.save(ingredient));
                    updatedIngredients.add(existingIngredient);
                }
                existingRecipe.setIngredients(updatedIngredients);
            }
    
            recipeRepository.save(existingRecipe);
    
            oldIngredients.removeAll(existingRecipe.getIngredients());
            for (Ingredient removedIngredient : oldIngredients) {
                if (!recipeRepository.existsByIngredients_Id(removedIngredient.getId())) {
                    ingredientRepository.delete(removedIngredient);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body("200 OK");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipe doesn't exist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    /**
     * Removes a recipe from the database by its ID.
     *
     * This method performs the following steps:
     * 1. Checks if the recipe with the specified ID exists. If it doesn't, throws an IllegalStateException.
     * 2. Retrieves the recipe and creates a copy of its associated ingredients for later checks.
     * 3. Clears the association of ingredients with the recipe and saves the updated recipe.
     * 4. Deletes the recipe from the database.
     * 5. Iterates over the previously associated ingredients. For each ingredient, it checks if it's still
     *    used in any other recipe.
     *    - If the ingredient is no longer used in any recipe, it gets deleted from the ingredients table.
     *
     * @param id The ID of the recipe to be removed.
     * @return ResponseEntity<?> indicating the outcome of the operation. It returns:
     *    - HttpStatus.NO_CONTENT (204 NO CONTENT) if the recipe is successfully removed.
     *    - HttpStatus.NOT_FOUND if the recipe with the specified ID does not exist.
     *    - HttpStatus.INTERNAL_SERVER_ERROR for any other errors encountered during the operation.
     */
    public ResponseEntity<?> removeRecipe(Long id) {
        try {
            Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
            if (!optionalRecipe.isPresent()) {
                throw new IllegalStateException("Recipe doesn't exist");
            }
            Recipe existingRecipe = optionalRecipe.get();
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
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("204 NO CONTENT");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipe doesn't exist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    /**
     * Filters recipes based on various criteria.
     *
     * This method allows filtering recipes based on a combination of criteria including
     * number of servings, whether the recipe is vegetarian, specific keywords in instructions,
     * and presence or absence of certain ingredients.
     *
     * The method dynamically constructs a JPQL query based on the provided criteria.
     * The query:
     * - Joins with the ingredients table if ingredient-related criteria are specified.
     * - Supports filtering by number of servings and vegetarian status.
     * - Allows filtering recipes that contain or do not contain specific keywords in their instructions.
     * - Enables filtering by the presence or absence of certain ingredients.
     * - Utilizes a subquery to handle recipes that do not contain specified ingredients.
     *
     * The method returns a list of filtered recipes encapsulated in a ResponseEntity.
     * If no recipes are found matching the criteria, it returns a 404 Not Found response.
     * In case of an error, it catches exceptions and returns an appropriate HTTP response.
     *
     * @param criteria A Map of filter criteria where the key is the filter type (e.g., "numberOfServings",
     *                 "isVegetarian", "instructionsContaining") and the value is the filter value.
     * @return ResponseEntity<?> indicating the outcome of the operation. It returns:
     *    - HttpStatus.NO_CONTENT (200 NO OK) if the recipes filtered.
     *    - HttpStatus.NOT_FOUND if no recipe exists.
     *    - HttpStatus.INTERNAL_SERVER_ERROR for any other errors encountered during the operation.
     */
    public ResponseEntity<?> filterBy(JsonNode criteriaJsonNode) {
        try {
            Map<String, Object> criteria = convertJsonNodeToMap(criteriaJsonNode);

            StringBuilder queryString = new StringBuilder("SELECT r FROM Recipe r ");
        
            if (criteria.containsKey("ingredientsContain") || criteria.containsKey("ingredientsNotContain")) {
                queryString.append("JOIN r.ingredients i ");
            }
        
            queryString.append("WHERE ");
        
            if (criteria.containsKey("numberOfServings")) {
                queryString.append("r.numberOfServings = :numberOfServings AND ");
            }
            if (criteria.containsKey("isVegetarian")) {
                queryString.append("r.isVegetarian = :isVegetarian AND ");
            }
            if (criteria.containsKey("instructionsContaining")) {
                queryString.append("LOWER(r.instructions) LIKE LOWER(CONCAT('%', :instructionsContaining, '%')) AND ");
            }
            if (criteria.containsKey("instructionsNotContaining")) {
                queryString.append("LOWER(r.instructions) NOT LIKE LOWER(CONCAT('%', :instructionsNotContaining, '%')) AND ");
            }
            if (criteria.containsKey("ingredientsContain")) {
                queryString.append("LOWER(i.name) LIKE LOWER(CONCAT('%', :ingredientsContain, '%')) AND ");
            }
            if (criteria.containsKey("ingredientsNotContain")) {
                queryString.append("r.id NOT IN (SELECT r2.id FROM Recipe r2 JOIN r2.ingredients i2 WHERE LOWER(i2.name) LIKE LOWER(CONCAT('%', :ingredientsNotContain, '%'))) AND ");
            }
        
            if (queryString.toString().endsWith(" AND ")) {
                queryString = new StringBuilder(queryString.substring(0, queryString.length() - 5));
            }
            
            Query query = entityManager.createQuery(queryString.toString(), Recipe.class);
        
            criteria.forEach((key, value) -> {
                if ("isVegetarian".equals(key)) {
                    query.setParameter("isVegetarian", criteria.get("isVegetarian") == "true" ? true : false);
                } else {
                    query.setParameter(key, value);
                }
            });
        
            List<Recipe> recipes = query.getResultList();
            if (recipes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No recipes found matching the criteria");
            }
            return ResponseEntity.status(HttpStatus.OK).body(recipes);
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    private Map<String, Object> convertJsonNodeToMap(JsonNode jsonNode) {
        Map<String, Object> map = new HashMap<>();
        jsonNode.fields().forEachRemaining(entry -> {
            map.put(entry.getKey(), entry.getValue().asText());
        });
        return map;
    }
}
