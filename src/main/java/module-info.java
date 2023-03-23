module krypto.krypto {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.apache.pdfbox;
    requires itextpdf;
    requires org.apache.commons.codec;


    opens krypto.GUI to javafx.fxml;
    exports krypto.GUI;
}