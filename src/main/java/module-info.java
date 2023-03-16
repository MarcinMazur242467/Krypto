module krypto.krypto {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;


    opens krypto.GUI to javafx.fxml;
    exports krypto.GUI;
}