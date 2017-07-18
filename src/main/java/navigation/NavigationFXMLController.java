/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navigation;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import main.Main;
import static main.Main.main;

/**
 * FXML Controller class
 *
 * @author cmeehan
 */
public class NavigationFXMLController implements Initializable {

    @FXML
    protected void navigationItemClicked(MouseEvent event) throws IOException{
        Label label = (Label) event.getSource();
        new Main().openNewStage(label.getText());
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
