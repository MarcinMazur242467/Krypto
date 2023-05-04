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
import java.util.*;

import static krypto.model.Key.bytesToHex;
import static krypto.model.Text.divideFileIntoBlocks;

public class PlecakController implements Initializable {
    @FXML
    private ToggleSwitch switchF;

    @FXML
    private TextArea CipherTextField;

    @FXML
    private TextField publicKey;

    @FXML
    private TextField privateKey;


    @FXML
    private Button LoadCipherFromFIleButton;

    @FXML
    private Button LoadTextFromFileButton;

    @FXML
    private TextArea PlainTextField;

    private Key key = new Key();
    private byte[][] buffer;
    private List<BigInteger> textInBigIntArray;
    private List<BigInteger> cipherInBigIntArray;
    private Knapsack knapsack = new Knapsack();

    private List<BigInteger> bigIntBuff = new ArrayList<>();

    @FXML
    public void changeSceneToMain(ActionEvent event) throws IOException {
        Parent DESXViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Scene DESXViewScene = new Scene(DESXViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(DESXViewScene);
        window.show();
    }

    @FXML
    public void changeSceneToDESX(ActionEvent event) throws IOException {
        Parent DESXViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DESX.fxml")));
        Scene DESXViewScene = new Scene(DESXViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(DESXViewScene);
        window.show();
    }
    private String intToHex(int value) {
        StringBuilder builder = new StringBuilder();
        String hexDigits = "0123456789ABCDEF";

        // Initialize an empty string to hold the result
        String hexadecimal = "";

        // Convert the decimal number to hexadecimal
        while (value > 0) {
            int remainder = value % 16;
            hexadecimal = hexDigits.charAt(remainder) + hexadecimal;
            value = value / 16;
        }
        return hexadecimal;
    }
    private void insertData(){
        StringBuilder builderPrivateKey = new StringBuilder();
        StringBuilder builderPublicKey = new StringBuilder();
        for (int i =0;i<8;i++) {
            String hexPrivateKey = intToHex(knapsack.getPrivateKey().get(i).intValue());
            String hexPublicKey = intToHex(knapsack.getPublicKey().get(i).intValue());
            if(i==7){
                builderPrivateKey.append(hexPrivateKey);
                builderPublicKey.append(hexPublicKey);
            }else{
                builderPrivateKey.append(hexPrivateKey).append(",");
                builderPublicKey.append(hexPublicKey).append(",");
            }
        }
        privateKey.setText(builderPrivateKey.toString());
        publicKey.setText(builderPublicKey.toString());
    }
    @FXML
    public void generateKeys(ActionEvent event) {
        knapsack.generatePrivateKey();
        knapsack.printKnapsack();
        insertData();
    }

    public void allert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void loadKey(ActionEvent event) {
        List<BigInteger> privateKey = new ArrayList<>();
        String hexString =this.privateKey.getText();
        if (!hexString.matches("[ABCDEF0123456789,]*")){
            allert(Alert.AlertType.WARNING, "Błędny klucz", "Klucz musi być podany w postaci hexadecymalnej!");
            return;
        }
        String[] stringValues = hexString.split(",");
        if (stringValues.length != 8){
            allert(Alert.AlertType.WARNING, "Błędny klucz", "Podany klucz musi składać sie z 8 części!");
            return;
        }
        BigInteger sum = BigInteger.valueOf(0);
        for (int i =0;i<8;i++) {
            BigInteger num = new BigInteger(stringValues[i],16);
            int compare = sum.compareTo(num);
            if(compare >= 0){
                allert(Alert.AlertType.WARNING, "Błędny klucz", "Podany klucz nie jest superrosnący!");
                return;
            }
            sum=sum.add(num);
            privateKey.add(num);
        }
        knapsack.loadPrivateKey(privateKey);
        insertData();
        knapsack.printKnapsack();
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
    public void loadKeyFromFile(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        try {
            File file = chooser.showOpenDialog(window);

            if (file != null) {
                FileObjManager manager = new FileObjManager(file);
                this.knapsack = manager.read();
                insertData();
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
                manager.write(knapsack);
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
                KnapsackFileManager manager = new KnapsackFileManager(file);
                textInBigIntArray = manager.readBigIntegersFromFile();
                for (BigInteger b : textInBigIntArray) {
                    stringBuilder.append(b);
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
                KnapsackFileManager manager = new KnapsackFileManager(file);
                cipherInBigIntArray = manager.readBigIntegersFromFile();
                bigIntBuff = cipherInBigIntArray;
                for (BigInteger b : cipherInBigIntArray) {
                    stringBuilder.append(b);
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
                KnapsackFileManager manager = new KnapsackFileManager(file);
                manager.saveBigIntegersToFile(textInBigIntArray);
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
                KnapsackFileManager manager = new KnapsackFileManager(file);
                manager.saveBigIntegersToFile(cipherInBigIntArray);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    @FXML
    public void Cipher(ActionEvent event) throws Exception {
        String text = PlainTextField.getText();
        StringBuilder builder = new StringBuilder();
        BigInteger integer;
        char c;
        for (int i =0;i<text.length();i++){
            c = text.charAt(i);
            integer = knapsack.encrypt(c);
            bigIntBuff.add(integer);
            if(i==text.length()-1) {
                builder.append(intToHex(integer.intValue()));
            }
            else builder.append(intToHex(integer.intValue())).append(",");
        }
        cipherInBigIntArray = bigIntBuff;
        CipherTextField.setText(builder.toString());
    }

    @FXML
    public void Decipher(ActionEvent event) throws Exception {
        StringBuilder builder = new StringBuilder();
        for (BigInteger bigInteger : bigIntBuff) {
            builder.append(knapsack.decrypt(bigInteger));
            textInBigIntArray.add(bigInteger);
        }
        PlainTextField.setText(builder.toString());
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

