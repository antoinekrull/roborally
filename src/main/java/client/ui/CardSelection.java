package client.ui;

import client.model.ModelGame;
import client.viewmodel.ViewModelGameWindow;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CardSelection {

  private StackPane baseStackPane;
  private ModelGame modelGame;
  private int finalCounter;

  private final Logger logger = LogManager.getLogger(ViewModelGameWindow.class);


  public CardSelection(StackPane baseStackPane) {
    modelGame = ModelGame.getInstance();
    this.baseStackPane = baseStackPane;
  }

  public void overlayDamagecards(String[] damagePiles, int counter) {
    Platform.runLater(() -> {
      finalCounter = counter;
      InputStream pickdamageInput = getClass().getResourceAsStream("textures/designelements/pickDamageSign.png");
      Image pickdamageImage = new Image(pickdamageInput);
      ImageView pickdamageSign = new ImageView(pickdamageImage);
      StackPane overlay = new StackPane();
      overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
      overlay.setAlignment(Pos.CENTER);
      baseStackPane.getChildren().add(overlay);
      GridPane grid = new GridPane();
      grid.setAlignment(Pos.CENTER);
      grid.setVgap(20);
      overlay.getChildren().add(grid);
      int column = 0;
      Label label = new Label("Damages to pick: " + counter);
      label.setStyle("-fx-font-size: 20; -fx-text-fill: yellow;");
      pickdamageSign.setFitWidth(500);
      grid.add(pickdamageSign, 0, 0);
      grid.add(label, 0, 1);
      List<ImageView> imageViews = new ArrayList<>();
      for (int i = 0; i < damagePiles.length; i++) {
        String card = damagePiles[i];
        switch (card) {

          case "Virus" -> {
            InputStream input2 = getClass().getResourceAsStream("/textures/cards/VIRUS.png");
            Image image2 = new Image(input2);
            ImageView imageView2 = new ImageView(image2);
            imageView2.setId("Virus");
            imageView2.setPreserveRatio(true);
            imageView2.setFitWidth(200);
            grid.add(imageView2, column, 2);
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
            grid.add(imageView1, column, 2);
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
            grid.add(imageView3, column, 2);
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

  //Wenn Upgradephase aktiv, dann Popup. VMgameWindow wird Liste der kaufbaren Karten gepflegt (durch Messages refillshop und exchange shop), aktuelle Liste wird Übertragen
  public void upgradeShop(String[] availableUpgrades) {
    Platform.runLater(() -> {
      boolean active = modelGame.currentPlayerProperty().get();
      StackPane overlay = new StackPane();
      InputStream shopsignInput = getClass().getResourceAsStream("/textures/designelements/upgradeShop.png");
      Image shopsignImage = new Image(shopsignInput);
      overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
      overlay.setAlignment(Pos.CENTER);
      baseStackPane.getChildren().add(overlay);
      GridPane grid = new GridPane();
      grid.setAlignment(Pos.CENTER);
      grid.setVgap(20);
      overlay.getChildren().add(grid);
      int column = 0;
      Button finish = new Button("Close Shop");
      ImageView shopsign = new ImageView(shopsignImage);
      Label energyLabel = new Label("My energy: " + modelGame.energyProperty().get());
      shopsign.setFitWidth(500);
      energyLabel.setStyle("-fx-font-size: 20; -fx-text-fill: yellow;");
      grid.add(energyLabel, 1, 0);
      grid.add(shopsign, 0, 0);
      grid.add(finish,1,2);
      List<ImageView> imageViews = new ArrayList<>();
      for (int i = 0; i < availableUpgrades.length; i++) {
        String card = availableUpgrades[i];
        switch (card) {
          case "AdminPrivilege" -> {
            InputStream input2 = getClass().getResourceAsStream("/textures/cards/adminUpgrade.png");
            Image image2 = new Image(input2);
            ImageView imageView2 = new ImageView(image2);
            imageView2.setId("AdminPrivilege");
            imageView2.setPreserveRatio(true);
            imageView2.setFitWidth(200);
            grid.add(imageView2, column, 1);
            imageViews.add(imageView2);
            column++;
          }
          case "RearLaser" -> {
            InputStream input1 = getClass().getResourceAsStream(
                "/textures/cards/rearLaserUpgrade.png");
            Image image1 = new Image(input1);
            ImageView imageView1 = new ImageView(image1);
            imageView1.setId("RearLaser");
            imageView1.setPreserveRatio(true);
            imageView1.setFitWidth(200);
            grid.add(imageView1, column, 1);
            imageViews.add(imageView1);
            column++;
          }
          case "MemorySwap" -> {
            InputStream input3 = getClass().getResourceAsStream(
                "/textures/cards/memorySwapTempUpgrade.png");
            Image image3 = new Image(input3);
            ImageView imageView3 = new ImageView(image3);
            imageView3.setId("MemorySwap");
            imageView3.setPreserveRatio(true);
            imageView3.setFitWidth(200);
            grid.add(imageView3, column, 1);
            imageViews.add(imageView3);
            column++;
          }
          case "SpamBlocker" -> {
            InputStream input4 = getClass().getResourceAsStream(
                "/textures/cards/spamBlockerTempUpgrade.png");
            Image image4 = new Image(input4);
            ImageView imageView4 = new ImageView(image4);
            imageView4.setId("SpamBlocker");
            imageView4.setPreserveRatio(true);
            imageView4.setFitWidth(200);
            grid.add(imageView4, column, 1);
            imageViews.add(imageView4);
            column++;
          }
        }
      }
      for (ImageView imageView : imageViews) {
        imageView.setOnMouseClicked(event -> {
          if (event.getButton() == MouseButton.PRIMARY) {
            if (active) {
              String selectedCard = imageView.getId();
              //sendingBuyingMessage
              modelGame.sendBuyUpgrade(true, selectedCard);
              baseStackPane.getChildren().remove(overlay);
              for (String u : availableUpgrades) {
                overlay.getChildren().remove(u);

              }
            }
            else {
              logger.warn("Not your turn");
            }
          }
        });
      }
      finish.setOnAction(event -> {
        if(active) {
          modelGame.sendBuyUpgrade(false, "Null");
          baseStackPane.getChildren().remove(overlay);
          for (String u : availableUpgrades) {
            overlay.getChildren().remove(u);

          }
        }
        else {
          logger.warn("Not your turn");
        }
      });

    });

  }

  //Wenn auf Kartenstapel - sieht man gekaufte Upgradekarten
}


