package client.connection;

import client.viewmodel.ViewModelGameWindow;
import client.viewmodel.ViewModelLobby;

/**
 * Notifier class. Actively notifies specific instances for changes.
 *
 * @author Tobias
 * @version 0.1
 */
public class NotifyChangeSupport {

    ViewModelGameWindow viewModelGameWindow;
    ViewModelLobby viewModelLobby;
    private static NotifyChangeSupport notifyChangeSupport;

    private Boolean lobby;
    private Boolean gamewindow;

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
    }

    public void setViewModelGameWindow(ViewModelGameWindow viewModelGameWindow) {
        this.viewModelGameWindow = viewModelGameWindow;
        this.lobby = false;
        this.gamewindow = true;
    }

    public void notifyInstance() {
        if (lobby) {
            System.out.println("instance notified");
            viewModelLobby.messageToChat();
        }
        if (gamewindow) {
            viewModelGameWindow.messageToChat();
        }
    }
}
