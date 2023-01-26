package client.changesupport;

import client.viewmodel.ViewModelGameWindow;
import client.viewmodel.ViewModelLobby;
import client.viewmodel.ViewModelRobotSelection;

import java.io.IOException;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Notifier class. Actively notifies specific instances for changes.
 *
 * @author Tobias
 * @version 2.0
 */
public class NotifyChangeSupport {

    ViewModelGameWindow viewModelGameWindow;
    ViewModelLobby viewModelLobby;
    ViewModelRobotSelection viewModelRobotSelection;
    private static NotifyChangeSupport notifyChangeSupport;

    private boolean lobby;
    private boolean gamewindow;
    private boolean robotselection;
    private final Logger logger = LogManager.getLogger(ViewModelGameWindow.class);

    private NotifyChangeSupport() {

    }
    public static NotifyChangeSupport getInstance() {
        if (notifyChangeSupport == null) {
            notifyChangeSupport = new NotifyChangeSupport();
        }
        return notifyChangeSupport;
    }

    public void setViewModelLobby(ViewModelLobby viewModelLobby) {
        this.viewModelLobby = viewModelLobby;
        this.lobby = true;
        this.gamewindow = false;
        this.robotselection = false;
    }

    public void setViewModelGameWindow(ViewModelGameWindow viewModelGameWindow) {
        this.viewModelGameWindow = viewModelGameWindow;
        this.lobby = false;
        this.gamewindow = true;
        this.robotselection = false;
    }

    public void setViewModelRobotSelection(ViewModelRobotSelection viewModelRobotSelection) {
        this.viewModelRobotSelection = viewModelRobotSelection;
        this.lobby = false;
        this.gamewindow = false;
        this.robotselection = true;
    }

    public void chatMessageArrived() {
        if (lobby) {
            viewModelLobby.receivedMessage();
        }
        if (gamewindow) {
            viewModelGameWindow.receivedChatMessage();
        }
    }

    public void robotAccepted() {
        if (robotselection) {
            viewModelRobotSelection.robotAccepted();
        }
    }
    public void enterGame() throws IOException {
        if (lobby){
            viewModelLobby.enterGame();
        }
    }

    public void updateProgrammingHandCards() {
        if (gamewindow) {
            logger.debug("programmingHandCards");
            viewModelGameWindow.fillHandCards();
        }
    }

    public void robotSetPosition() {
        Platform.runLater(() -> {
            if (gamewindow) {
                viewModelGameWindow.robotSetPosition();
                logger.debug("RobotID in robotSetPosition: ");
            }
        });
    }

    public void logMessageArrived() {
        if (gamewindow) {
            viewModelGameWindow.receivedGameLogMessage();
        }
    }

    public void gameEventMessageArrived() {
        if (gamewindow) {
            viewModelGameWindow.reveivedGameEventMessage();
        }
    }

    public void startTimer() {
        if(gamewindow) {
            viewModelGameWindow.startTimer();
        }
    }


    public void setRobotAlignment() {
        if (gamewindow) {
            viewModelGameWindow.setRobotAlignment();
        }
    }
}
