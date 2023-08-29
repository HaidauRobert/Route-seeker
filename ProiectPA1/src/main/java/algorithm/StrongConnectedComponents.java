package algorithm;


import dao.NodeDAO;
import models.Node;

import java.sql.SQLException;
import java.util.*;

public class StrongConnectedComponents {

    NodeDAO nodeDao = new NodeDAO();
    private Map<Node, List<Node>> adjMainList = new HashMap<>();
    private Map<Node, List<Node>> adjCurrentList = new HashMap<>();
    private Map<Integer, Boolean> visited = new HashMap<>();
    private Stack<Integer> stack = null;
    private Map<Integer, Integer> lowLink = null;
    private Map<Integer, Integer> number = null;
    private int sccCounter = 0;
    private List<List<Integer>> currentSCCs = null;

    public StrongConnectedComponents(Map<Node, List<Node>> adjList) {

        this.adjMainList = adjList;
        for (Map.Entry<Node, List<Node>> pair : adjMainList.entrySet()) {
            visited.put(pair.getKey().getId(), false);
        }
    }

    public SCCResult getAdjacencyList(int node) throws SQLException {

        this.lowLink = new HashMap<>(adjMainList.size());
        this.number = new HashMap<>(adjMainList.size());
        this.stack = new Stack<>();
        this.currentSCCs = new ArrayList<>();

        this.makeAdjListSubgraph(node);

        for (Map.Entry<Node, List<Node>> pair : adjMainList.entrySet()) {
            if (!visited.get(pair.getKey().getId())) {
                getStrongConnectedComponents(pair.getKey().getId());
                List<Integer> nodes = getLowestIdComponent();

                if (!nodes.isEmpty() && !nodes.contains(node) && !nodes.contains(node + 1)) {
                    return getAdjacencyList(node + 1);
                } else {
                    Map<Node, List<Node>> adjacencyList = getAdjList(nodes);
                    if (adjacencyList != null) {
                        for (Map.Entry<Node, List<Node>> pairNode : adjMainList.entrySet()) {
                            if (pairNode.getValue().size() > 0) {
                                return new SCCResult(adjacencyList, pairNode.getKey().getId());
                            }
                        }

                    }
                }
            }
        }


        return null;
    }

    private void makeAdjListSubgraph(int idNode) throws SQLException {

        adjCurrentList = new HashMap<>();
        for (Map.Entry<Node, List<Node>> pair : adjMainList.entrySet()) {
            List<Integer> successors = new ArrayList<>();
            for (Node node : pair.getValue()) {
                if (node.getId() >= idNode) {
                    successors.add(node.getId());
                }
            }
            if (successors.size() > 0) {
                adjCurrentList.put(pair.getKey(), new ArrayList<>());
                for (Integer id : successors) {
                    adjCurrentList.get(pair.getKey()).add(nodeDao.findById(id));
                }
            }

        }

    }

    private List<Integer> getLowestIdComponent() {
        int minimId = 100;
        for (Map.Entry<Node, List<Node>> pair : adjMainList.entrySet()) {
            minimId = Math.min(minimId, pair.getKey().getId());
        }
        int min = this.adjCurrentList.size() + minimId;
        List<Integer> currentSCC = new ArrayList<>();

        for (List<Integer> listSCC : currentSCCs) {
            for (Integer ind : listSCC) {
                if (ind < min) {
                    currentSCC = listSCC;
                    min = ind;
                }
            }
        }
        return currentSCC;
    }

    private Map<Node, List<Node>> getAdjList(List<Integer> nodes) throws SQLException {
        Map<Node, List<Node>> lowestIdAdjacencyList = null;

        if (!nodes.isEmpty()) {
            lowestIdAdjacencyList = new HashMap<>(adjMainList.size());

            for (Integer idNode : nodes) {
                lowestIdAdjacencyList.put(nodeDao.findById(idNode), new ArrayList<>());

                Node node = nodeDao.findById(idNode);
                for (Node nodeAdj : adjCurrentList.get(node)) {
                    if (nodes.contains(nodeAdj.getId())) {
                        lowestIdAdjacencyList.get(nodeDao.findById(idNode)).add(nodeAdj);
                    }
                }
            }

        }

        return lowestIdAdjacencyList;
    }

    private void getStrongConnectedComponents(int root) throws SQLException {
        this.sccCounter++;
        this.lowLink.put(root, sccCounter);
        this.number.put(root, sccCounter);
        this.visited.put(root, true);
        this.stack.push(root);

        Node nodeRoot = nodeDao.findById(root);
        for (Node node : adjCurrentList.get(nodeRoot)) {
            int idNode = node.getId();
            if (!visited.get(idNode)) {
                getStrongConnectedComponents(idNode);
                lowLink.put(root, Math.min(lowLink.get(root), lowLink.get(idNode)));
            } else if (number.get(idNode) < number.get(root)) {
                lowLink.put(root, Math.min(lowLink.get(root), number.get(idNode)));
            }
        }


        if ((lowLink.get(root) == number.get(root)) && (stack.size() > 0)) {
            int next = -1;
            List<Integer> scc = new ArrayList<>();
            do {
                next = stack.peek();
                stack.pop();
                scc.add(next);
            } while (number.get(next) > number.get(root));

            if (scc.size() > 1) {
                this.currentSCCs.add(scc);
            }
        }
    }
}