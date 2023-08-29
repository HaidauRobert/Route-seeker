package models;

import java.util.Objects;

public class Node {
    int id;
    String name;
    int x;
    int y;
    int nrMap;

    public Node() {}
    public Node(int id, String name, int x, int y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Node(int id, String name, int x, int y, int nrMap) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.nrMap = nrMap;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getNrMap() {
        return nrMap;
    }

    public void setNrMap(int nrMap) {
        this.nrMap = nrMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id == node.id && x == node.x && y == node.y && nrMap == node.nrMap && Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, x, y, nrMap);
    }
}

