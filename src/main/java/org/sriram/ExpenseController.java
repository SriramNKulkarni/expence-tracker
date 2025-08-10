package org.sriram.expensetracker.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.sriram.expensetracker.dao.ExpenseDAO;
import org.sriram.expensetracker.dao.SQLiteExpenseDAO;
import org.sriram.expensetracker.dao.CategoryDAO;
import org.sriram.expensetracker.dao.SQLiteCategoryDAO;
import org.sriram.expensetracker.model.Expense;
import org.sriram.expensetracker.model.Category;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseController {

    private final ExpenseDAO expenseDAO;
    private final CategoryDAO categoryDAO;

    private final ObservableList<Expense> expenses = FXCollections.observableArrayList();

    public ExpenseController() {
        try {
            this.expenseDAO = new SQLiteExpenseDAO();
            this.categoryDAO = new SQLiteCategoryDAO();
            loadExpensesFromDB();
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing DAOs", e);
        }
    }

    // ===== Expense methods =====
    private void loadExpensesFromDB() {
        try {
            List<Expense> expenseList = expenseDAO.getAllExpenses();
            expenses.setAll(expenseList);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Expense> getExpenses() {
        return expenses;
    }

    public boolean addExpense(String description, double amount, String category, LocalDate date) {
        if (!validateExpense(description, amount, category, date)) {
            return false; // validation failed
        }

        Expense expense = new Expense(description, amount, category, date);
        try {
            expenseDAO.addExpense(expense);
            loadExpensesFromDB();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateExpense(Expense expense) {
        try {
            expenseDAO.updateExpense(expense);
            loadExpensesFromDB();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteExpense(int id) {
        try {
            expenseDAO.deleteExpense(id);
            loadExpensesFromDB();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ===== Category methods =====
    public ObservableList<Category> getCategories() {
        try {
            return FXCollections.observableArrayList(categoryDAO.getAllCategories());
        } catch (SQLException e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }

    public boolean addCategory(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        try {
            categoryDAO.addCategory(new Category(0, name.trim()));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== Business Logic =====
    private boolean validateExpense(String description, double amount, String category, LocalDate date) {
        return description != null && !description.trim().isEmpty()
                && amount >= 0
                && category != null && !category.trim().isEmpty()
                && date != null;
    }

    public List<Expense> filterByCategory(String category) {
        return expenses.stream()
                .filter(e -> e.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<Expense> filterByDateRange(LocalDate start, LocalDate end) {
        return expenses.stream()
                .filter(e -> !e.getDate().isBefore(start) && !e.getDate().isAfter(end))
                .collect(Collectors.toList());
    }

    public double getTotalForMonth(int year, int month) {
        return expenses.stream()
                .filter(e -> e.getDate().getYear() == year && e.getDate().getMonthValue() == month)
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public double getTotalForCategory(String category) {
        return expenses.stream()
                .filter(e -> e.getCategory().equalsIgnoreCase(category))
                .mapToDouble(Expense::getAmount)
                .sum();
    }
}
