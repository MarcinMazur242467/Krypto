module krypto.krypto {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.apache.pdfbox;


    opens krypto.GUI to javafx.fxml;
    exports krypto.GUI;
}