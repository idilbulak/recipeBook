# RecipeBook Project

## Introduction
This project is a Recipe Book application backend developed in Java using Spring Boot, and it utilizes Docker for containerization. It provides a platform to manage and retrieve recipes recipes along with their ingredients.

## Prerequisites
- Docker and Docker Compose should be installed on your machine.

## Getting Started
Build the Project: make
Start the Project: make up
Stop the Project: make down
Rebuild the Project: make re
Clean Up: make clean

## Project Overview

### Application - RecipeBook
The RecipeBook application runs inside a Docker container, accessible through localhost:8080. This container is named recipebook.

In this application, users can add, update, and delete recipes. Each recipe includes details such as name, vegetarian flag, number of servings, instructions, and ingredients. The application supports the following features:
    -Adding new recipes with their ingredients.
    -Updating existing recipes and their ingredients.
    -Deleting recipes.
    -Filtering recipes based on various criteria like number of servings, vegetarian flag, keywords in instructions and specific ingredients.

- **Ports**: The service exposes port 8080 and maps it to port 8080 on the host machine, allowing access via localhost:8080.

### Tests Container
I also have a separate container for running tests, named `tests` by pulling postman/newman. This container depends on the `app` service, ensuring that tests can interact with the main recipeBook application.

## API Endpoints

#### **GET** `/recipes`
- **Description**: List all recipes.
- **Controller**: `RecipeBookController:getAllRecipes`
- **Request Body**: None

#### **GET** `/ingredients`
- **Description**: List all ingredients.
- **Controller**: `RecipeBookController:getAllIngredients`
- **Request Body**: None

#### **POST** `/recipes/filter`
- **Description**: Filter recipes based on given criteria.
- **Controller**: `RecipeBookController:deleteRecipe`
- **Request Body**: 
  ```json
    {
        "numberOfServings": 4,
        "isVegetarian": true,
        "instructionsContain": "oven"
    }

#### **POST** `/recipes`
- **Description**: Add a new recipe.
- **Controller**: `RecipeBookController:addRecipe`
- **Request Body**: 
  ```json
    {
        "name": "Recipe Name",
        "isVegetarian": true,
        "numberOfServings": 4,
        "instructions": "Recipe Instructions",
        "ingredients": [{"name": "Ingredient1"}, {"name": "Ingredient2"}]
    }

#### **PUT** `/recipes/{id}`
- **Description**:  Update a specific recipe by its ID.
- **Controller**: `RecipeBookController:updateRecipe`
- **Request Body**: 
  ```json
    {
        "name": "Updated Recipe Name",
        "isVegetarian": false,
        "numberOfServings": 2,
        "instructions": "Updated Instructions",
        "ingredients": [{"name": "UpdatedIngredient1"}]
    }

#### **DELETE** `/recipes/{id}`
- **Description**: Delete a specific recipe by its ID.
- **Controller**: `RecipeBookController:deleteRecipe`
- **Request Body**: None

#### **Default Route** `ANY /{route}`
- **Description**: Block any other unspecified route and return a "Not Found" message.
- **Response**: `404 Not Found`

## Database Schema

The RecipeBook application utilizes a relational H2 database. The database schema is designed to manage recipes and their associated ingredients. Below are the details of the tables and their structure:

### `recipes` Table
- **`id`**: Primary key, auto-generated (Long type).
- **`name`**: Name of the recipe (String).
- **`isVegetarian`**: Boolean value indicating if the recipe is vegetarian.
- **`numberOfServings`**: Integer specifying the number of servings the recipe makes.
- **`instructions`**: Text field for cooking instructions.

### `ingredients` Table
- **`id`**: Primary key, auto-generated (Long type).
- **`name`**: Name of the ingredient (String).

### `recipe_ingredient` Table
- **`recipe_id`**: Foreign key referencing the id in the recipes table.
- **`ingredient_id`**: Foreign key referencing the id in the ingredients table.
Primary Key: A composite primary key consisting of recipe_id and ingredient_id.

## Design Pattern

### Client-Server Pattern
The RecipeBook application follows the Client-Server pattern. The server provides API endpoints (RESTful services), and clients (such as a web browser or a Postman application) make requests to these endpoints. The server processes these requests and returns responses. The client and server operate independently, and communication is done over HTTP.
### Model-View-Controller (MVC)
In this RecipeBook application, the MVC pattern is primarily applied on the server side. The Model represents the application data structures, such as Recipe and Ingredient. The View is essentially the JSON responses returned to the client, as there is no direct user interface built into the server. The Controller consists of endpoints that handle client requests, process them using services and models, and return appropriate responses.
### Repository
The Repository Pattern is implemented through interfaces like RecipeRepository and IngredientRepository. These repositories abstract the data layer, providing a collection-like interface for accessing domain objects. They handle all data operations and translate domain models to database models, ensuring separation of concerns.
### Service Layer
The Service Layer encapsulates the application's business logic. In our application, this is evident in the RecipeBookService class. It acts as a transaction boundary and contains business logic and transformations, separating our controllers from the data access layer.
### Dependency Injection
By using Spring's @Autowired to inject dependencies like repositories into services, I am employing Dependency Injection, which is a form of Inversion of Control. This makes application more flexible and easier to test.
### Exception Handling
Error and exception handling in endpoints (using try-catch blocks and specific responses) can be seen as a pattern to ensure consistency and reliability in how application handles and reports errors.

## Contact
Idil BULAK
bulakidil@gmail.com