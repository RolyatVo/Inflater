package Inflater;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Collections;

public class AStar {
    int[][] map;
    private Node[][] pathMap = new Node[15][20];
    int v =0;

    public AStar(int[][] map) {
        this.map = map;
        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < 20; x++) {

                if (((y +1 < 15) &&(map[y][x] == 0 && map[y+1][x] != 0 && map[y+1][x] != 139 && map[y+1][x] != 186 )) || map[y][x] == 71 || map[y][x] == 139 || map[y][x] == 186) {
                    pathMap[y][x] = new Node(x, y);
                    pathMap[y][x].setWalkable(true);
                } else {
                    pathMap[y][x] = new Node(x, y);
                    pathMap[y][x].setWalkable(false);
                }

                v++;
            }
        }
    }

    public List<Node> aStarSearch(int startX, int startY, int destX, int destY) {
        if(startX < 0 || startX > 19 || startY < 0 || startY > 14) { return null; }
        Node start = pathMap[startY][startX];
        Node dest = pathMap[destY][destX];

        PriorityQueue<Node> open = new PriorityQueue<>();
        List<Node> closed = new ArrayList<>();
        start.setG(0);
        start.setCost(h(start,dest));
        open.add(start);

        while (!open.isEmpty()) {
            Node current = open.poll();
            closed.add(current);

            if (current.getX() == dest.getX() && current.getY() == dest.getY()) {
                return getPath(start, dest);
            }

            List<Node> neighbors = getNeighbors(current);

            for (Node neighbor : neighbors) {
                if (closed.contains(neighbor)) {
                    continue;
                }
                float neighborCost = current.getG() + 1;

                if (neighborCost < neighbor.getG()) {
                    neighbor.setG(neighborCost);
                    neighbor.setCost(neighbor.getG() + h(neighbor, dest));
                    neighbor.setParent(current);
                }
                if (!open.contains(neighbor)) open.add(neighbor);
            }

        }
        return null;
    }
    public List<Node> randomPath(int startX, int startY, int destX, int destY) {
        if(startX < 0 || startX > 19 || startY < 0 || startY > 14) { return null; }
        Node start = pathMap[startY][startX];
        Node dest = getRandomNode(destX, destY);

        PriorityQueue<Node> open = new PriorityQueue<>();
        List<Node> closed = new ArrayList<>();
        start.setG(0);
        start.setCost(h(start,dest));
        open.add(start);

        while (!open.isEmpty()) {
            Node current = open.poll();
            closed.add(current);

            if (current.getX() == dest.getX() && current.getY() == dest.getY()) {
                return getPath(start, dest);
            }

            List<Node> neighbors = getNeighbors(current);

            for (Node neighbor : neighbors) {
                if (closed.contains(neighbor)) {
                    continue;
                }
                float neighborCost = current.getG() + 1;

                if (neighborCost < neighbor.getG()) {
                    neighbor.setG(neighborCost);
                    neighbor.setCost(neighbor.getG() + h(neighbor, dest));
                    neighbor.setParent(current);
                }
                if (!open.contains(neighbor)) open.add(neighbor);
            }

        }
        return null;
    }

    private List<Node> getPath(Node start, Node dest) {
        List<Node> path = new ArrayList<>();
        Node current = dest;

        while (current != start && current.getParent() != null) {
            path.add(current);
            current = current.getParent();
        }
        Collections.reverse(path);
        return path;
    }

    private List<Node> getNeighbors(Node current) {
        List<Node> neighbors = new ArrayList<>();

        if (current.getX() - 1 > 0 && pathMap[current.getY()][current.getX()-1].isWalkable())
            neighbors.add(pathMap[current.getY()][current.getX()-1]);
        if (current.getX() + 1 < 20 && pathMap[current.getY()][current.getX()+1].isWalkable())
            neighbors.add(pathMap[current.getY()][current.getX()+1]);
        if (current.getY() - 1 > 0 && current.getX() < 20&& pathMap[current.getY()-1][current.getX()].isWalkable())
            neighbors.add(pathMap[current.getY()-1][current.getX()]);
        if (current.getY() + 1 < 15 && current.getX() < 20 && pathMap[current.getY()+1 ][current.getX()].isWalkable())
            neighbors.add(pathMap[current.getY()+1][current.getX()]);

        return neighbors;
    }

    private int h(Node current, Node dest) {
        int x = Math.abs(current.getX() - dest.getX());
        int y = Math.abs(current.getY() - dest.getY());

        return x + y;
    }
    private int getRandomInt(int max, int min) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }
    private Node getRandomNode(int runnerX, int runnerY) {
        Node random = null;

        while(true){
            int x = getRandomInt(19,1);
            int y = getRandomInt(14,1);
            if((x != runnerX || y != runnerY) && pathMap[y][x].isWalkable()) {
                random = pathMap[y][x];
                break;
            }
        }

        System.out.printf("RANDOM: %d %d", random.getX(), random.getY());
        System.out.println("");
        return random;
    }

    public void printPathMap() {
        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < 20; x++) {
                int W = pathMap[y][x].isWalkable() ? 1 : 0;
                System.out.printf("%3d", W);
            }
            System.out.println("");
        }
    }

}
