package org.sriram.expensetracker.model;

import javafx.beans.property.*;

public class Category {

    private final IntegerProperty id;
    private final StringProperty name;

    public Category(int id, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
    }

    // ID getters/setters/properties
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // Name getters/setters/properties
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public String toString() {
        // Ensures ComboBox displays category name
        return getName();
    }
}
