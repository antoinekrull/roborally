package client.player;

import game.player.Robot;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Moritz, Dominic, Antoine, Firas
 * @version 1.0
 */
public class ClientPlayer {

    private int id;
    private String username;
    private Robot robot;
    private BooleanProperty isReady;
    private BooleanProperty isPlaying;
    private IntegerProperty score;
    private DoubleProperty energyCubes;
    private IntegerProperty cardsInHand;


    private ObservableList<RegisterInformation> registerInformations;

    public ClientPlayer(int id, String username, Robot robot) {
        this.id = id;
        this.username = username;
        this.robot = robot;
        this.score = new SimpleIntegerProperty();
        this.isPlaying = new SimpleBooleanProperty();
        this.isReady = new SimpleBooleanProperty();
        this.energyCubes = new SimpleDoubleProperty(5);
        this.cardsInHand = new SimpleIntegerProperty(9);
        this.registerInformations = FXCollections.observableArrayList();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BooleanProperty isReadyProperty() {
        return isReady;
    }

    public void setIsReady(boolean isReady) {
        this.isReady.set(isReady);
    }

    public boolean isReady() {
        return isReady.get();
    }

    public boolean isPlaying() {
        return isPlaying.get();
    }

    public BooleanProperty isPlayingProperty() {
        return isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying.set(isPlaying);
    }

    public int getScore() {
        return score.get();
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    @Override
    public String toString() {
        return username;
    }

    public boolean isIsReady() {
        return isReady.get();
    }

    public boolean isIsPlaying() {
        return isPlaying.get();
    }

    public double getEnergyCubes() {
        return energyCubes.get();
    }

    public DoubleProperty energyCubesProperty() {
        return energyCubes;
    }

    public void setEnergyCubes(int energyCubes) {
        this.energyCubes.set(energyCubes);
    }

    public int getHand() {
        return cardsInHand.get();
    }

    public IntegerProperty handProperty() {
        return cardsInHand;
    }

    public void setCardsInHand(int hand) {
        this.cardsInHand.set(hand);
    }

    public void setEnergyCubes(double energyCubes) {
        this.energyCubes.set(energyCubes);
    }

    public int getCardsInHand() {
        return cardsInHand.get();
    }

    public IntegerProperty cardsInHandProperty() {
        return cardsInHand;
    }

    public ObservableList<RegisterInformation> getRegisterInformations() {
        return registerInformations;
    }

    public void setRegisterInformations(ObservableList<RegisterInformation> registerInformations) {
        this.registerInformations = registerInformations;
    }

    public void addEnergy(int count) {
        this.energyCubes.add(count);
    }
}