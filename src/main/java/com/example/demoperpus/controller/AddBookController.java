package com.example.demoperpus.controller;

import com.example.demoperpus.model.Book;
import com.example.demoperpus.repository.BookRepository;
import com.example.demoperpus.view.BookListPage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.logging.Logger;

import static com.example.demoperpus.utils.UIUtils.showAlert;

public class AddBookController {
    private static final Logger log = Logger.getLogger(AddBookController.class.getName());
    private static final BookRepository bookRepository = new BookRepository();

    @FXML
    private TextField bookCode;
    @FXML
    private TextField judulBuku;
    @FXML
    private TextField pengarang;
    @FXML
    private TextField penerbit;
    @FXML
    private TextField deskripsi;
    @FXML
    private Button addBookButton;


    @FXML
    private void handleAddBook() {
        try {
            String bookCodeValue = bookCode.getText();
            String judulBukuValue = judulBuku.getText();
            String pengarangValue = pengarang.getText();
            String penerbitValue = penerbit.getText();
            String deskripsiValue = deskripsi.getText();

            if (bookCodeValue.isEmpty() || judulBukuValue.isEmpty() || pengarangValue.isEmpty() || penerbitValue.isEmpty() || deskripsiValue.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Bad Request", "Please enter all the details");
                return;
            }

            if (bookRepository.findByBookCode(bookCodeValue) != null) {
                showAlert(Alert.AlertType.ERROR, "Duplicate Request", "Product Code already exists");
                return;
            }

            Book book = new Book();
            book.setBookCode(bookCodeValue);
            book.setJudulBuku(judulBukuValue);
            book.setPengarang(pengarangValue);
            book.setPenerbit(penerbitValue);
            book.setDeskripsi(deskripsiValue);

            bookRepository.save(book);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Successfully added product");
            back((Stage) addBookButton.getScene().getWindow());
        } catch (Exception exception) {
            showAlert(Alert.AlertType.ERROR, "Server Error", "Please contact the administrator");
            log.warning(exception.getMessage());
        }
    }


    @FXML
    private void handleBack() {
        Stage primaryStage = (Stage) addBookButton.getScene().getWindow();
        back(primaryStage);
    }

    public void back(Stage stage) {
        BookListPage bookListPage = new BookListPage();
        try {
            bookListPage.start(stage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
