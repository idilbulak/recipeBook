public class App {
    public static void main(String[] args) {
        RecipeBook myRecipeBook = RecipeBook.getInstance();
        myRecipeBook.printBook();
    }
}

class RecipeBook {
    // The single instance of the RecipeBook class
    private static RecipeBook instance = new RecipeBook();

    // Private constructor to prevent external instantiation
    private RecipeBook() {}

    // Public method to get the instance of the RecipeBook
    public static RecipeBook getInstance() {
        return instance;
    }

    // Method to print a message
    public void printBook() {
        System.out.println("recipeBook");
    }
}
