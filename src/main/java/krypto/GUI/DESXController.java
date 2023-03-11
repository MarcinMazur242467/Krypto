package krypto.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import krypto.model.FileManager;
import krypto.model.Key;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

import static krypto.model.Key.bytesToHex;

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
    private Stage stage;

    private final Key key = new Key();


    @FXML
    public void generateKeys(ActionEvent event) {
        key.resetKey();
        key.generateKey();
        key.generateKey();
        key.generateKey();
        KeyVal1.setText(bytesToHex(key.getKey(0)));
        KeyVal2.setText(bytesToHex(key.getKey(1)));
        KeyVal3.setText(bytesToHex(key.getKey(2)));
    }

    public void allert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void loadKey(ActionEvent event) {
        key.resetKey();
        if (Objects.equals(KeyVal1.getText(), "")||Objects.equals(KeyVal2.getText(), "")||Objects.equals(KeyVal3.getText(), "")) {
            allert(Alert.AlertType.WARNING, "Błędny klucz", "Nie podałeś klucza! Musisz wpisać klucz jeszcze raz!");
            return;
        }
        if(KeyVal1.getText().matches("[ABCDEF0123456789].{0,15}")||KeyVal2.getText().matches("[ABCDEF0123456789].{0,15}")||KeyVal3.getText().matches("[ABCDEF0123456789].{0,15}")){
            hexToByte(KeyVal1);
            hexToByte(KeyVal2);
            hexToByte(KeyVal3);
        }else{
            allert(Alert.AlertType.ERROR,"dupa","dupa");
        }
    }

    private void hexToByte(TextField field) {
        BigInteger bigInt = new BigInteger(field.getText().getBytes(),0,8);
        System.out.println(bigInt.toString(16));
        byte[] bytes = bigInt.toByteArray();
        System.out.println(bytes.length);
        if (bytes.length <= 8) {
            byte[] paddedBytes = new byte[8];
            System.arraycopy(bytes, 0, paddedBytes, 8 - bytes.length, bytes.length);
            bytes = paddedBytes;
            key.addKey(bytes);
            System.out.println("Hex string: " + field.getText());
            System.out.println("Bytes: " + Arrays.toString(bytes));
        }
    }
    @FXML
    public void loadFromFile(ActionEvent event) {
//        System.out.println(key.getKey(0));
//        System.out.println(key.getKey(1));
//        System.out.println(key.getKey(2));
//        System.out.println(key.getKeyList());
    }


    @FXML
    public void changeSceneToMain(ActionEvent event) throws IOException {
        Parent DESXViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Scene DESXViewScene = new Scene(DESXViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(DESXViewScene);
        window.show();
    }

    @FXML
    public void changeSceneToPlecakowy(ActionEvent event) throws IOException {
        Parent DESXViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Plecakowy.fxml")));
        Scene DESXViewScene = new Scene(DESXViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(DESXViewScene);
        window.show();
    }

//    @FXML
//    public void loadKeyFromFile(ActionEvent event){
//        FileChooser chooser = new FileChooser();
//      ReadKeyButton.setOnAction(e -> {
//            try {
//                File file = chooser.showSaveDialog(stage);
//
//                if (file != null) {
//                    FileManager manager = new FileManager(file);
//                    manager.read();
//                }
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
//        });
//    }
//    @FXML
//    public void loadKeyFromFile(ActionEvent event){
//        FileManager manager = new FileManager("");
//    }
}
