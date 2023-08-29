package algorithm;

import models.Node;

import java.util.*;

public class SCCResult {
    private Set<Integer> nodeIdsOfSCC = null;
    private Map<Node, List<Node>> adjList = null;
    private int lowestNodeId = -1;

    public SCCResult(Map<Node, List<Node>> adjList, int lowestNodeId) {
        this.adjList = adjList;
        this.lowestNodeId = lowestNodeId;
        this.nodeIdsOfSCC = new HashSet();

        if (this.adjList != null) {
            for (Map.Entry<Node, List<Node>> pair : adjList.entrySet()) {
                if (pair.getKey().getId() >= this.lowestNodeId) {
                    if (pair.getValue().size() > 0) {
                        this.nodeIdsOfSCC.add(pair.getKey().getId());
                    }
                }
            }
        }
    }

    public Map<Node, List<Node>> getAdjList() {
        return adjList;
    }

    public int getLowestNodeId() {
        return lowestNodeId;
    }
}
