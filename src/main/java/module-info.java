module com.example.knorrigekorrelatehp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;

    opens helloworld to javafx.fxml;
    exports helloworld;

    opens server to javafx.graphics;
    exports server;

    opens client to javafx.graphics;
    exports client;
}