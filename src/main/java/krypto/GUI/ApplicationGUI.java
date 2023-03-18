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

    public static void main(String[] args) throws Exception {
        Key key = new Key();
        key.generateKey();
        DESX desx = new DESX();
        byte[] test = new byte[]{1,1,1,1,1,1,1,1};
        byte[] testKey = new byte[]{1,2,3,4,5,6};
        key.generateKey();
        key.generatePermutedKeys(key.getKey(1));
//        test(desx.extendedPermutation(test));
        for(int i=0;i<16;i++){
//            test(key.getPermuttedKey(i));
        }


        try {
//            test(desx.feistelFunction(test, testKey));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
            desx.cipher(test, key);


//        launch();
    }
}