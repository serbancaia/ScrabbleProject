module com.mycompany.scrabble_project.scrabble_client {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.scrabble_project.scrabble_client to javafx.fxml;
    opens com.mycompany.scrabble_project.controllers to javafx.fxml;
    exports com.mycompany.scrabble_project.scrabble_client;
    exports com.mycompany.scrabble_project.controllers;
    requires inet.ipaddr;
    requires java.base;
    requires diction;
}
