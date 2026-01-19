module com.example.mealplanner {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql; // das in der Dokumentation ansprechen! und anhand des databaseConnection zeigen
    requires transitive javafx.base;


    opens com.example.mealplanner to javafx.fxml;
    exports com.example.mealplanner;
}