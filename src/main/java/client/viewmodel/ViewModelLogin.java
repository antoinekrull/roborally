package client.viewmodel;


import client.RoboRallyStart;
import client.model.ModelUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

//Testklasse

public class ViewModelLogin {

    @FXML
    private Button loginButton;

    private ModelUser modelUser;

    public ViewModelLogin() {
        this.modelUser = ModelUser.getInstance();
    }

    public void loginButtonOnAction(ActionEvent event) throws IOException {

        RoboRallyStart.switchScene("lobby.fxml");


        //resource is null
        /*
        FXMLLoader loader = FXMLLoader.load(getClass().getResource("lobby.fxml"));
        Parent root = loader.load();
        Stage currentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        currentStage.setScene(new Scene(root, 1650, 1000));
        currentStage.show();
        */;
    }

}
