package com.example.mealplanner;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class Meal {
    private SimpleStringProperty name;
    private SimpleIntegerProperty weekday;
    private SimpleIntegerProperty time;
    private SimpleStringProperty mealType;
    private SimpleStringProperty recipe;



    public Meal() {
        this.name = new SimpleStringProperty();
        this.weekday = new SimpleIntegerProperty();
        this.time = new SimpleIntegerProperty();
        this.mealType = new SimpleStringProperty("Regular");
        this.recipe = new SimpleStringProperty();
    }

    public Meal(String name) {
        this.name = new SimpleStringProperty(name);
        this.weekday = new SimpleIntegerProperty();
        this.time = new SimpleIntegerProperty();
        this.mealType = new SimpleStringProperty("Regular");
        this.recipe = new SimpleStringProperty();
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public int getWeekday() {
        return weekday.get();
    }

    public void setWeekday(int weekday) {
        this.weekday.set(weekday);
    }

    public SimpleIntegerProperty weekdayProperty() {
        return weekday;
    }

    public int getTime() {
        return time.get();
    }

    public void setTime(int time) {
        this.time.set(time);
    }

    public SimpleIntegerProperty timeProperty() {
        return time;
    }

    public String getMealType() {
        return mealType.get();
    }

    public void setMealType(String mealType) {
        this.mealType.set(mealType);
    }

    public SimpleStringProperty mealTypeProperty() {
        return mealType;
    }

    public String getRecipe() {
        return recipe.get();
    }

    public void setRecipe(String recipe) {
        this.recipe.set(recipe);
    }

    public SimpleStringProperty recipeProperty() {
        return recipe;
    }

}
