package communication;

    /**
     * This class is used for communication between the server and the client by processing the client's input
     */

    public class MessageCreator {
    //TODO: Rework message creator
        private final String escapeCharacter = "!";
        private String directMessage = "dm";
        private String joinSession = "join";
        private String leaveSession = "quit";
        private String startGame = "start";

        /**
         * Takes input from the client, processes it depending on the input (defined command or his username) and sends the processed data to the server.
         *
         * @param username Client's username as input.
         * @param userInput Client's command to the server as input
         * @return Message Processed client input sent to server.
         */

        public ConcreteMessage generateMessage(String username, String userInput) {
            ConcreteMessage generatedConcreteMessage = new ConcreteMessage();
            generatedConcreteMessage.setUsername(username);
            if(userInput.startsWith(escapeCharacter)) {
                userInput = userInput.substring(1);
                if(userInput.startsWith(directMessage)) {
                    userInput = userInput.substring(3);
                    String target = userInput.substring(0, userInput.indexOf(" "));
                    userInput = userInput.substring(target.length()+1);
                    generatedConcreteMessage.setTarget(target);
                    generatedConcreteMessage.setMessage(userInput);
                    generatedConcreteMessage.setMessageType(MessageType.DIRECT_MESSAGE);
                } else if (userInput.startsWith(joinSession)) {
                    generatedConcreteMessage.setMessageType(MessageType.JOIN_SESSION);
                } else if (userInput.startsWith(leaveSession)) {
                    generatedConcreteMessage.setMessageType(MessageType.LEAVE_SESSION);
                } else if (userInput.startsWith(startGame)) {
                    generatedConcreteMessage.setMessageType(MessageType.START_GAME);
                } else {
                    generatedConcreteMessage.setMessageType(MessageType.INVALID_COMMAND);
                }
            } else {
                generatedConcreteMessage.setMessageType(MessageType.GROUP_CHAT);
                generatedConcreteMessage.setMessage(userInput);
            }
            return generatedConcreteMessage;
        }
    }

