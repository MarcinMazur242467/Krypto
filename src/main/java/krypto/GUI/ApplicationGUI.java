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
        byte[][] blocks = DESX.divideIntoBlocks("123asd123asd123asd");
        byte[] ones = new byte[6];
//        Arrays.fill(ones, (byte) 0xFF);
//        BigInteger intT = new BigInteger(ones);
//        System.out.println(intT.bitCount());
//        System.out.println(intT.bitLength());

        desx.feistelFunction(blocks[0], key.getKey(0));
//        desx.feistelFunction(blocks[1], key.getKey(0));
//        desx.feistelFunction(blocks[2], key.getKey(0));

//        System.out.println(integer.shiftRight(2));
//        System.out.println();
//        System.out.println(integer);



//        launch();
    }
}