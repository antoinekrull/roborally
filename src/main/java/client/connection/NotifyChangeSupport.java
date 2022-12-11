package client.connection;

import client.viewmodel.ViewModelGameWindow;
import client.viewmodel.ViewModelLobby;

/**
 * Notifier class. Notifies specific instances for changes.
 *
 * @author Tobias
 * @version 0.1
 */
public class NotifyChangeSupport {

    ViewModelGameWindow viewModelGameWindow;
    ViewModelLobby viewModelLobby;
    private static NotifyChangeSupport notifyChangeSupport;

    private Boolean lobby = false;
    private Boolean gamewindow = false;

    private NotifyChangeSupport() {

    }
    public static NotifyChangeSupport getInstance() {
        if (notifyChangeSupport == null) {
            notifyChangeSupport = new NotifyChangeSupport();
        }
        return notifyChangeSupport;
    }

    public void setBoolean(String instance) {
        if(instance.equals("ViewModelLobby")) {
            gamewindow = false;
            lobby = true;
        }
        if(instance.equals("ViewModelGameWindow")) {
            lobby = false;
            gamewindow = true;
        }
    }

    public void setViewModelLobby(ViewModelLobby viewModelLobby) {
        this.viewModelLobby = viewModelLobby;
    }

    public void setViewModelGameWindow(ViewModelGameWindow viewModelGameWindow) {
        this.viewModelGameWindow = viewModelGameWindow;
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
