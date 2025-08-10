package org.sriram.expensetracker.dao;

import org.sriram.expensetracker.model.Expense;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SQLiteExpenseDAO implements ExpenseDAO {
    private static final String DB_URL = "jdbc:sqlite:expenses.db";

    public SQLiteExpenseDAO() throws SQLException {
        // Create table if it doesn't exist
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS expenses (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "description TEXT NOT NULL," +
                    "amount REAL NOT NULL," +
                    "category TEXT NOT NULL," +
                    "date TEXT NOT NULL" +  // store date as ISO string
                    ")";
            stmt.execute(sql);
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    @Override
    public void addExpense(Expense expense) throws SQLException {
        String sql = "INSERT INTO expenses(description, amount, category, date) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, expense.getDescription());
            pstmt.setDouble(2, expense.getAmount());
            pstmt.setString(3, expense.getCategory());
            pstmt.setString(4, expense.getDate().toString());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Expense> getAllExpenses() throws SQLException {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses";

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Expense expense = new Expense(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getDouble("amount"),
                        rs.getString("category"),
                        LocalDate.parse(rs.getString("date"))
                );
                expenses.add(expense);
            }
        }
        return expenses;
    }

    @Override
    public void updateExpense(Expense expense) throws SQLException {
        String sql = "UPDATE expenses SET description = ?, amount = ?, category = ?, date = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, expense.getDescription());
            pstmt.setDouble(2, expense.getAmount());
            pstmt.setString(3, expense.getCategory());
            pstmt.setString(4, expense.getDate().toString());
            pstmt.setInt(5, expense.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void deleteExpense(int id) throws SQLException {
        String sql = "DELETE FROM expenses WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
