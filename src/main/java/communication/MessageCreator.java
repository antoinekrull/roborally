package communication;

/**
     * This class is used for communication between the server and the client by processing the client's input
     */

    public class MessageCreator {

        public Message generateHelloClientMessage(String protocolVersion){
            MessageBody messageBody = new MessageBody();
            messageBody.setProtocol(protocolVersion);
            return new Message(MessageType.HelloClient, messageBody);
        }

        public Message generateAliveMessage(){
            MessageBody messageBody = new MessageBody();
            return new Message(MessageType.Alive, messageBody);
        }

        public Message generateHelloServerMessage(String group, boolean isAI, String protocolVersion){
            MessageBody messageBody = new MessageBody();
            messageBody.setGroup(group);
            messageBody.setAI(isAI);
            messageBody.setProtocol(protocolVersion);
            return new Message(MessageType.HelloServer, messageBody);
        }
        public Message generateWelcomeMessage(int clientID){
            MessageBody messageBody = new MessageBody();
            messageBody.setClientID(clientID);
            return new Message(MessageType.Welcome, messageBody);
        }
        public Message generatePlayerValuesMessage(String name, int figure){
            MessageBody messageBody = new MessageBody();
            messageBody.setName(name);
            messageBody.setFigure(figure);
            return new Message(MessageType.PlayerValues, messageBody);
        }
        public Message generatePlayerAddedMessage(String name, int figure, int clientID){
            MessageBody messageBody = new MessageBody();
            messageBody.setName(name);
            messageBody.setFigure(figure);
            messageBody.setClientID(clientID);
            return new Message(MessageType.PlayerAdded, messageBody);
        }
        public Message generateSetStatusMessage(boolean ready){
            MessageBody messageBody = new MessageBody();
            messageBody.setReady(ready);
            return new Message(MessageType.SetStatus, messageBody);
        }
        public Message generatePlayerStatusMessage(int clientID, boolean ready){
            MessageBody messageBody = new MessageBody();
            messageBody.setClientID(clientID);
            messageBody.setReady(ready);
            return new Message(MessageType.PlayerStatus, messageBody);
        }
        public Message generateSelectMapMessage(String[] availableMaps){
            MessageBody messageBody = new MessageBody();
            messageBody.setAvailableMaps(availableMaps);
            return new Message(MessageType.SelectMap, messageBody);
        }
        public Message generateMapSelectedMessage(String map){
            MessageBody messageBody = new MessageBody();
            messageBody.setMap(map);
            return new Message(MessageType.MapSelected, messageBody);
        }
        public Message generateGameStartedMessage(String jsonMap){
            MessageBody messageBody = new MessageBody();
            messageBody.setGameMap(jsonMap);
            return new Message(MessageType.GameStarted, messageBody);
        }
        //method overload: if message is not a private message, the client doesn't specify a clientID and the
        //message is sent to everybody
        public Message generateSendChatMessage(String message){
            MessageBody messageBody = new MessageBody();
            messageBody.setMessage(message);
            messageBody.setTo(-1);
            return new Message(MessageType.SendChat, messageBody);
        }
        //method overload: if message is a private message, the client specifies a clientID
        public Message generateSendChatMessage(String message, int to){
            MessageBody messageBody = new MessageBody();
            messageBody.setMessage(message);
            messageBody.setTo(to);
            return new Message(MessageType.SendChat, messageBody);
        }
        public Message generateReceivedChatMessage(String message, int from, boolean isPrivate){
            MessageBody messageBody = new MessageBody();
            messageBody.setMessage(message);
            messageBody.setFrom(from);
            messageBody.setPrivate(isPrivate);
            return new Message(MessageType.ReceivedChat, messageBody);
        }
        public Message generateErrorMessage(String error){
            MessageBody messageBody = new MessageBody();
            messageBody.setError(error);
            return new Message(MessageType.Error, messageBody);
        }
        public Message generatePlayCardMessage(String card){
            MessageBody messageBody = new MessageBody();
            messageBody.setCard(card);
            return new Message(MessageType.PlayCard, messageBody);
        }
        public Message generateCardPlayedMessage(String card, int clientID){
            MessageBody messageBody = new MessageBody();
            messageBody.setCard(card);
            messageBody.setClientID(clientID);
            return new Message(MessageType.CardPlayed, messageBody);
        }
        public Message generateCurrentPlayerMessage(int clientID){
            MessageBody messageBody = new MessageBody();
            messageBody.setClientID(clientID);
            return new Message(MessageType.CurrentPlayer, messageBody);
        }
        public Message generateActivePhaseMessage(int phase){
            MessageBody messageBody = new MessageBody();
            messageBody.setClientID(phase);
            return new Message(MessageType.ActivePhase, messageBody);
        }
        public Message generateSetStartingPointMessage(int x, int y){
            MessageBody messageBody = new MessageBody();
            messageBody.setX(x);
            messageBody.setY(y);
            return new Message(MessageType.SetStartingPoint, messageBody);
        }
        public Message generateStartingPointTakenMessage(int x, int y, int clientID){
            MessageBody messageBody = new MessageBody();
            messageBody.setX(x);
            messageBody.setY(y);
            messageBody.setClientID(clientID);
            return new Message(MessageType.StartingPointTaken, messageBody);
        }
        public Message generateYourCardsMessage(String[] cardsInHand){
            MessageBodyCardsInHandStringArray messageBody = new MessageBodyCardsInHandStringArray();
            messageBody.setCardsInHand(cardsInHand);
            return new Message(MessageType.YourCards, messageBody);
        }
        public Message generateNotYourCardsMessage(int clientID, int cardsInHand){
            MessageBodyCardsInHandInteger messageBody = new MessageBodyCardsInHandInteger();
            messageBody.setClientID(clientID);
            messageBody.setCardsInHand(cardsInHand);
            return new Message(MessageType.NotYourCards, messageBody);
        }
        public Message generateShuffleCodingMessage(int clientID){
            MessageBody messageBody = new MessageBody();
            messageBody.setClientID(clientID);
            return new Message(MessageType.ShuffleCoding, messageBody);
        }
        public Message generateSelectedCardMessage(String card, int register){
            MessageBody messageBody = new MessageBody();
            messageBody.setCard(card);
            messageBody.setRegister(register);
            return new Message(MessageType.SelectedCard, messageBody);
        }
        public Message generateCardSelectedMessage(int clientID, int register, boolean filled){
            MessageBody messageBody = new MessageBody();
            messageBody.setClientID(clientID);
            messageBody.setRegister(register);
            messageBody.setFilled(filled);
            return new Message(MessageType.CardSelected, messageBody);
        }
        public Message generateSelectionFinishedMessage(int clientID){
            MessageBody messageBody = new MessageBody();
            messageBody.setClientID(clientID);
            return new Message(MessageType.SelectionFinished, messageBody);
        }
        public Message generateTimerStartedMessage(){
            MessageBody messageBody = new MessageBody();
            return new Message(MessageType.TimerStarted, messageBody);
        }
        public Message generateTimerEndedMessage(int[] clientIDs){
            MessageBody messageBody = new MessageBody();
            messageBody.setClientIDs(clientIDs);
            return new Message(MessageType.TimerEnded, messageBody);
        }
        public Message generateCardsYouGotNowMessage(String[] cards){
            MessageBodyCardsInHandStringArray messageBody = new MessageBodyCardsInHandStringArray();
            messageBody.setCardsInHand(cards);
            return new Message(MessageType.CardsYouGotNow, messageBody);
        }
        public Message generateCurrentCardsMessage(Object[] activeCards){
            MessageBody messageBody = new MessageBody();
            messageBody.setActiveCards(activeCards);
            return new Message(MessageType.CurrentCards, messageBody);
        }
        public Message generateReplaceCardMessage(int register, String newCard, int clientID){
            MessageBody messageBody = new MessageBody();
            messageBody.setRegister(register);
            messageBody.setNewCard(newCard);
            messageBody.setClientID(clientID);
            return new Message(MessageType.ReplaceCard, messageBody);
        }
        public Message generateMovementMessage(int clientID, int x, int y){
            MessageBody messageBody = new MessageBody();
            messageBody.setClientID(clientID);
            messageBody.setX(x);
            messageBody.setY(y);
            return new Message(MessageType.Movement, messageBody);
        }
        public Message generatePlayerTurningMessage(int clientID, String rotation){
            MessageBody messageBody = new MessageBody();
            messageBody.setClientID(clientID);
            messageBody.setRotation(rotation);
            return new Message(MessageType.PlayerTurning, messageBody);
        }
        public Message generateAnimationMessage(String type){
            MessageBody messageBody = new MessageBody();
            messageBody.setType(type);
            return new Message(MessageType.Animation, messageBody);
        }
        public Message generateRebootMessage(int clientID){
            MessageBody messageBody = new MessageBody();
            messageBody.setClientID(clientID);
            return new Message(MessageType.Reboot, messageBody);
        }
        public Message generateRebootDirectionMessage(String direction){
            MessageBody messageBody = new MessageBody();
            messageBody.setDirection(direction);
            return new Message(MessageType.RebootDirection, messageBody);
        }
        public Message generateEnergyMessage(int clientID, int count, String source){
            MessageBody messageBody = new MessageBody();
            messageBody.setClientID(clientID);
            messageBody.setCount(count);
            messageBody.setSource(source);
            return new Message(MessageType.Energy, messageBody);
        }
        public Message generateCheckPointReachedMessage(int clientID, int number){
            MessageBody messageBody = new MessageBody();
            messageBody.setClientID(clientID);
            messageBody.setNumber(number);
            return new Message(MessageType.CheckPointReached, messageBody);
        }
        public Message generateGameFinishedMessage(int clientID){
            MessageBody messageBody = new MessageBody();
            messageBody.setClientID(clientID);
            return new Message(MessageType.GameFinished, messageBody);
        }
    }

