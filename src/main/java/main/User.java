/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import connections.DBConnection;
import dialogs.Dialogs;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import navigation.NavigationFXMLController;

/**
 *
 * @author cmeehan
 */
public class User {

    /**
     * Sign the user in based on the input username and password values.
     * 
     * @param username
     * @param password
     * @param stage 
     */
    public void logIn(String username, String password, Stage stage) {
        Connection conn = new DBConnection().connect();
        String sql = "SELECT ID, ROLE FROM USERS WHERE USERNAME = ? AND PASSWORD = MD5(?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Main.USER_ID = rs.getString("ID");
                Main.USER_ROLE = rs.getString("ROLE");
                openNavigation(stage);
            }else{
                new Dialogs().warningDialog("Login Error", "Login Error", "The username and password do not match our records. Please try again");
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Open the navigation stage. 
     * @param stage 
     */
    private void openNavigation(Stage stage) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/NavigationFXML.fxml"));
        try {
            Parent root = (Parent) loader.load();
            NavigationFXMLController controller = loader.getController();

            Scene scene = new Scene(root);

            stage.setY(10.00);
            stage.setX(Main.screenWidth() - stage.getWidth() / 2);
            stage.setTitle("Navigation");
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest((WindowEvent event) -> {
                Platform.exit();
            });
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
