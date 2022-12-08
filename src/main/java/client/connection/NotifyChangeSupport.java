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

    Boolean lobby;
    Boolean gamewindow;

    public void notifyInstance() {
        if (lobby) {
            viewModelLobby.messageToChat();
        }
        if (gamewindow) {
            viewModelGameWindow.messageToChat();
        }
    }

    public void setBoolean(Boolean instance1, Boolean instance2) {
        this.lobby = instance1;
        this.gamewindow = instance2;
    }
}
