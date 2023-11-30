package com.app.recipeBook.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "isVegetarian")
    private Boolean isVegetarian;

    @Column(name = "numberOfServings")
    private Integer numberOfServings;

    @Column(name = "instructions")
    private String instructions;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
        name = "recipe_ingredient",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private Set<Ingredient> ingredients = new HashSet<>();
    
    // Default constructor
    public Recipe() {
    }

    // Constructor with parameters
    public Recipe(String name, Boolean isVegetarian, Integer numberOfServings, String instructions) {
        this.name = name;
        this.isVegetarian = isVegetarian;
        this.numberOfServings = numberOfServings;
        this.instructions = instructions;
        this.ingredients = new HashSet<>();
    }

    // Getter and Setter for id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for isVegetarian
    public Boolean getIsVegetarian() {
        return isVegetarian;
    }

    public void setIsVegetarian(Boolean isVegetarian) {
        this.isVegetarian = isVegetarian;
    }

    // Getter and Setter for numberOfServings
    public Integer getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(Integer numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    // Getter and Setter for instructions
    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    // Getter and Setter for ingredients
    public Set<Ingredient> getIngredients() {
        return ingredients;
    }
    
    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

}
