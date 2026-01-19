package com.example.mealplanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.Statement;


public class EditMealController{
    private Stage stage;
    private Scene scene;
    private static Meal editMeal = new Meal();

    @FXML
    private TableView<Meal> tableView;

    @FXML
    private TableColumn<Meal, String> tableColumn;
    @FXML
    private Button buttonDelete,buttonSure,buttonEdit;

    // ChangeMealPage Variables
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
    private Label ingredientTitle,mealTitle;
    @FXML
    private Button buttonIngredientAdd;
    @FXML
    private Button buttonEditMeal,buttonEditMacro;
    @FXML
    private Label macroTitle,servingTitle,caloriesTitle,carboTitle,proteinTitle,fatTitle;
    @FXML
    private TextField servingTF,caloriesTF,carbohydratesTF,proteinTF,fatTF;
    @FXML
    private Label errorNutrients;

    public void initialize() {
            tableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));


            List<Meal> meals = retrieveIngredients();
            tableView.getItems().addAll(meals);

    }

    public void setupMacro(){
        //setting the textfields
        DatabaseConnection connect = new DatabaseConnection();
        Connection connection = connect.getConnection();
        try {
            String query = "SELECT * FROM macronutritions WHERE meal_name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, editMeal.getName());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                servingTF.setText(resultSet.getString("serving"));
                caloriesTF.setText(resultSet.getString("calories"));
                carbohydratesTF.setText(resultSet.getString("carbohydrates"));
                proteinTF.setText(resultSet.getString("protein"));
                fatTF.setText(resultSet.getString("fat"));
            }
        }catch(Exception e){e.printStackTrace();}

        //visuals
        buttonEditMeal.setVisible(false);
        buttonEditMacro.setVisible(false);
        macroTitle.setVisible(true);
        servingTitle.setVisible(true);
        caloriesTitle.setVisible(true);
        carboTitle.setVisible(true);
        proteinTitle.setVisible(true);
        fatTitle.setVisible(true);
        servingTF.setVisible(true);
        caloriesTF.setVisible(true);
        carbohydratesTF.setVisible(true);
        proteinTF.setVisible(true);
        fatTF.setVisible(true);


    }
    public void setupChange(){
        //setting the meal name
        mealTF.setText(editMeal.getName());
        //setting the ingredients
        setIngredientList();
        //setting the type and recipe
        DatabaseConnection connect = new DatabaseConnection();
        Connection connection = connect.getConnection();
        try {
            String query = "SELECT meal_type FROM meal WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, editMeal.getName());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String type = resultSet.getString("meal_type");
                typeSelection.setText(type);
            }

            query = "SELECT recipe FROM meal WHERE name = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, editMeal.getName());

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String recipe = resultSet.getString("recipe");
                recipeTF.setText(recipe);
            }
        }catch(Exception e){e.printStackTrace();}

        // visuals
        buttonEditMacro.setVisible(false);
        buttonEditMeal.setVisible(false);
        mealTitle.setVisible(true);
        mealTF.setVisible(true);
        listIngredients.setVisible(true);
        buttonRemove.setVisible(true);
        ingredientTitle.setVisible(true);
        ingredientTF.setVisible(true);
        ingredientQuantityTF.setVisible(true);
        buttonIngredientAdd.setVisible(true);
        typeSelection.setVisible(true);
        recipeTF.setVisible(true);
    }
    private List<Meal> retrieveIngredients() {
        List<Meal> mealNameList = new ArrayList<>();
        try {

            DatabaseConnection connect = new DatabaseConnection();
            Connection connection = connect.getConnection();

             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT name FROM meal");

            while (resultSet.next()) {
                String mealName = resultSet.getString("name");
                //mealNames.add(resultSet.getString(1));
                Meal meal = new Meal(mealName);
                mealNameList.add(meal);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mealNameList;
    }
    @FXML
    public void activateSureButton(){
            buttonDelete.setDisable(true);
            buttonDelete.setVisible(false);
            buttonSure.setDisable(false);
            buttonSure.setVisible(true);
    }
    @FXML
    public void deleteMeal(ActionEvent event){
        Meal selectedMeal = tableView.getSelectionModel().getSelectedItem();
        if (selectedMeal != null) {
            try {
                DatabaseConnection connect = new DatabaseConnection();
                Connection connection = connect.getConnection();

                String deleteQuery = "DELETE FROM meal WHERE name = ?";
                PreparedStatement statement = connection.prepareStatement(deleteQuery);
                statement.setString(1, selectedMeal.getName());
                statement.executeUpdate();

                statement.close();
                connection.close();
            }catch(SQLException e){e.printStackTrace();}
            switchChangeMealPage(event);
        }
    }
    public void setIngredientList() {
        try {
            DatabaseConnection connect = new DatabaseConnection();
            Connection connection = connect.getConnection();

            String query = "SELECT ingredient_name, amount FROM mealingredient WHERE meal_name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, editMeal.getName());

            ResultSet resultSet = statement.executeQuery();
            List<String> ingredients = new ArrayList<>();
            List<String> amounts = new ArrayList<>();

            while (resultSet.next()) {
                String ingredient = resultSet.getString("ingredient_name");
                String amount = resultSet.getString("amount");
                ingredients.add(ingredient);
                amounts.add(amount);
            }
            ingredientString.getItems().addAll(ingredients);
            quantityString.getItems().addAll(amounts);
            for(int i=0;i < ingredients.size();i++)
            {
                listIngredients.getItems().add(amounts.get(i)+ " " +ingredients.get(i));
            }

        }catch(Exception e){e.printStackTrace();}
    }

    @FXML
    void selectType(ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource(); // get the selected item

        typeSelection.setText(selectedItem.getText()); // set the text to the selected item
        editMeal.setMealType(selectedItem.getText()); // set the value of our object accordingly
    }
    ////////////////Scene Switches/////////////////////
    public void switchChangeMealPage(ActionEvent event) {
        Meal selectedMeal = tableView.getSelectionModel().getSelectedItem();
        if (selectedMeal != null) {
            editMeal = selectedMeal;
            try {
             Parent root = FXMLLoader.load(getClass().getResource("ChangePage.fxml"));

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
            } catch (Exception e) {
            System.out.println(e.getMessage());
            }
         }
    }
    public void switchMainPage(ActionEvent event)
    {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));

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
        try {
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
    public void switchAddPageTitle(ActionEvent event)
    {
        try{
        Parent root = FXMLLoader.load(getClass().getResource("AddPageTitle.fxml"));
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

