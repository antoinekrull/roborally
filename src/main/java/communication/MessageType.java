package communication;

/**
 * @author Antoine, Moritz
 * @version 1.0
 */

/**
 * Serves to categorize messages so that the server can process them accordingly.
 */
public enum MessageType {
    DIRECT_MESSAGE,
    JOIN_SESSION,
    LEAVE_SESSION,
    GROUP_CHAT,
    START_GAME,
    INVALID_COMMAND,
    USERNAME_COMMAND
}

