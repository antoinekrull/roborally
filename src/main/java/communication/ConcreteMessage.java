package communication;

/**
 * @author Moritz, Firas, Antoine
 * @version 1.0
 */

public class ConcreteMessage extends Message{
    private String username;
    private String message;
    private String target;
    private MessageType messageType = MessageType.GROUP_CHAT;
    private int playedCard = 0;
    public ConcreteMessage(){}
    public ConcreteMessage(String username, String message
    ){
        this.username = username;
        this.message = message;
    }

    public void setUsername(String newUserName) {
        this.username = newUserName;
    }
    public String getUsername() {
        return username;
    }
    public void setMessage(String newMessage) {
        this.message = newMessage;
    }
    public String getMessage() {
        return message;
    }

    public void setTarget(String newTarget) {
        target = newTarget;
    }
    public String getTarget() {
        return target;
    }
    public void setMessageType(MessageType messageType){ this.messageType = messageType; }
    public MessageType getMessageType(){ return this.messageType; }

    public void setPlayedCard(int cardPosition) {
        playedCard = cardPosition;
    }
    public int getPlayedCard() {
        return playedCard;
    }
}
