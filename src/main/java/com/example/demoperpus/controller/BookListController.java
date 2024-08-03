package com.example.demoperpus.controller;

import com.example.demoperpus.view.BookDetailPage;
import com.example.demoperpus.view.BookListPage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import com.example.demoperpus.config.SessionManager;
import com.example.demoperpus.model.Book;
import com.example.demoperpus.repository.BookRepository;
import com.example.demoperpus.view.AddBookPage;
import com.example.demoperpus.view.LoginPage;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

public class BookListController {

    private static final Logger log = Logger.getLogger(BookListController.class.getName());
    private final BookRepository bookRepository = new BookRepository();

    @FXML
    private TableView<Book> bookTableView;
    @FXML
    private TableColumn<Book, String> bookCodeColumn;
    @FXML
    private TableColumn<Book, String> judulBukuColumn;
    @FXML
    private TableColumn<Book, String> pengarangColumn;
    @FXML
    private TableColumn<Book, String> penerbitColumn;
    @FXML
    private TableColumn<Book, String> deskripsiColumn;
    @FXML
    private TableColumn<Book, LocalDateTime> createdAtColumn;
    @FXML
    private TableColumn<Book, LocalDateTime> updatedAtColumn;
    @FXML
    private Button addBookButton;
    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() throws IOException {
        bookTableView.setItems(getBooks());

        bookCodeColumn.setCellValueFactory(new PropertyValueFactory<>("bookCode"));
        judulBukuColumn.setCellValueFactory(new PropertyValueFactory<>("judulBuku"));
        pengarangColumn.setCellValueFactory(new PropertyValueFactory<>("pengarang"));
        penerbitColumn.setCellValueFactory(new PropertyValueFactory<>("penerbit"));
        deskripsiColumn.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));

        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

        bookTableView.setRowFactory( tv -> {
            TableRow<Book> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY ) {
                    Book clickedBook = row.getItem();
                    log.info("Book clicked: " + clickedBook);
                    showBookDetailPage((Stage) bookTableView.getScene().getWindow(), clickedBook);
                }
            });
            return row;
        });
    }

    @FXML
    private void handleAddBook() throws IOException {
        new AddBookPage().start((Stage) addBookButton.getScene().getWindow());
    }

    @FXML
    private void handleLogout() throws IOException {
        SessionManager.clearSession();
        new LoginPage().start((Stage) logoutButton.getScene().getWindow());
    }

    private ObservableList<Book> getBooks() {
        List<Book> books = bookRepository.findAll();
        return FXCollections.observableList(books);
    }

    private void showBookDetailPage(Stage primaryStage, Book book) {
        BookDetailPage detailPage = new BookDetailPage(book);
        try {
            detailPage.start(primaryStage);
        } catch (Exception e) {
            log.warning("Error while showing book detail page");
        }
    }


}