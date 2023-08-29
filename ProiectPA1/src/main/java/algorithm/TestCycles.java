package algorithm;

import dao.NodeDAO;
import models.Node;

import java.sql.SQLException;
import java.util.List;

public class TestCycles {

    public List<List<Node>> getCycles(int nrMap) throws SQLException {

        NodeDAO nodeDAO = new NodeDAO();
        List<Node> nodesList = nodeDAO.generateNodes(nrMap);

        ElementaryCycles elementaryCyclesSearch = new ElementaryCycles(nodesList, nrMap);
        List<List<Node>> cycles = elementaryCyclesSearch.getElementaryCycles();

        return cycles;
    }


}
