package client.ui;

import client.model.ModelGame;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;

public class CardSelection {

  private StackPane baseStackPane;
  private ModelGame modelGame;
  private int finalCounter;

  public CardSelection(StackPane baseStackPane) {
    this.baseStackPane = baseStackPane;
  }

  public void overlayDamagecards(String[] damagePiles, int counter) {
    //TODO: Logo für Damageauswahl
    finalCounter = counter;
    StackPane overlay = new StackPane();
    overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
    overlay.setAlignment(Pos.CENTER);
    baseStackPane.getChildren().add(overlay);
    Label label = new Label("Damages to pick: " + counter);
    overlay.getChildren().add(label);
    List<ImageView> imageViews = new ArrayList<>();
    for (String card : damagePiles) {
      switch (card) {
        case "Worm" -> {
          InputStream input1 = getClass().getResourceAsStream("/textures/cards/WORM.png");
          Image image1 = new Image(input1);
          ImageView imageView1 = new ImageView(image1);
          imageView1.setId("Spam");
          imageView1.setPreserveRatio(true);
          imageView1.setFitWidth(200);
          overlay.getChildren().add(imageView1);
          imageViews.add(imageView1);
        }
        case "Virus" -> {
          InputStream input2 = getClass().getResourceAsStream(
              "/textures/cards/VIRUS.png");
          Image image2 = new Image(input2);
          ImageView imageView2 = new ImageView(image2);
          imageView2.setId("Worm");
          imageView2.setImage(new Image(input2));
          imageView2.setPreserveRatio(true);
          imageView2.setFitWidth(200);
          overlay.getChildren().add(imageView2);
          imageViews.add(imageView2);
        }
        case "Tojan" -> {
          InputStream input3 = getClass().getResourceAsStream(
              "/textures/cards/TROJAN_HORSE.png");
          Image image3 = new Image(input3);
          ImageView imageView3 = new ImageView(image3);
          imageView3.setId("Virus");
          imageView3.setImage(new Image(input3));
          imageView3.setPreserveRatio(true);
          imageView3.setFitWidth(200);
          overlay.getChildren().add(imageView3);
          imageViews.add(imageView3);
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
          if (finalCounter == 0) {
            baseStackPane.getChildren().remove(overlay);
            for (String c : damagePiles) {
              overlay.getChildren().remove(c);
            }
            // send the selected cards to the model
            modelGame.sendReturnCards(selectedCards.toArray(new String[0]));
          }
        }
      });
    }
  }

  //Wenn Upgradephase aktiv, dann kann man auf den Kartenstapel für Upgradekarten klicken, damit sich Shop öffnet
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
}


