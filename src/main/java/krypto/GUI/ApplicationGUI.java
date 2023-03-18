package krypto.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import krypto.model.Key;
import krypto.model.DESX;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

import static krypto.model.DESX.test;

public class ApplicationGUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainScreen.fxml")));
        Scene scene = new Scene(root);
        Image icon = new Image(getClass().getResourceAsStream("encryption-culture-mydiamo.jpg"));
        stage.getIcons().add(icon);
        stage.setTitle("Projekt Kryptografia");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Key key = new Key();
        key.generateKey();
        DESX desx = new DESX();
        byte[] test = new byte[]{127,23,12,123};
        byte[] testKey = new byte[]{1,2,3,4,5,6};
        key.generateKey();
//        key.generatePermutedKeys(key.getKey(1));
//        test(desx.extendedPermutation(test));
        try {
            desx.feistelFunction(test, testKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        launch();
    }
}