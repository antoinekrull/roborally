package client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;

/**
 * @author Dominic
 * @version 1.0
 */
public class ClientMain extends Application {

    public Parent root;
    private Client client;
    String address = "localhost";
    int port = 4000;

    //Link to fxml file
    URL url = getClass().getResource("login.fxml");
    public LoginScreen controller;


    /**
     * FXMLLoader is loaded, the login window is created and the client is started.
     *
     * @param primaryStage Required to create a Stage, the window for the game.
     * @return client Tells LoginScreen the client.
     */
    @Override
    public void start(Stage primaryStage){

        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(url);
            root = loader.load();
            Scene login = new Scene(root, 600, 520);
            login.getStylesheets().add("/chat/chatgui/styles.css");
            primaryStage.setScene(login);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        LoginScreen loginScreen = loader.getController();
        try {
            client = new Client(address, port, loginScreen);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error creating Client");
        }
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                logout(primaryStage);
            }
        });
        loginScreen.getClient(client);

    }
    public void logout(Stage stage){
        client.closeApplication();
        stage.close();
    }
    public static void main(String[] args) { launch(args);}
}


