module com.example.demoperpus {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.sql;
    requires jbcrypt;
    requires com.google.protobuf;


    opens com.example.demoperpus to javafx.fxml;
    exports com.example.demoperpus;
    exports com.example.demoperpus.controller;
    exports com.example.demoperpus.model;
    exports com.example.demoperpus.repository;
    opens com.example.demoperpus.controller to javafx.fxml;
}