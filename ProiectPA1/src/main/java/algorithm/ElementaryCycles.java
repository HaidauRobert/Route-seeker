package algorithm;

import dao.NodeDAO;
import models.Node;

import java.sql.SQLException;
import java.util.*;

public class ElementaryCycles {

    private List<List<Node>> cycles = new ArrayList<>();
    private Map<Node, List<Node>> adjList = new HashMap<>();
    private List<Node> graphNodes = new ArrayList<>();
    private NodeDAO nodeDao = new NodeDAO();
    private Map<Node, Boolean> blocked = new HashMap<>();
    private Map<Node, List<Node>> blockedMap = new HashMap<>();
    private List<Integer> stack = new ArrayList<>();

    public ElementaryCycles(List<Node> graphNodes, int nrMap) throws SQLException {
        this.graphNodes = graphNodes;
        this.adjList = AdjacencyList.getAdjList(nrMap);
    }

    /**
     * @return lista de cicluri elementare
     * @throws SQLException
     */
    public List<List<Node>> getElementaryCycles() throws SQLException {
        this.cycles = new ArrayList<>();
        this.blocked = new HashMap<>(graphNodes.size());
        this.blockedMap = new HashMap<>(graphNodes.size());
        this.stack = new ArrayList<>();

        StrongConnectedComponents strongConnComp = new StrongConnectedComponents(this.adjList);
        int startNode = graphNodes.get(0).getId();

        while (true) {
            SCCResult sccResult = strongConnComp.getAdjacencyList(startNode);

            if (sccResult != null && sccResult.getAdjList() != null) {
                Map<Node, List<Node>> scc = sccResult.getAdjList();
                startNode = sccResult.getLowestNodeId();

                for (Map.Entry<Node, List<Node>> pair : scc.entrySet()) {
                    if (!pair.getValue().isEmpty() && pair.getValue().size() > 0) {
                        blocked.put(pair.getKey(), false);
                        blockedMap.put(pair.getKey(), new ArrayList<>());
                    }
                }

                this.findCycles(startNode, startNode, scc);
                startNode++;
            } else {
                break;
            }
        }

        return this.cycles;
    }

    /**
     * @param currentNode nodul pe care vrem ca ciclul sa l contina
     * @param startNode nodul de start al ciclului
     * @param adjList lista de adiacenta a comp tare conexe
     * @return
     * @throws SQLException
     */
    private boolean findCycles(int currentNode, int startNode, Map<Node, List<Node>> adjList) throws SQLException {
        boolean flag = false;
        this.stack.add(currentNode);
        blocked.put(nodeDao.findById(currentNode), true);

        Node nodeRoot = nodeDao.findById(currentNode);
        for (Node node : adjList.get(nodeRoot)) {
            int idNode = node.getId();
            if (idNode == startNode) {
                List<Node> cycle = new ArrayList<>();
                for (int pos = 0; pos < this.stack.size(); pos++) {
                    int index = this.stack.get(pos);
                    cycle.add(nodeDao.findById(index));
                }
                cycles.add(cycle);
                flag = true;

            } else if (!blocked.get(nodeDao.findById(idNode))) {
                if (findCycles(idNode, startNode, adjList)) {
                    flag = true;
                }
            }
        }

        if (flag) {
            unblock(currentNode);
        } else {
            for (Node node : adjList.get(nodeDao.findById(currentNode))) {

                if (!blockedMap.get(node).contains(currentNode)) {
                    blockedMap.get(node).add(nodeDao.findById(currentNode));
                }
            }
        }

        this.stack.remove((Integer) currentNode);
        return flag;
    }

    private void unblock(int node) throws SQLException {

        Node foundNode = nodeDao.findById(node);
        blocked.put(foundNode, false);

        List<Node> blockedNodes = blockedMap.get(foundNode);

        while (blockedNodes.size() > 0) {
            Node nodeB = blockedNodes.get(0);
            blockedNodes.remove(0);
            if (blocked.get(nodeB))  unblock(nodeB.getId());
        }
    }
}


