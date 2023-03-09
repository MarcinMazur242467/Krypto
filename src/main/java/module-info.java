module krypto.krypto {
    requires javafx.controls;
    requires javafx.fxml;


    opens krypto.krypto to javafx.fxml;
    exports krypto.krypto;
}