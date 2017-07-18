/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.sun.javafx.stage.StageHelper;
import dialogs.Dialogs;
import java.io.IOException;
import java.util.HashMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import navigation.NavigationFXMLController;
import projects.ProjectFXMLController;

/**
 *
 * @author cmeehan
 */
public class Main extends Application {

    public static String USER_ID = null, APPLICATION_NAME = "Project Manager", USER_ROLE = null;
    public static final boolean IS_LOGGED_IN = false;
    private Stage stage = new Stage();

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Check to see if the user is logged in. 
        if (!IS_LOGGED_IN) {
            HashMap<String, String> userLogin = new Dialogs().LoginDialog();
            userLogin.forEach((key, value) -> {
                User user = new User();
                user.logIn(key, value, primaryStage);
            });
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/NavigationFXML.fxml"));
            Parent root = (Parent) loader.load();
            NavigationFXMLController controller = loader.getController();

            Scene scene = new Scene(root);

            primaryStage.setY(10.00);
            primaryStage.setX(screenWidth() - stage.getWidth() / 2);
            primaryStage.setTitle("Navigation");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    /**
     * Get the width of the screen to set the location of the navigation box.
     *
     * @return double
     */
    protected static double screenWidth() {
        double width;
        Rectangle2D rectangle = Screen.getPrimary().getBounds();
        width = rectangle.getWidth();
        return width;
    }

    public void openNavigation(Stage primaryStage) throws IOException {

    }

    public void openNewStage(String newStage) throws IOException {
        System.out.println(StageHelper.getStages().toString());
        switch (newStage) {
            case "Add New Project":
                openNewProject();
                break;
            default:
                break;
        }
    }

    private void openNewProject() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/ProjectFXML.fxml"));
        Parent root = (Parent) loader.load();
        ProjectFXMLController controller = loader.getController();

        Scene scene = new Scene(root);

        stage.setTitle("New Project");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
