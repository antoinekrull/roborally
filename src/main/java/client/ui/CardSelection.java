package client.ui;

import java.io.InputStream;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class CardSelection {

  private StackPane baseStackPane;

  private void overlayDamagecards(ArrayList<String> damagePiles, int counter) {
    StackPane overlay = new StackPane();
    overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
    overlay.setAlignment(Pos.CENTER);
    baseStackPane.getChildren().add(overlay);
    Label label = new Label("Cards to pick: " + counter);
    overlay.getChildren().add(label);
    for (String card : damagePiles) {
      ImageView damagepile = new ImageView();
      switch (card) {
        case "Worm" -> {
          InputStream input1 = getClass().getResourceAsStream("/textures/cards/WORM.png");
          damagepile.setImage(new Image(input1));
          damagepile.setPreserveRatio(true);
          damagepile.setFitWidth(200);
          overlay.getChildren().add(damagepile);
        }
        case "Virus" -> {
          InputStream input2 = getClass().getResourceAsStream(
              "/textures/cards/VIRUS.png");
          damagepile.setImage(new Image(input2));
          damagepile.setPreserveRatio(true);
          damagepile.setFitWidth(200);
          overlay.getChildren().add(damagepile);
        }
        case "Tojan" -> {
          InputStream input3 = getClass().getResourceAsStream(
              "/textures/cards/TROJAN_HORSE.png");
          damagepile.setImage(new Image(input3));
          damagepile.setPreserveRatio(true);
          damagepile.setFitWidth(200);
          overlay.getChildren().add(damagepile);
        }
      }

    }
  }

}
