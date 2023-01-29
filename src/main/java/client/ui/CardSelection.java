package client.ui;

import client.model.ModelGame;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class CardSelection {

  private StackPane baseStackPane;
  private ModelGame modelGame;
  private int finalCounter;

  public CardSelection(StackPane baseStackPane) {
    modelGame = ModelGame.getInstance();
    this.baseStackPane = baseStackPane;
  }

  public void overlayDamagecards(String[] damagePiles, int counter) {
    //TODO: Logo für Damageauswahl
    Platform.runLater(() -> {
    finalCounter = counter;
    StackPane overlay = new StackPane();
    overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
    overlay.setAlignment(Pos.CENTER);
    baseStackPane.getChildren().add(overlay);
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    overlay.getChildren().add(grid);
    int column = 0;
    Label label = new Label("Damages to pick: " + counter);
    label.setStyle("-fx-font-size: 20; -fx-text-fill: yellow;");
    grid.add(label, 0, 0);
    List<ImageView> imageViews = new ArrayList<>();
    for (int i=0;i<damagePiles.length;i++) {
      String card = damagePiles[i];
      switch (card) {

        case "Virus" -> {
          InputStream input2 = getClass().getResourceAsStream("/textures/cards/VIRUS.png");
          Image image2 = new Image(input2);
          ImageView imageView2 = new ImageView(image2);
          imageView2.setId("Virus");
          imageView2.setPreserveRatio(true);
          imageView2.setFitWidth(200);
          grid.add(imageView2, column, 1);
          imageViews.add(imageView2);
          column++;
        }
        case "Worm" -> {
          InputStream input1 = getClass().getResourceAsStream("/textures/cards/WORM.png");
          Image image1 = new Image(input1);
          ImageView imageView1 = new ImageView(image1);
          imageView1.setId("Worm");
          imageView1.setPreserveRatio(true);
          imageView1.setFitWidth(200);
          grid.add(imageView1, column, 1);
          imageViews.add(imageView1);
          column++;
        }
        case "Trojan" -> {
          InputStream input3 = getClass().getResourceAsStream("/textures/cards/TROJAN_HORSE.png");
          Image image3 = new Image(input3);
          ImageView imageView3 = new ImageView(image3);
          imageView3.setId("Trojan");
          imageView3.setPreserveRatio(true);
          imageView3.setFitWidth(200);
          grid.add(imageView3, column, 1);
          imageViews.add(imageView3);
          column++;
        }
      }
    }
    List<String> selectedCards = new ArrayList<>();
    for (ImageView imageView : imageViews) {
      imageView.setOnMouseClicked(event -> {
        if (event.getButton() == MouseButton.PRIMARY) {
          String selectedCard = imageView.getId();
          selectedCards.add(selectedCard);
          finalCounter--;
          label.setText("Damages to pick: " + finalCounter);
          if (finalCounter == 0) {
            baseStackPane.getChildren().remove(overlay);
            for (String c : damagePiles) {
              overlay.getChildren().remove(c);
            }
          }
        }
      });

    }
    // send the selected cards to the model
    modelGame.sendReturnCards(selectedCards.toArray(new String[0]));
    });
  }

  //Wenn Upgradephase aktiv, dann Popup
  //In VMgameWindow wird Liste der kaufbaren Karten gepflegt (durch Messages refillshop und exchange shop), aktuelle Liste wird Übertragen
  public void upgradeShop (){
    //TODO: Grundstruktur - Stackpane mit einem Gridpane in der Mitte
    //Energie Guthaben Oben ersichtlich
    //Logo für UpgradeShop

    //Karten werden für "isBuying" auf false gesetzt
    //Karten anklicken zum Kaufen, dabei wird "isBuying" auf "true" gesetzt Message "BuyUpgrade" verschickt
    //Wenn "UpgradeBought" zurückkommt, wird Karte aus Shop entfernt
    //Mit "Go Back" schließt sich der Shop wieder
  }

  //Wenn auf Kartenstapel - sieht man gekaufte Upgradekarten
}


