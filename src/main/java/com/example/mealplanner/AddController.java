package com.example.mealplanner;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class AddController {
    private Stage stage;
    private Scene scene;
    private static Meal meal = new Meal();


    @FXML
    private SplitMenuButton typeSelection;
    @FXML
    private Button buttonRemove;
    @FXML
    private TextField mealTF;
    @FXML
    private TextField ingredientTF,ingredientQuantityTF;
    @FXML
    private TextArea recipeTF;
    @FXML
    private ListView<String> ingredientString,quantityString; // to save the values for our database
    @FXML
    private ListView<String> listIngredients;
    @FXML
    private Label errorName,errorIngredient,errorRecipe,errorDouble;

    // Nutrient textfields
    @FXML
    private TextField servingTF,caloriesTF,carbohydratesTF,proteinTF,fatTF;
    @FXML
    private Label errorNutrients;

    //////////////////Functions///////////////////
    @FXML
    void addIngredient() {
        if (isTextFieldEmpty(ingredientTF) || isTextFieldEmpty(ingredientQuantityTF)) {
            // Just to prevent adding nothing
        } else if (ingredientString.getItems().contains(ingredientTF.getText().toLowerCase())) {
            // just to prevent adding double
            errorDouble.setVisible(true);
        } else {
            listIngredients.getItems().add(ingredientQuantityTF.getText().toLowerCase() + " " + ingredientTF.getText().toLowerCase());

            // Adding them separately for easy editing
            ingredientString.getItems().add(ingredientTF.getText().toLowerCase());
            quantityString.getItems().add(ingredientQuantityTF.getText().toLowerCase());

            // Visuals
            listIngredients.setVisible(true);
            buttonRemove.setVisible(true);
            ingredientTF.clear();
            ingredientQuantityTF.clear();

            errorDouble.setVisible(false);
        }
    }
    @FXML
    void removeIngredient() {
        int selectedIngredient = listIngredients.getSelectionModel().getSelectedIndex();
            listIngredients.getItems().remove(selectedIngredient);
            ingredientString.getItems().remove(selectedIngredient);
            quantityString.getItems().remove(selectedIngredient);

    }
    public boolean isTextFieldEmpty(TextField textField) {
        String text = textField.getText();
        return text == null || text.trim().isEmpty();
    }

    @FXML
    void selectType(ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource(); // get the selected item

        typeSelection.setText(selectedItem.getText()); // set the text to the selected item
        meal.setMealType(selectedItem.getText()); // set the value of our object accordingly
    }
    @FXML
    boolean checkForEmpty() { // this will check if any vital information is left empty and only produce a message where it is needed
        boolean containsEmpty = false;
        errorName.setVisible(false);
        errorIngredient.setVisible(false);
        errorRecipe.setVisible(false);

        if (isTextFieldEmpty(mealTF)){
            errorName.setVisible(true);
            containsEmpty = true;
        }
        if (listIngredients.getItems().isEmpty()){
            errorIngredient.setVisible(true);
            containsEmpty = true;
        }
        if(recipeTF == null || recipeTF.getText().trim().isEmpty()){
            errorRecipe.setVisible(true);
            containsEmpty = true;
        }

    return containsEmpty;
    }

    @FXML
    void addMeal(ActionEvent event) {
        System.out.println("addMeal called");
        if(checkForEmpty())
        {
            System.out.println("Missing required fields");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing fields");
            alert.setHeaderText(null);
            alert.setContentText("Please fill the meal name, add at least one ingredient and enter a recipe.");
            alert.showAndWait();
            return;
        }
        DatabaseConnection connect = new DatabaseConnection();
        Connection connection = connect.getConnection();
        if (connection == null) {
            System.out.println("Database connection is null");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not connect to the database. Please check your database settings and that MySQL is running.");
            alert.showAndWait();
            return;
        }
        try{
            meal.setName(mealTF.getText());
            String insertQuery = "INSERT INTO meal (name, meal_type, recipe) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setString(1, meal.getName());
            statement.setString(2, meal.getMealType());
            statement.setString(3, recipeTF.getText());
            statement.executeUpdate();
            ObservableList<String> texts1 = ingredientString.getItems();
            ObservableList<String> texts2 = quantityString.getItems();
            for(int i=0; i < texts1.size();i++) {
                String selectQueryF = "SELECT * FROM ingredient WHERE ingredient_name = ?";
                PreparedStatement selectStatement = connection.prepareStatement(selectQueryF);
                selectStatement.setString(1,texts1.get(i));
                ResultSet resultset = selectStatement.executeQuery();
                if(!resultset.next()) {
                    String insertQueryF = "INSERT INTO ingredient (ingredient_name) VALUES (?)";
                    PreparedStatement statementF = connection.prepareStatement(insertQueryF);
                    statementF.setString(1, texts1.get(i));
                    statementF.executeUpdate();
                }
                String insertQueryF2 = "INSERT INTO mealingredient (meal_name,ingredient_name,amount) VALUES (?, ?, ?)";
                PreparedStatement statementF2 = connection.prepareStatement(insertQueryF2);
                statementF2.setString(1, mealTF.getText());
                statementF2.setString(2, texts1.get(i));
                statementF2.setString(3, texts2.get(i));
                statementF2.executeUpdate();
            }
            statement.close();
            connection.close();
            switchNutrition(event);
        } catch (Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred: " + e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    boolean checkForEmptyNutrient(){
        boolean containsEmpty = false;
        errorNutrients.setVisible(false);
        if(isTextFieldEmpty(servingTF) || isTextFieldEmpty(caloriesTF) || isTextFieldEmpty(carbohydratesTF) || isTextFieldEmpty(proteinTF) || isTextFieldEmpty(fatTF)){
            containsEmpty = true;
            errorNutrients.setVisible(true);
        }
        return containsEmpty;
    }
    @FXML
    void addNutrient(ActionEvent event){
        if(checkForEmptyNutrient()){
            return;
        }
        DatabaseConnection connect = new DatabaseConnection();
        Connection connection = connect.getConnection();
        try{
            String insertQuery = "INSERT INTO macronutritions (meal_name, calories, protein, carbohydrates, fat) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setString(1, meal.getName());
            System.out.println(meal.getName());
            statement.setFloat(2, Float.parseFloat(caloriesTF.getText()));
            statement.setFloat(3, Float.parseFloat(proteinTF.getText()));
            statement.setFloat(4, Float.parseFloat(carbohydratesTF.getText()));
            statement.setFloat(5, Float.parseFloat(fatTF.getText()));
            statement.executeUpdate();

            //switching scene to main scene
            switchMainPage(event);
        } catch (Exception e){e.printStackTrace();}
    }

    ////////////////Scene Switches/////////////////////
    public void switchMainPage(ActionEvent event)
    {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));

            stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            // Store the current window size
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            scene = new Scene(root);

            // Set the stored size to the new scene
            stage.setScene(scene);
            stage.setWidth(currentWidth);
            stage.setHeight(currentHeight);

            stage.show();
        } catch (Exception e) {System.out.println(e.getMessage());}
    }
    public void switchNutrition(ActionEvent event)
    {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("AddNutrition.fxml"));

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        // Store the current window size
        double currentWidth = stage.getWidth();
        double currentHeight = stage.getHeight();

        scene = new Scene(root);

        // Set the stored size to the new scene
        stage.setScene(scene);
        stage.setWidth(currentWidth);
        stage.setHeight(currentHeight);

        stage.show();
         } catch (Exception e) {System.out.println(e.getMessage());}
    }
    public void switchFrontPage(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FrontPage.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Store the current window size
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            scene = new Scene(root);

            // Set the stored size to the new scene
            stage.setScene(scene);
            stage.setWidth(currentWidth);
            stage.setHeight(currentHeight);

            stage.show();
        } catch (Exception e) {System.out.println(e.getMessage());}
    }
    public void switchToEditMealPage(ActionEvent event)
    {
        try{
        Parent root = FXMLLoader.load(getClass().getResource("EditMealPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        // Store the current window size
        double currentWidth = stage.getWidth();
        double currentHeight = stage.getHeight();

        scene = new Scene(root);

        // Set the stored size to the new scene
        stage.setScene(scene);
        stage.setWidth(currentWidth);
        stage.setHeight(currentHeight);

        stage.show();
    } catch (Exception e) {System.out.println(e.getMessage());}
    }

    // this only exits to give buttons a glowing effect when hovered over
    @FXML
    protected void onButtonHover(MouseEvent event) {
        InnerShadow shadow = new InnerShadow();
        shadow.setColor(Color.web("#0CA0A0"));
        ((Button)event.getSource()).setEffect(shadow); // using getSource to get our Button and cast it as Button with (Button)
    }
    @FXML
    protected void onButtonLeave(MouseEvent event) {
        InnerShadow shadow = new InnerShadow();
        shadow.setColor(Color.web("#095252"));
        ((Button) event.getSource()).setEffect(shadow); // using getSource to get our Button and cast it as Button with (Button)
    }

}
