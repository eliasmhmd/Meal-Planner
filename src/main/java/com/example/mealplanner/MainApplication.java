package com.example.mealplanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

            Parent root = FXMLLoader.load(getClass().getResource("FrontPage.fxml"));
            Scene scene1 = new Scene(root);
            scene1.setFill(Color.web( "#434545"));
            stage.setScene(scene1);
            stage.setTitle("Meal Planner :)");
            stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}