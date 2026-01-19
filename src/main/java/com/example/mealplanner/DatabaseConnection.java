package com.example.mealplanner;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection databaseLink;

    public Connection getConnection(){
        String databaseName = "meal_planner";
        String databaseUser = "root";
        String databasePassword = "Memo2341Acer40!";
        String url = "jdbc:mysql://localhost/" + databaseName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url,databaseUser,databasePassword);

        } catch (Exception e) {e.printStackTrace();}


        return databaseLink;
    }
}
