package org.sriram.expensetracker.dao;

import org.sriram.expensetracker.model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLite implementation of CategoryDAO.
 */
public class SQLiteCategoryDAO implements CategoryDAO {
    private static final String DB_URL = "jdbc:sqlite:expenses.db";

    public SQLiteCategoryDAO() throws SQLException {
        // Create table if not exists
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS categories (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL UNIQUE)";
            stmt.execute(sql);
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    @Override
    public void addCategory(Category category) throws SQLException {
        String sql = "INSERT INTO categories(name) VALUES(?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category.getName());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
        }
        return categories;
    }

    @Override
    public void updateCategory(Category category) throws SQLException {
        String sql = "UPDATE categories SET name = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category.getName());
            pstmt.setInt(2, category.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteCategory(int id) throws SQLException {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
