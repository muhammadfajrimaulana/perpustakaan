package com.example.demoperpus.view;

import com.example.demoperpus.App;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.demoperpus.utils.UIUtils.show;

public class AddBookPage {

    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("add-book-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        show(primaryStage, scene);
    }
}
