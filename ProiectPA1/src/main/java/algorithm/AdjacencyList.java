package algorithm;

import dao.NodeDAO;
import dao.StreetDAO;
import models.Node;
import models.Street;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdjacencyList {

    static Graph<Node, Street> graph = new SimpleGraph<>(Street.class);
    static Map<Node, List<Node>> adjList = new HashMap<>();

    public static void createGraph(int nrMap) throws SQLException {

        graph = new SimpleGraph<>(Street.class);
        adjList = new HashMap<>();

        StreetDAO streetDAO = new StreetDAO();
        NodeDAO nodeDAO = new NodeDAO();

        List<Node> nodes = nodeDAO.generateNodes(nrMap);
        List<Street> streets = streetDAO.generateStreets(nrMap);

        for (Node node : nodes) {
            graph.addVertex(node);
        }

        for (Street street : streets) {
            graph.addEdge(nodeDAO.findById(street.getIdNodeStart()), nodeDAO.findById(street.getIdNodeEnd()), street);
        }
    }

    public static Map<Node, List<Node>> getAdjList(int nrMap) throws SQLException {

        createGraph(nrMap);

        for (Node node : graph.vertexSet()) {
            adjList.put(node, new ArrayList<>());

            for (Node nodeAdj : graph.vertexSet()) {
                if (graph.containsEdge(node, nodeAdj)) {
                    adjList.get(node).add(nodeAdj);
                }
            }
        }
        return adjList;
    }
}
