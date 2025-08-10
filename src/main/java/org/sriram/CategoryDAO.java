package org.sriram.expensetracker.dao;

import org.sriram.expensetracker.model.Category;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO interface for Category operations.
 */
public interface CategoryDAO {
    void addCategory(Category category) throws SQLException;
    List<Category> getAllCategories() throws SQLException;
    void updateCategory(Category category) throws SQLException;
    void deleteCategory(int id) throws SQLException;
}
