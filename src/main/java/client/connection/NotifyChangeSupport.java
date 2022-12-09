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

    private static NotifyChangeSupport notifyChangeSupport;

    ViewModelGameWindow viewModelGameWindow;
    ViewModelLobby viewModelLobby;

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
    public void notifyInstance() {
        if (lobby) {
            viewModelLobby.messageToChat();
        }
        if (gamewindow) {
            viewModelGameWindow.messageToChat();
        }
    }
}
