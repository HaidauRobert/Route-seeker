package algorithm;

import dao.NodeDAO;
import dao.StreetDAO;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import models.Node;
import models.Street;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;
import utils.AlertMessage;

import java.sql.SQLException;
import java.util.*;

public class Route {
    Graph<Node, Street> graph = new SimpleGraph<>(Street.class);
    NodeDAO nodeDAO = new NodeDAO();

    public void createGraph(int nrMap) throws SQLException {

        StreetDAO streetDAO = new StreetDAO();

        List<Node> nodes = nodeDAO.generateNodes(nrMap);
        List<Street> streets = streetDAO.generateStreets(nrMap);

        for (Node node : nodes) {
            graph.addVertex(node);
        }

        for (Street street : streets) {
            graph.addEdge(nodeDAO.findById(street.getIdNodeStart()), nodeDAO.findById(street.getIdNodeEnd()), street);
        }

    }

    public List<Node> getCyclesFromNode(int idStartNode, int nrMap, int searchedLength, List<List<Node>> foundCycles) throws SQLException {

        createGraph(nrMap);
        int nodeLengthList = 0;

        for (List<Node> list : foundCycles) {
            System.out.println(list);
            System.out.println(calculateLength(list));
        }

        Node startNode = nodeDAO.findById(idStartNode);
        for (List<Node> list : foundCycles) {
            nodeLengthList = calculateLength(list);
            if (list.contains(startNode)) {
                if (searchedLength >= nodeLengthList - 200 && searchedLength <= nodeLengthList + 200) {
                    {
                        AlertMessage.messageBox("Lungimea rutei gasite este de: " + nodeLengthList);
                        return list;
                    }
                }
            }
        }

        return new ArrayList<>();


    }

    public int calculateLength(List<Node> nodeList) {

        int distance = 0;
        Node previousNode = nodeList.get(0);
        for (int index = 1; index < nodeList.size(); index++) {
            distance += graph.getEdge(previousNode, nodeList.get(index)).getLength();
            previousNode = nodeList.get(index);
        }
        distance += graph.getEdge(previousNode, nodeList.get(0)).getLength();
        return distance;
    }


    /* void dfs(int src, int parent, Stack<Integer> stack, Map<Integer, Integer> isVisited) throws SQLException {
        isVisited.put(src, 1);
        stack.push(src);
        Node foundNode = nodeDAO.findById(src);
        for (Node node : graph.vertexSet()){
            if (graph.containsEdge(node, foundNode)){
                if (isVisited.get(node.getId()) == 0) {
                    dfs(node.getId(), src, stack, isVisited);
                }
                else  if (isVisited.get(node.getId()) == 1){
                    if(node.getId() != parent){
                        ArrayList<Node> nodeList = new ArrayList<>();
                        nodeList.add(node);
                        int pos = stack.size()-1;
                        while(pos >= 0 && stack.get(pos) != node.getId()) {
                            nodeList.add(nodeDAO.findById(stack.get(pos)));
                            pos--;
                        }

                        foundCycles.add(nodeList);
                    }
                }
            }

        }
        isVisited.put(src,2);
        stack.pop();
    } */


}


