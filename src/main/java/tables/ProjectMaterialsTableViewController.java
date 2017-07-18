/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tables;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;

/**
 *
 * @author cmeehan
 */
public class ProjectMaterialsTableViewController {

    public ObservableList data = FXCollections.observableArrayList();
    public ObservableList materialTypes = FXCollections.observableArrayList("Material 1", "Material 2", "Material 3", "Material 4");

    /**
     * This sets up the table including any necessary data to be added.
     *
     * @param tableView
     */
    public void materialsTable(TableView tableView) {
        Callback<TableColumn<Materials, String>, TableCell<Materials, String>> cellFactory = (TableColumn<Materials, String> param) -> new EditingStringCell();
        
        // Setting the column properties
        TableColumn<Materials, String> materialTypeColumn = new TableColumn("Material Type");
        TableColumn<Materials, String> materialQuantityColumn = new TableColumn("Quantity");
        TableColumn<Materials, String> materialDescriptionColumn = new TableColumn("Description");
        
        materialTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        materialTypeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), materialTypes));
        materialTypeColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.33));
        materialTypeColumn.setOnEditCommit((TableColumn.CellEditEvent<Materials, String> t)->{
            int tableRow = t.getTablePosition().getRow();
            ((Materials) t.getTableView().getItems().get(tableRow)).setType(t.getNewValue());
        });
        
        materialQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        materialQuantityColumn.setCellFactory(cellFactory);
        materialQuantityColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.16));
        materialQuantityColumn.setOnEditCommit((TableColumn.CellEditEvent<Materials, String> t)->{
            int tableRow = t.getTablePosition().getRow();
            ((Materials) t.getTableView().getItems().get(tableRow)).setType(t.getNewValue());
        });
        
        materialDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        materialDescriptionColumn.setCellFactory(cellFactory);
        materialDescriptionColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.50));
        materialDescriptionColumn.setOnEditCommit((TableColumn.CellEditEvent<Materials, String> t)->{
            int tableRow = t.getTablePosition().getRow();
            ((Materials) t.getTableView().getItems().get(tableRow)).setType(t.getNewValue());
        });
        
        tableView.setItems(data);
        tableView.getColumns().addAll(materialTypeColumn, materialQuantityColumn, materialDescriptionColumn);
        
    }

    public static class Materials {
        private final SimpleStringProperty type, quantity, description;
        
        public Materials(String type, String quantity, String description) {
            this.type = new SimpleStringProperty(type);
            this.quantity = new SimpleStringProperty(quantity);
            this.description = new SimpleStringProperty(description);
        }
        
        public void setType(String type){
            this.type.set(type);
        }
        
        public String getType(){
            return this.type.get();
        }
        
        public void setQuantity(String quantity){
            this.quantity.set(quantity);
        }
        
        public String getQuantity(){
            return this.quantity.get();
        }
        
        public void setDescription(String description){
            this.description.set(description);
        }
        
        public String getDescription(){
            return this.description.get();
        }
    }
    public class EditingStringCell extends TableCell<ProjectMaterialsTableViewController.Materials, String> {

        /**
         * Allow editing of a textfield cell
         */
        private TextField textField;

        public EditingStringCell() {
        }

        @Override
        public void startEdit() {
            super.startEdit();
            if (!isEmpty()) {
                createTextField();
            }
            setText(null);
            setGraphic(textField);
            textField.requestFocus();
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText((String) getItem());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.focusedProperty().addListener((ObservableValue<? extends Boolean> arg, Boolean arg1, Boolean arg2) -> {
                if (!arg2) {
                    commitEdit(textField.getText());
                }
            });
            textField.setOnKeyPressed((KeyEvent event) -> {
                if (event.getCode() == KeyCode.ENTER) {
                    commitEdit(textField.getText());
                } else if (event.getCode() == KeyCode.TAB) {
                    commitEdit(textField.getText());
                    TableColumn nextColumn = getNextColumn(!event.isShiftDown());
                    if (nextColumn != null) {
                        getTableView().edit(getTableRow().getIndex(), nextColumn);

                    }
                }
            });
        }

        // Thanks to https://gist.github.com/abhinayagarwal/9383881
        private TableColumn<ProjectMaterialsTableViewController.Materials, ?> getNextColumn(boolean forward) {
            List<TableColumn<ProjectMaterialsTableViewController.Materials, ?>> columns = new ArrayList<>();
            getTableView().getColumns().forEach((column) -> {
                columns.addAll(getLeaves(column));
            });
            // There is no other column that supports editing.
            if (columns.size() < 2) {
                return null;
            }
            int currentIndex = columns.indexOf(getTableColumn());
            int nextIndex = currentIndex;
            if (forward) {
                nextIndex++;
                if (nextIndex > columns.size() - 1) {
                    nextIndex = 0;
                }
            } else {
                nextIndex--;
                if (nextIndex < 0) {
                    nextIndex = columns.size() - 1;
                }
            }
            return columns.get(nextIndex);
        }

        private List<TableColumn<ProjectMaterialsTableViewController.Materials, ?>> getLeaves(
                TableColumn<ProjectMaterialsTableViewController.Materials, ?> root) {
            List<TableColumn<ProjectMaterialsTableViewController.Materials, ?>> columns = new ArrayList<>();
            if (root.getColumns().isEmpty()) {
                // We only want the leaves that are editable.
                if (root.isEditable()) {
                    columns.add(root);
                }
                return columns;
            } else {
                root.getColumns().forEach((column) -> {
                    columns.addAll(getLeaves(column));
                });
                return columns;
            }
        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }
    }

}
