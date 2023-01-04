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

    opens client.viewmodel to javafx.graphics, javafx.fxml, javafx.controls;
    exports client.viewmodel;

    exports communication;
    exports client.connection;
    opens client.connection to javafx.controls, javafx.fxml, javafx.graphics;
    exports game.player;
    opens game.player to javafx.graphics;
    exports game;
    opens game to javafx.graphics;
    exports game.board;
    opens game.board to javafx.graphics;
}