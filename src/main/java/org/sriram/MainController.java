package org.sriram.expensetracker;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import org.sriram.expensetracker.model.Expense;
import org.sriram.expensetracker.model.Category;
import org.sriram.expensetracker.service.ExpenseController;

import java.time.LocalDate;

public class MainController {

    @FXML
    private TableView<Expense> expenseTable;
    @FXML
    private TableColumn<Expense, String> descriptionColumn;
    @FXML
    private TableColumn<Expense, Double> amountColumn;
    @FXML
    private TableColumn<Expense, String> categoryColumn;
    @FXML
    private TableColumn<Expense, LocalDate> dateColumn;

    @FXML
    private TextField descriptionField;
    @FXML
    private TextField amountField;
    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private DatePicker datePicker;

    private ExpenseController expenseController;

    @FXML
    public void initialize() {
        // 1. Initialize service layer
        expenseController = new ExpenseController();

        // 2. Bind table to ObservableList from service layer
        expenseTable.setItems(expenseController.getExpenses());

        // 3. Load categories from DB into dropdown
        categoryComboBox.setItems(expenseController.getCategories());

        // 4. Setup column mappings
        // For now, you may keep simple getters (not property binding)
        descriptionColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        amountColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getAmount()));
        categoryColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategory()));
        dateColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDate()));
    }

    @FXML
    private void handleAddExpense(ActionEvent event) {
        String description = descriptionField.getText();
        String amountText = amountField.getText();
        Category category = categoryComboBox.getValue();
        LocalDate date = datePicker.getValue();

        try {
            double amount = Double.parseDouble(amountText);

            // Call service layer method — it will validate & persist
            expenseController.addExpense(description, amount,
                    category != null ? category.getName() : null,
                    date);

            // Clear input fields after adding
            descriptionField.clear();
            amountField.clear();
            categoryComboBox.getSelectionModel().clearSelection();
            datePicker.setValue(null);

        } catch (NumberFormatException e) {
            showAlert("Invalid amount", "Please enter a valid number for amount.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
