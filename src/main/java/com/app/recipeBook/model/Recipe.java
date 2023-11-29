package com.app.recipeBook.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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

    @Column(name = "isVegatarian")
    private Boolean isVegetarian;

    @Column(name = "numberOfservings")
    private Integer numberOfservings;

    @Column(name = "instructions")
    private String instructions;

    // Default constructor
    public Recipe() {
    }

    // Constructor with parameters
    public Recipe(String name, Boolean isVegetarian, Integer numberOfservings, String instructions) {
        this.name = name;
        this.isVegetarian = isVegetarian;
        this.numberOfservings = numberOfservings;
        this.instructions = instructions;
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

    // Getter and Setter for numberOfservings
    public Integer getNumberOfServings() {
        return numberOfservings;
    }

    public void setNumberOfServings(Integer numberOfservings) {
        this.numberOfservings = numberOfservings;
    }

    // Getter and Setter for instructions
    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

}
