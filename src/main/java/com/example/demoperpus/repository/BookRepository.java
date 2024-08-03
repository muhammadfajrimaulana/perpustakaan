package com.example.demoperpus.repository;

import com.example.demoperpus.config.DatabaseConnection;
import com.example.demoperpus.model.Book;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookRepository {
    private static final Logger log = Logger.getLogger(BookRepository.class.getName());

    public void save(Book book) {
        String sql = "INSERT INTO book (book_code, judul_buku, pengarang, penerbit, deskripsi) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getDbConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, book.getBookCode());
            statement.setString(2, book.getJudulBuku());
            statement.setString(3, book.getPengarang());
            statement.setString(4, book.getPenerbit());
            statement.setString(5, book.getDeskripsi());
            statement.executeUpdate();

        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error saving product: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Book findByBookCode(String bookCode) {
        String sql = "select book_code, judul_buku, pengarang, penerbit, deskripsi, created_at, updated_at from book where book_code = ?";
        Book book = null;
        try (Connection connection = DatabaseConnection.getDbConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(sql)) {
            statement.setString(1, bookCode);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    book = mapResultSetToProduct(rs);
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error finding product by ID: {0}", e.getMessage());
        }
        return book;
    }

    public List<Book> findAll() {
        String sql = "select book_code, judul_buku, pengarang, penerbit, deskripsi, created_at, updated_at from book";
        List<Book> books = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getDbConnection();
             Statement statement = Objects.requireNonNull(connection).createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error finding all products: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
        return books;
    }

    public boolean update(Book book) {
        String sql = "UPDATE book SET judul_buku = ?, pengarang = ?, penerbit = ?, deskripsi = ?, updated_at = ? WHERE book_code = ?";
        boolean updated = false;
        try (Connection connection = DatabaseConnection.getDbConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(sql)) {
            statement.setString(1, book.getJudulBuku());
            statement.setString(2, book.getPengarang());
            statement.setString(3, book.getPenerbit());
            statement.setString(4, book.getDeskripsi());
            statement.setTimestamp(5, Timestamp.from(Instant.now()));
            statement.setString(6, book.getBookCode());
            updated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error updating product: {0}", e.getMessage());
        }
        return updated;
    }

    public boolean delete(String bookCode) {
        String sql = "DELETE FROM book WHERE book_code = ?";
        boolean deleted = false;
        try (Connection connection = DatabaseConnection.getDbConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(sql)) {
            statement.setString(1, bookCode);
            deleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error deleting product: {0}", e.getMessage());
        }
        return deleted;
    }

    private Book mapResultSetToProduct(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBookCode(rs.getString("book_code"));
        book.setJudulBuku(rs.getString("judul_buku"));
        book.setPengarang(rs.getString("pengarang"));
        book.setPenerbit(rs.getString("penerbit"));
        book.setDeskripsi(rs.getString("deskripsi"));
        book.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        Timestamp updatedAtTimestamp = rs.getTimestamp("updated_at");
        if (updatedAtTimestamp != null) {
            book.setUpdatedAt(updatedAtTimestamp.toLocalDateTime());
        } else {
            book.setUpdatedAt(null);
        }
        return book;
    }
}
