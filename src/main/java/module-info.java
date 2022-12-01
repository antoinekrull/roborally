module com.example.knorrigekorrelatehp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;

    opens server to javafx.graphics;
    exports server;

    opens client to javafx.graphics, javafx.fxml, javafx.controls;
    exports client;

    exports communication;
}