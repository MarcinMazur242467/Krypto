package krypto.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import krypto.model.KeyGenerator;

import java.io.IOException;
import java.util.Objects;

public class DESXController {
    @FXML
    private Button CIpherButton;

    @FXML
    private TextField CipherTextField;

    @FXML
    private Button DecipherButton;

    @FXML
    private Button GenerateKeyButton;

    @FXML
    private TextField KeyVal1;

    @FXML
    private TextField KeyVal2;

    @FXML
    private TextField KeyVal3;

    @FXML
    private Button LoadCipherFromFIleButton;

    @FXML
    private Button LoadKeyButton1;

    @FXML
    private Button LoadTextFromFileButton;

    @FXML
    private TextField PlainTextField;

    @FXML
    private Button ReadKeyButton;

    @FXML
    private Button SafeCpiherToFIleButton;

    @FXML
    private Button SafeTextToFIleButton;

    @FXML
    private Button WriteKeyButton;

    @FXML
    public void generateKeys(ActionEvent event){
        KeyGenerator generator = new KeyGenerator();
        KeyVal1.setText(generator.generateKey());
        KeyVal2.setText(generator.generateKey());
        KeyVal3.setText(generator.generateKey());
    }


    @FXML
    public void changeSceneToMain(ActionEvent event) throws IOException {
        Parent DESXViewParent  = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Scene DESXViewScene = new Scene(DESXViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(DESXViewScene);
        window.show();
    }
    @FXML
    public void changeSceneToPlecakowy(ActionEvent event) throws IOException {
        Parent DESXViewParent  = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Plecakowy.fxml")));
        Scene DESXViewScene = new Scene(DESXViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(DESXViewScene);
        window.show();
    }
}
