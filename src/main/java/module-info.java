module com.example.knorrigekorrelatehp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;
    requires javatuples;
    requires org.apache.commons.lang3;

    opens server to javafx.graphics;
    exports server;

    opens client to javafx.graphics, javafx.fxml, javafx.controls;
    exports client;

    exports communication;
}