package client;

import helloworld.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * @author Dominic
 * @version 1.0
 */
public class ClientMain extends Application {

    public Parent root;
    private Client client;
    String address = "localhost";
    int port = 3000;

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
            //login.getStylesheets().add("/chat/chatgui/styles.css");
            primaryStage.setScene(login);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        LoginScreen controller = loader.getController();
        try {
            client = new Client(address, port, controller);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error creating Client");
        }
        primaryStage.setOnCloseRequest(windowEvent -> logout(primaryStage));
        controller.getClient(client);

    }
    public void logout(Stage stage){
        client.closeApplication();
        stage.close();
    }
    public static void main(String[] args) { launch(args);}
}


