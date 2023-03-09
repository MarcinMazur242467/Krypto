module krypto.krypto {
    requires javafx.controls;
    requires javafx.fxml;


    opens krypto.GUI to javafx.fxml;
    exports krypto.GUI;
}