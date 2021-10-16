package Inflater;

public class Node implements Comparable<Node>{
    private int x, y;
    private float cost;
    private boolean walkable;

    private float g;
    private float h;
    private Node parent;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.g = 9999f;
        this.cost = 9999f;
    }

    public void  setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public void setG(float g) {
        this.g = g;
    }

    public void setH(float h) {
        this.h = h;
    }

    @Override
    public int compareTo(Node n) {
        return Float.compare(this.cost, n.cost);
    }

    public int getX() {
        return x;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public float getG() {
        return g;
    }

    public float getH() {
        return h;
    }

    public float getCost() {
        return cost;
    }

    public Node getParent() {
        return parent;
    }

    public int getY() {
        return y;
    }

    public float getFCost() { return g + h; }
 }
