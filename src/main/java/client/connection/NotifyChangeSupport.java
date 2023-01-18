package client.connection;

import client.viewmodel.ViewModelGameWindow;
import client.viewmodel.ViewModelLobby;
import client.viewmodel.ViewModelRobotSelection;

/**
 * Notifier class. Actively notifies specific instances for changes.
 *
 * @author Tobias
 * @version 0.1
 */
public class NotifyChangeSupport {

    ViewModelGameWindow viewModelGameWindow;
    ViewModelLobby viewModelLobby;
    ViewModelRobotSelection viewModelRobotSelection;
    private static NotifyChangeSupport notifyChangeSupport;

    private boolean lobby;
    private boolean gamewindow;
    private boolean robotselection;

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

    public void notifyInstance() {
        if (lobby) {
            viewModelLobby.receivedMessage();
        }
        if (gamewindow) {
            viewModelGameWindow.receivedMessage();
        }
        if (robotselection) {
            viewModelRobotSelection.robotAccepted();
        }
    }
}
