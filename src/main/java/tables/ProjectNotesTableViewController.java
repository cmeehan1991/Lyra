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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

/**
 *
 * @author cmeehan
 */
import tables.ProjectNotesTableViewController.EditingStringCell;
public class ProjectNotesTableViewController {

    public ObservableList data = FXCollections.observableArrayList();

    /**
     * This sets up the table including any necessary data to be added.
     *
     * @param tableView
     */
    public void projectNotesTable(TableView tableView) {
        Callback<TableColumn<Notes, String>, TableCell<Notes, String>> cellFactory = (TableColumn<Notes, String> param) -> new EditingStringCell();
        
        // Setting the column properties
        TableColumn<Notes, String> materialTypeColumn = new TableColumn("Date");
        TableColumn<Notes, String> materialQuantityColumn = new TableColumn("Time");
        TableColumn<Notes, String> materialDescriptionColumn = new TableColumn("Memo");
        
        materialTypeColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        materialTypeColumn.setCellFactory(cellFactory);
        materialTypeColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.16));
        materialTypeColumn.setOnEditCommit((TableColumn.CellEditEvent<Notes, String> t)->{
            int tableRow = t.getTablePosition().getRow();
            ((Notes) t.getTableView().getItems().get(tableRow)).setType(t.getNewValue());
        });
        
        materialQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        materialQuantityColumn.setCellFactory(cellFactory);
        materialQuantityColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.16));
        materialQuantityColumn.setOnEditCommit((TableColumn.CellEditEvent<Notes, String> t)->{
            int tableRow = t.getTablePosition().getRow();
            ((Notes) t.getTableView().getItems().get(tableRow)).setType(t.getNewValue());
        });
        
        materialDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("memo"));
        materialDescriptionColumn.setCellFactory(cellFactory);
        materialDescriptionColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.68));
        materialDescriptionColumn.setOnEditCommit((TableColumn.CellEditEvent<Notes, String> t)->{
            int tableRow = t.getTablePosition().getRow();
            ((Notes) t.getTableView().getItems().get(tableRow)).setType(t.getNewValue());
        });
        
        tableView.setItems(data);
        tableView.getColumns().addAll(materialTypeColumn, materialQuantityColumn, materialDescriptionColumn);
        
    }

    public static class Notes {
        private final SimpleStringProperty date, time, memo;
        
        public Notes(String date, String time, String memo) {
            this.date = new SimpleStringProperty(date);
            this.time = new SimpleStringProperty(time);
            this.memo = new SimpleStringProperty(memo);
        }
        
        public void setType(String type){
            this.date.set(type);
        }
        
        public String getType(){
            return this.date.get();
        }
        
        public void setQuantity(String time){
            this.time.set(time);
        }
        
        public String getQuantity(){
            return this.time.get();
        }
        
        public void setDescription(String memo){
            this.memo.set(memo);
        }
        
        public String getDescription(){
            return this.memo.get();
        }
    }
    
    public class EditingStringCell extends TableCell<ProjectNotesTableViewController.Notes, String> {

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
    private TableColumn<ProjectNotesTableViewController.Notes, ?> getNextColumn(boolean forward) {
        List<TableColumn<ProjectNotesTableViewController.Notes, ?>> columns = new ArrayList<>();
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

    private List<TableColumn<ProjectNotesTableViewController.Notes, ?>> getLeaves(
            TableColumn<ProjectNotesTableViewController.Notes, ?> root) {
        List<TableColumn<ProjectNotesTableViewController.Notes, ?>> columns = new ArrayList<>();
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
