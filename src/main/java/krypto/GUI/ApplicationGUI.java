package krypto.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import krypto.model.Knapsack;

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

        Knapsack knapsack = new Knapsack();
        knapsack.generatePrivateKey();
        knapsack.printKnapsack();
        System.out.println("A="+knapsack.encrypt((char)65));
        System.out.println("A="+knapsack.decrypt(knapsack.encrypt((char)65)));

//        launch();
    }
}

