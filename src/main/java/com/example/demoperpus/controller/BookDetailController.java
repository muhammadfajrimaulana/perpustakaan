package com.example.demoperpus.controller;

import com.example.demoperpus.model.Book;
import com.example.demoperpus.repository.BookRepository;
import com.example.demoperpus.view.BookDetailPage;
import com.example.demoperpus.view.BookListPage;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Logger;

public class BookDetailController {
    private static final Logger log = Logger.getLogger(BookDetailController.class.getSimpleName());
    private Book book;
    private final BookRepository bookRepository = new BookRepository();

    @FXML
    private Label bookCodeLabel;
    @FXML
    private Label judulBukuLabel;
    @FXML
    private Label pengarangLabel;
    @FXML
    private Label penerbitLabel;
    @FXML
    private Label deskripsiLabel;
    @FXML
    private Label createdAtLabel;
    @FXML
    private Label updatedAtLabel;
    @FXML
    private Button editBookButton;
    @FXML
    private Button deleteBookButton;
    @FXML
    private Button backButton;

    public void initData(Book book) {
        this.book = book;
        bookCodeLabel.setText(bookCodeLabel.getText() + " " + book.getBookCode());
        judulBukuLabel.setText(judulBukuLabel.getText() + " " + book.getJudulBuku());
        pengarangLabel.setText(pengarangLabel.getText() + " " + book.getPengarang());
        penerbitLabel.setText(penerbitLabel.getText() + " " + book.getPenerbit());
        deskripsiLabel.setText(deskripsiLabel.getText() + " " + book.getDeskripsi());
        createdAtLabel.setText(createdAtLabel.getText() + " " + book.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        updatedAtLabel.setText(updatedAtLabel.getText() + " " + (book.getUpdatedAt() != null ? book.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : "N/A"));
    }

    @FXML
    private void handleBack() throws IOException {
        new BookListPage().start((Stage) backButton.getScene().getWindow());
    }

    @FXML
    private void handleDelete() throws IOException {
        // Create a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this product?");
        alert.setContentText("Book: " + book.getJudulBuku());

        // Show the alert and wait for a response
        Optional<ButtonType> result = alert.showAndWait();

        // Check if the user confirmed the deletion
        if (result.isPresent() && result.get() == ButtonType.OK) {
            bookRepository.delete(book.getBookCode());
            log.info("Book deleted: " + book);
            showListPage((Stage) deleteBookButton.getScene().getWindow()); // Go back to the book list page
        } else {
            log.info("Book deletion canceled: " + book);
        }
    }

    @FXML
    private void handleEdit() {
        // init dialog
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Edit Book");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // init grid pane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // fill the content with book information
        TextField judulBukuField = new TextField(book.getJudulBuku());
        TextField pengarangField = new TextField(book.getPengarang());
        TextField penerbitField = new TextField(book.getPenerbit());
        TextField deskripsiField = new TextField(book.getDeskripsi());

        grid.add(new Label("JudulBuku:"), 0, 0);
        grid.add(judulBukuField, 1, 0);
        grid.add(new Label("Pengarang:"), 0, 1);
        grid.add(pengarangField, 1, 1);
        grid.add(new Label("Penerbit:"), 0, 2);
        grid.add(penerbitField, 1, 2);
        grid.add(new Label("Deskripsi"), 0, 3);
        grid.add(deskripsiField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Pair<>(judulBukuField.getText(), deskripsiField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(judulDeskripsiPair -> {
            book.setJudulBuku(judulDeskripsiPair.getKey());
            book.setDeskripsi(judulDeskripsiPair.getValue());

            book.setUpdatedAt(LocalDateTime.now());
            // update product to table
            bookRepository.update(book);
            log.info("Book updated: " + book);
            showBookDetailPage((Stage) editBookButton.getScene().getWindow(), book);
        });
    }

    private void showBookDetailPage(Stage primaryStage, Book book) {
        BookDetailPage detailPage = new BookDetailPage(book);
        try {
            detailPage.start(primaryStage);
        } catch (Exception e) {
            log.warning("Error while showing book detail page");
        }
    }

    private void showListPage(Stage primary) throws IOException {
        new BookListPage().start(primary);
    }
}
