package com.example.demoperpus.repository;

import com.example.demoperpus.config.DatabaseConnection;
import com.example.demoperpus.model.User;

import java.sql.*;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRepository {

    private static final Logger log = Logger.getLogger(UserRepository.class.getName());

    public void save(User user) {
        String sql = "INSERT INTO users (username, last_name, first_name, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getDbConnection();
             PreparedStatement statement = Objects.requireNonNull(conn).prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getPassword());
            statement.executeUpdate();

        } catch (Exception e) {
            log.log(Level.SEVERE, "Error saving user: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public User findByUsername(String username) {
        String sql = "SELECT * from users WHERE username = ?";
        User user = null;
        try (Connection conn = DatabaseConnection.getDbConnection();
             PreparedStatement statement = Objects.requireNonNull(conn).prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    user = mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error finding user by username: {0}", e.getMessage());
            throw new RuntimeException(e);
        }
        return user;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUsername(rs.getString("username"));
        user.setLastName(rs.getString("last_name"));
        user.setFirstName(rs.getString("first_name"));
        user.setPassword(rs.getString("password"));
        return user;
    }
}