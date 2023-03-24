package krypto.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ApplicationGUIController {
    @FXML
    public void changeSceneToPlecak(ActionEvent event) throws IOException {
        Parent DESXViewParent = FXMLLoader.load(getClass().getResource("Plecakowy.fxml"));
        Scene DESXViewScene = new Scene(DESXViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(DESXViewScene);
        window.show();
    }

    @FXML
    public void changeSceneToDESX(ActionEvent event) throws IOException {
        Parent DESXViewParent = FXMLLoader.load(getClass().getResource("DESX.fxml"));
        Scene DESXViewScene = new Scene(DESXViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(DESXViewScene);
        window.show();
    }
}