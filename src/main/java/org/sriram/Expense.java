package org.sriram.expensetracker.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Expense {

    private final IntegerProperty id;
    private final StringProperty description;
    private final DoubleProperty amount;
    private final StringProperty category;
    private final ObjectProperty<LocalDate> date;

    // Constructor for new expense (no ID yet)
    public Expense(String description, double amount, String category, LocalDate date) {
        this(0, description, amount, category, date);
    }

    // Constructor with ID (from DB)
    public Expense(int id, String description, double amount, String category, LocalDate date) {
        this.id = new SimpleIntegerProperty(id);
        this.description = new SimpleStringProperty(description);
        this.amount = new SimpleDoubleProperty(amount);
        this.category = new SimpleStringProperty(category);
        this.date = new SimpleObjectProperty<>(date);
    }

    // ID
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    // Description
    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }
    public StringProperty descriptionProperty() { return description; }

    // Amount
    public double getAmount() { return amount.get(); }
    public void setAmount(double amount) { this.amount.set(amount); }
    public DoubleProperty amountProperty() { return amount; }

    // Category
    public String getCategory() { return category.get(); }
    public void setCategory(String category) { this.category.set(category); }
    public StringProperty categoryProperty() { return category; }

    // Date
    public LocalDate getDate() { return date.get(); }
    public void setDate(LocalDate date) { this.date.set(date); }
    public ObjectProperty<LocalDate> dateProperty() { return date; }
}
