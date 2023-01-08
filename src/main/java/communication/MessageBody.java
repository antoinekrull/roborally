package communication;

/**
 * @author Antoine, Firas
 * @version 1.0
 * Serves to hold all possible message (beside cardsInHand attribute, resolved by the child classes) information.
 * The needed attributes are set depending on the MessageType.
 */
public class MessageBody {

    private String protocol;
    private String group;
    private boolean isAI;
    private int clientID;
    private String name;
    private int figure;
    private boolean ready;
    private String[] availableMaps;
    private String map;
    private String message;
    private int to;
    private int from;
    private boolean isPrivate;
    private String error;
    private String card;
    private int phase;
    private int x;
    private int y;
    private int register;
    private boolean filled;
    private int[] clientIDs;
    private Object[] activeCards;
    private String newCard;
    private String rotation;
    private String type;
    private String direction;
    private int count;
    private String source;
    private String gameMap; //TODO: must be a jsonObject, think about implementation
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getGameMap() {
        return gameMap;
    }

    public void setGameMap(String gameMap) {
        this.gameMap = gameMap;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isAI() {
        return isAI;
    }

    public void setAI(boolean AI) {
        isAI = AI;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFigure() {
        return figure;
    }

    public void setFigure(int figure) {
        this.figure = figure;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public String[] getAvailableMaps() {
        return availableMaps;
    }

    public void setAvailableMaps(String[] availableMaps) {
        this.availableMaps = availableMaps;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRegister() {
        return register;
    }

    public void setRegister(int register) {
        this.register = register;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public int[] getClientIDs() {
        return clientIDs;
    }

    public void setClientIDs(int[] clientIDs) {
        this.clientIDs = clientIDs;
    }

    public Object[] getActiveCards() {
        return activeCards;
    }

    public void setActiveCards(Object[] activeCards) {
        this.activeCards = activeCards;
    }

    public String getNewCard() {
        return newCard;
    }

    public void setNewCard(String newCard) {
        this.newCard = newCard;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
