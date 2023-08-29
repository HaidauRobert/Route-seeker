package models;

import dao.NodeDAO;

import java.sql.SQLException;
import java.util.Objects;

import static java.lang.Math.sqrt;

public class Street {
    int id;
    String name;
    int idNodeStart;
    int idNodeEnd;
    int nrMap;
    int length;

    public Street() {
    }

    public Street(int id, String name, int idNodeStart, int idNodeEnd, int length, int nrMap) {
        this.id = id;
        this.name = name;
        this.idNodeStart = idNodeStart;
        this.idNodeEnd = idNodeEnd;
        this.nrMap = nrMap;
        this.length = length;

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

    public int getIdNodeStart() {
        return idNodeStart;
    }

    public void setIdNodeStart(int idNodeStart) {
        this.idNodeStart = idNodeStart;
    }

    public int getIdNodeEnd() {
        return idNodeEnd;
    }

    public void setIdNodeEnd(int idNodeEnd) {
        this.idNodeEnd = idNodeEnd;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getNrMap() {
        return nrMap;
    }

    public void setNrMap(int nrMap) {
        this.nrMap = nrMap;
    }

    @Override
    public String toString() {
        return "Street{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", idNodeStart=" + idNodeStart +
                ", idNodeEnd=" + idNodeEnd +
                ", nrMap=" + nrMap +
                ", length=" + length +
                '}';
    }

    public int calculateLength(int id1, int id2) throws SQLException {

        NodeDAO nodeDao = new NodeDAO();
        Node nodeStart = nodeDao.findById(id1);
        int x1 = nodeStart.getX();
        int y1 = nodeStart.getY();

        Node nodeEnd = nodeDao.findById(id2);

        int x2 = nodeEnd.getX();
        int y2 = nodeEnd.getY();

        int distance = (int) sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));

        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Street street = (Street) o;
        return id == street.id && idNodeStart == street.idNodeStart && idNodeEnd == street.idNodeEnd && nrMap == street.nrMap && length == street.length && Objects.equals(name, street.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, idNodeStart, idNodeEnd, nrMap, length);
    }
}
