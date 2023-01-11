package message;

import java.io.Serializable;

public class Message implements Serializable {

    private MessageType messageType;
    private Object argument;

    public Message(MessageType messageType, Object argument) {
        this.messageType = messageType;
        this.argument = argument;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Object getArgument() {
        return argument;
    }
}
