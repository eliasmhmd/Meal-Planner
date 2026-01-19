package com.example.mealplanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SceneController {
    private Stage stage;
    private Scene scene;

   /*@FXML
    Label labelDatabase;

    public void connectTest (){
        DatabaseConnection connect = new DatabaseConnection();
        Connection connection = connect.getConnection();

            try{
                Statement statement = connection.createStatement();
                ResultSet queryOutput = statement.executeQuery("SELECT name FROM meal");
                while(queryOutput.next()) {
                    labelDatabase.setText(queryOutput.getString("name"));
                }
            } catch (Exception e){e.printStackTrace();}
    }*/



    // Theses are here to switch between the different pages
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
    public void switchToMainPage(ActionEvent event) throws IOException
    {
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
    }
    public void switchFrontPage(ActionEvent event) throws IOException {
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
    }
    public void switchAddPageTitle(ActionEvent event) throws IOException
    {
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