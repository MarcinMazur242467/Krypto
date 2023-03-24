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
import krypto.model.*;
import org.controlsfx.control.ToggleSwitch;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

import static krypto.model.Key.bytesToHex;
import static krypto.model.Text.divideFileIntoBlocks;

public class DESXController implements Initializable {


    @FXML
    private ToggleSwitch switchF;


    @FXML
    private TextArea CipherTextField;


    @FXML
    private TextField KeyVal1;

    @FXML
    private TextField KeyVal2;

    @FXML
    private TextField KeyVal3;

    @FXML
    private Button LoadCipherFromFIleButton;

    @FXML
    private Button LoadTextFromFileButton;

    @FXML
    private TextArea PlainTextField;


    private Key key = new Key();
    private byte[][] buffer;
    private byte[] textInByteArray;
    private byte[] cipherInByteArray;

    @FXML
    public void generateKeys(ActionEvent event) {
        key.resetKey();
        key.generateKey();
        key.generateKey();
        key.generateKey();
        KeyVal1.setText(bytesToHex(key.getKey(0)));
        KeyVal2.setText(bytesToHex(key.getKey(1)));
        KeyVal3.setText(bytesToHex(key.getKey(2)));
        loadKey(event);
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
        } else if (Objects.equals(KeyVal2.getText(), "1111111111111111")) {
            allert(Alert.AlertType.WARNING, "Błędny klucz", "Drugi klucz nie może mieć takiej wartości!");
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
    public void loadKeyFromFile(ActionEvent event) {
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
    public void saveKeysToFile(ActionEvent event) {
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
        StringBuilder stringBuilder = new StringBuilder();
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            File file = chooser.showOpenDialog(window);

            if (file != null) {
                FileManager manager = new FileManager(file);
                textInByteArray = manager.read();
                for (byte b : textInByteArray) {
                    stringBuilder.append((char) b);
                }
            }
            PlainTextField.setText(stringBuilder.toString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    public void loadCipher(ActionEvent event) {
        StringBuilder stringBuilder = new StringBuilder();
        FileChooser chooser = new FileChooser();
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            File file = chooser.showOpenDialog(window);

            if (file != null) {
                FileManager manager = new FileManager(file);
                cipherInByteArray = manager.read();
                buffer = divideFileIntoBlocks(cipherInByteArray);
                for (byte b : cipherInByteArray) {
                    stringBuilder.append((char) b);
                }
            }
            CipherTextField.setText(stringBuilder.toString());
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
                manager.write(textInByteArray);
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
                manager.write(cipherInByteArray);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    @FXML
    public void Cipher(ActionEvent event) throws Exception {
        byte[][] textInBlocks;
        if (!switchF.isSelected()) {
            textInBlocks = Text.divideFileIntoBlocks(textInByteArray);
        } else {
            textInBlocks = Text.divideStringIntoBlocks(PlainTextField.getText());
        }

        buffer = new byte[textInBlocks.length - 1][8];
        DESX desx = new DESX();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < textInBlocks.length - 1; i++) {
            buffer[i] = desx.cipher(textInBlocks[i], key);
            for (byte b : buffer[i]) {
                stringBuilder.append((char) b);
            }
        }
        cipherInByteArray = Text.flatten(buffer);
        CipherTextField.setText(stringBuilder.toString());
    }

    @FXML
    public void Decipher(ActionEvent event) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        DESX desx = new DESX();
        byte[][] temp = new byte[buffer.length][8];
        for (int i = 0; i < buffer.length; i++) {
            temp[i] = desx.decipher(buffer[i], key);
        }
        if (!switchF.isSelected()) {
            for (int i = 0; i < 8; i++) {
                temp[buffer.length - 1][i] = 0;
            }
        }
        textInByteArray = Text.flatten(temp);
        for (byte b : textInByteArray) {
            stringBuilder.append((char) b);
        }
        PlainTextField.setText(stringBuilder.toString());

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        switchF.selectedProperty().addListener((observable, oldValue, newValue) -> {
            PlainTextField.setDisable(true);
            CipherTextField.setDisable(true);
            if (!switchF.isSelected()) {
                CipherTextField.setText("");
                PlainTextField.setText("");
                PlainTextField.setDisable(true);
                CipherTextField.setDisable(true);
                LoadCipherFromFIleButton.setDisable(false);
                LoadTextFromFileButton.setDisable(false);

            } else {
                PlainTextField.setDisable(false);
                CipherTextField.setDisable(false);
                LoadCipherFromFIleButton.setDisable(true);
                LoadTextFromFileButton.setDisable(true);
            }
        });
    }
}
