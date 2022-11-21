module com.example.knorrigekorrelatehp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens helloworld to javafx.fxml;
    exports helloworld;
}