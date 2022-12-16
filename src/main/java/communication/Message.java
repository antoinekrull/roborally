package communication;

/**
 * @author Moritz, Firas, Antoine
 * @version 1.0
 */

public class Message {
    public Message() {
    }
    private MessageType messageType;

    private MessageBody messageBody;

    public Message(MessageType messageType, MessageBody messageBody){
        this.messageType = messageType;
        this.messageBody = messageBody;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(MessageBody messageBody) {
        this.messageBody = messageBody;
    }
}
