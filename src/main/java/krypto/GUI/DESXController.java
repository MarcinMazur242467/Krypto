package krypto.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import krypto.model.FileManager;
import krypto.model.FileObjManager;
import krypto.model.Key;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.*;

import static krypto.model.Key.bytesToHex;

public class DESXController implements Initializable {
    @FXML
    private Button CIpherButton;

    @FXML
    private TextArea CipherTextField;

    @FXML
    private Button DecipherButton;

    @FXML
    private Button GenerateKeyButton;
    @FXML
    private Button TestButton;

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
    private TextArea PlainTextField;

    @FXML
    private Button ReadKeyButton;

    @FXML
    private Button SaveCpiherToFileButton;

    @FXML
    private Button SaveTextToFileButton;

    @FXML
    private Button WriteKeyButton;

    private Key key = new Key();

    private boolean flag= false;


    @FXML
    public void generateKeys(ActionEvent event) {
        key.resetKey();
        key.generateKey();
        key.generateKey();
        key.generateKey();
        KeyVal1.setText(bytesToHex(key.getKey(0)));
        KeyVal2.setText(bytesToHex(key.getKey(1)));
        KeyVal3.setText(bytesToHex(key.getKey(2)));
        allert(Alert.AlertType.INFORMATION,"Co dalej?","Naciśnij teraz przycisk po prawej stronie: 'Wczytaj klucze' :)");
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
        if (Objects.equals(KeyVal1.getText(), "") || Objects.equals(KeyVal2.getText(), "") || Objects.equals(KeyVal3.getText(), "")) {
            allert(Alert.AlertType.WARNING, "Błędny klucz", "Nie podałeś klucza! Musisz wpisać klucz jeszcze raz!");
            return;
        }
        if (KeyVal1.getText().matches("[ABCDEF0123456789]*") && KeyVal2.getText().matches("[ABCDEF0123456789]*") && KeyVal3.getText().matches("[ABCDEF0123456789]*")) {
            hexToByte(KeyVal1);
            hexToByte(KeyVal2);
            hexToByte(KeyVal3);
        } else {
            allert(Alert.AlertType.ERROR, "Błędny klucz", "Klucz jest za długi albo nie jest w systemie 16");
        }
    }

    private void hexToByte(TextField field) {
        BigInteger bigInt = new BigInteger(field.getText(), 16);
        byte[] bytes = bigInt.toByteArray();
        if (bytes.length > 8) {
            // jeśli liczba jest dłuższa niż 8 bajtów, to obetnij tablicę do pierwszych 8 bajtów
            bytes = Arrays.copyOf(bytes, 8);
        }
        System.out.println(bytes.length);
            byte[] paddedBytes = new byte[8];
            System.arraycopy(bytes, 0, paddedBytes, 8 - bytes.length, bytes.length);
            bytes = paddedBytes;
            key.addKey(bytes);
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

    @FXML
    public void loadKeyFromFile(ActionEvent event){
        FileChooser chooser = new FileChooser();
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            File file = chooser.showOpenDialog(window);

            if (file != null) {
                FileObjManager manager = new FileObjManager(file);
                key = manager.read();
                KeyVal1.setText(bytesToHex(key.getKey(0)));
                KeyVal2.setText(bytesToHex(key.getKey(1)));
                KeyVal3.setText(bytesToHex(key.getKey(2)));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    @FXML
    public void saveKeysToFile(ActionEvent event){
        FileChooser chooser = new FileChooser();
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
           try {
                File file = chooser.showSaveDialog(window);

              if (file != null) {
                   FileObjManager manager = new FileObjManager(file);
                   manager.write(key);
               }
            } catch (Exception ex) {
               throw new RuntimeException(ex);
           }
    }

    @FXML
    public void loadText(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            File file = chooser.showOpenDialog(window);

            if (file != null) {
                FileManager manager = new FileManager(file);
                List<String> list = manager.read();
                for (String s : list) {
                    PlainTextField.appendText(s+"\n");
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    public void loadCipher(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            File file = chooser.showOpenDialog(window);

            if (file != null) {
                FileManager manager = new FileManager(file);
                List<String> list = manager.read();
                for (String s : list) {
                    CipherTextField.appendText(s+"\n");
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    public void saveText(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            File file = chooser.showSaveDialog(window);
            if (file != null) {
                FileManager manager = new FileManager(file);
                manager.write(PlainTextField.getText());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    public void saveCipher(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            File file = chooser.showSaveDialog(window);
            if (file != null) {
                FileManager manager = new FileManager(file);
                manager.write(CipherTextField.getText());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }



    @FXML
    public void test(){
        System.out.println(key.getKeyList());
        System.out.println(key.getKey(0));
        System.out.println(key.getKey(1));
        System.out.println(key.getKey(2));
        System.out.println();
        System.out.println(bytesToHex(key.getKey(0)));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
