/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dialogs;

import java.util.HashMap;
import java.util.Optional;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import static javafx.scene.control.Alert.AlertType.WARNING;
import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;
import javafx.scene.control.ButtonType;
import static javafx.scene.control.ButtonType.APPLY;
import static javafx.scene.control.ButtonType.CANCEL;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import main.Main;
import projects.Projects;

/**
 *
 * @author cmeehan
 */
public class Dialogs {
    
    /**
     * Display a warning dialog. 
     * @param titleText
     * @param headerText
     * @param contentText 
     */
    public void warningDialog(String titleText, String headerText, String contentText){
        Alert alert = new Alert(WARNING);
        alert.setTitle(titleText);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    } 
    
    /**
     * Show the project memo dialog and allow the user to either update the 
     * selected memo or create a new memo.
     * 
     * @param id 
     * @param projectId 
     */
    public void ProjectNotesDialog(String id, String projectId){
        Projects projects = new Projects();
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Project Memo");
  
        // Set the button types
        dialog.getDialogPane().getButtonTypes().addAll(APPLY, CANCEL);
        
        // Create the user interface
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextArea memo = new TextArea();
        
        grid.add(new Label("Memo:"), 0, 0);
        grid.add(memo, 0, 1);
        
        Node applyButton = dialog.getDialogPane().lookupButton(APPLY);
        applyButton.setDisable(true);
        
        memo.textProperty().addListener((ovservable, oldValue, newValue)->{
            if(!newValue.trim().isEmpty()){
                applyButton.setDisable(true);
            }else{
                applyButton.setDisable(false);
            }
        });
        
        // Check if we are updating/viewing a memo or just creating a new memo
        if(id != null){
            projects.getMemo(id, memo, projectId);
        }
        // Set the graphic
        dialog.setGraphic(grid);
        
        Optional<String> result = dialog.showAndWait();
        if(result != null && !result.toString().trim().isEmpty()){
            projects.setMemo(id, result.toString(), projectId);
        }
    }
    
    /**
     * Displays a custom login dialog that returns the username and password in
     * a HashMap as Strings.
     * @return The username and password.  
     */
    public HashMap<String, String> LoginDialog(){
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle(Main.APPLICATION_NAME);
        dialog.setHeaderText(" ");
        
        // Set the icon (must be included in this project
        dialog.setGraphic(new ImageView(this.getClass().getClassLoader().getResource("images/logo.png").toString()));

        // Set the button types
        ButtonType loginButtonType = new ButtonType("Login", OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, CANCEL);
        
        // Create the username and password labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        
        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1,0);
        grid.add(new Label("Password:"), 0,1);
        grid.add(password, 1,1);
        
        // Enable/Disable login button depending on whether a usernamd and password were entered
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);
        
        // Do some validation 
        username.textProperty().addListener((observable, oldValue, newValue)->{
            if(!newValue.trim().isEmpty() && !password.getText().trim().isEmpty()){
                loginButton.setDisable(false);
            }else{
                loginButton.setDisable(true);
            }
        });
        
        password.textProperty().addListener((observable, oldValue, newValue)->{
        if(!newValue.trim().isEmpty() && !username.getText().trim().isEmpty()){
                loginButton.setDisable(false);
            }else{
                loginButton.setDisable(true);
            }
        });
        
        dialog.getDialogPane().setContent(grid);
        
        // Request focus on the username field by default
        Platform.runLater(()-> username.requestFocus());
        
        // Convert the result into a username-password pair when the login button is clicked
        dialog.setResultConverter(dialogButton -> {
            if(dialogButton == loginButtonType){
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });
        
        // Get the result
        Optional<Pair<String, String>> result = dialog.showAndWait();
        
        // Set the hashmap to be returned
        HashMap<String, String> loginSet = new HashMap<>();
        result.ifPresent(usernamePassword -> {
            loginSet.put(usernamePassword.getKey(), usernamePassword.getValue());
        });

        return loginSet;
    }
}
