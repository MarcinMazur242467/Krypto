package krypto.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import krypto.model.DESX;
import krypto.model.Key;

import java.util.Objects;

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

//    public static void main(String[] args){
//        launch();
//    }
    public static void main(String[] args) throws Exception {
        Key keys = new Key();
        keys.generateKey();
        keys.generateKey();
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder out = new StringBuilder();
        byte[] block = {1, 1, 1, 1, 1, 1, 1, 1};
        DESX desx = new DESX();

        byte[] buffer = new byte[8];
        buffer = desx.cipher(block, keys);
        for (byte b : buffer) {
            stringBuilder.append((char) b);
        }
        System.out.println(stringBuilder);
        buffer = desx.decipher(buffer, keys);
        for (byte b : buffer) {
            out.append((char) b);
        }
        System.out.println(out);
    }
}

