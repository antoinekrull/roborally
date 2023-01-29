package helper;

public class Node implements Comparable<Node>{
    private final int xCoordinate;
    private final int yCoordinate;
    private final int distance;
    private Node lastNode;
    public Node(int xCoordinate, int yCoordinate, int distance, Node lastNode){
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.distance = distance;
        this.lastNode = lastNode;
    }

    @Override
    public int compareTo(Node node) {
        return Integer.compare(this.distance, node.distance);
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public int getDistance() {
        return distance;
    }

    public Node getLastNode() {
        return lastNode;
    }
}
