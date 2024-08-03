package com.example.demoperpus.view;

import com.example.demoperpus.controller.BookDetailController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.demoperpus.App;
import com.example.demoperpus.model.Book;

import java.io.IOException;

import static com.example.demoperpus.utils.UIUtils.show;

public class BookDetailPage{

    private final Book book;

    public BookDetailPage(Book book) {
        this.book = book;
    }

    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("book-detail-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        BookDetailController bookDetailController = fxmlLoader.getController();
        bookDetailController.initData(book);

        show(primaryStage, scene);
    }
}
