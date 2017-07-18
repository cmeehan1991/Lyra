/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projects;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import main.Main;
import org.controlsfx.control.CheckComboBox;
import tables.ProjectMaterialsTableViewController;
import tables.ProjectMaterialsTableViewController.Materials;
import tables.ProjectNotesTableViewController;

/**
 * FXML Controller class
 *
 * @author cmeehan
 */
public class ProjectFXMLController implements Initializable {

    @FXML
    TextField projectIdTextField, projectNameTextField;
    
    @FXML 
    ComboBox clientComboBox, projectTypeComboBox;
    
    @FXML 
    CheckComboBox projectManagersCheckComboBox;
    
    @FXML
    ToggleGroup projectStatusGroup;
    
    @FXML
    RadioButton statusBid, statusPending, statusInProgress, statusComplete;
    
    @FXML 
    ListView projectEmployeesListView;
    
    @FXML 
    TextArea projectNotesArea;
    
    @FXML 
    TableView projectMaterialsTableView, projectNotesTableView;
    
    private final ProjectMaterialsTableViewController materialsTableViewController = new ProjectMaterialsTableViewController();
    private final ProjectNotesTableViewController projectNotesTableViewController = new ProjectNotesTableViewController();
    
    private Stage currentStage;
    
    /**
     * Opens a new project in a new window. 
     * 
     * @param event 
     * @throws java.io.IOException 
     */
    @FXML
    protected void startNewProject(ActionEvent event) throws IOException{
        Main main = new Main();
        main.openNewStage("Add New Project");
    }
    
    /**
     * Exports various project data in multiple forms. 
     * @param event 
     */
    @FXML
    protected void exportProjectData(ActionEvent event){
        
    }
    
    /**
     * Email specific project data to recipient(s)
     * @param event 
     */
    @FXML
    protected void sendProject(ActionEvent event){
        
    }
    
    /**
     * Save the project
     * Either save as new or update depending on if it is a new project. 
     * 
     * @param event 
     */
    @FXML
    protected void saveProject(ActionEvent event){
        
    }
    
    /**
     * Save the current project and open a new one. 
     * @param event 
     */
    @FXML
    protected void saveProjectOpenNew(ActionEvent event){
        
    }
    
    /**
     * Save the project and close the window
     * @param even 
     */
    @FXML
    protected void saveProjectClose(ActionEvent even){
        
    }
    
    /**
     * Add an employee to the project employee list
     * @param event 
     */
    @FXML 
    protected void addEmployee(ActionEvent event){
        
    }
    
    /**
     * Remove the selected employee from the project employee list
     * @param event 
     */
    @FXML 
    protected void removeEmployee(ActionEvent event){
        
    }
    
    /**
     * Add line for new project materials
     * @param event 
     */
    @FXML
    protected void addProjectMaterial(ActionEvent event){
        ObservableList<Materials> newRow = FXCollections.observableArrayList(new Materials(null, null, null));  
        this.projectMaterialsTableView.getItems().addAll(newRow);
        
    }
    
    /**
     * Remove selected line for project materials
     * @param event 
     */
    @FXML
    protected void removeProjectMaterial(ActionEvent event){
        ObservableList<Materials> itemsToRemove = this.projectMaterialsTableView.getSelectionModel().getSelectedItems();
        
        this.projectMaterialsTableView.getItems().removeAll(itemsToRemove);
    }
    
    /**
     * Add line for new project note
     * @param event 
     */
    @FXML
    protected void addProjectNote(ActionEvent event){
        
    }
    
    /**
     * Remove selected line for project note
     * @param event 
     */
    @FXML
    protected void removeProjectNote(ActionEvent event){
        
    }
    
    /**
     * Close the project window. 
     * @param event 
     */
    @FXML
    protected void closeProject(ActionEvent event){
        currentStage = (Stage) projectIdTextField.getScene().getWindow();
        currentStage.close();
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        materialsTableViewController.materialsTable(this.projectMaterialsTableView);
        projectNotesTableViewController.projectNotesTable(projectNotesTableView);
    }    
    
}
