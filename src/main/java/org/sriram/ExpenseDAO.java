package org.sriram.expensetracker.dao;

import org.sriram.expensetracker.model.Expense;

import java.util.List;

public interface ExpenseDAO {
    void addExpense(Expense expense) throws Exception;
    List<Expense> getAllExpenses() throws Exception;
    void updateExpense(Expense expense) throws Exception;
    void deleteExpense(int id) throws Exception;
}

