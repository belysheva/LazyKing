import java.util.List;
import java.util.stream.Stream;

public class StatusTree {
    private IStatusTreeNode<String> rootNode = new MyStatusTreeNode<>("король");

    public void organizeNodes(List<String> pollResults) { pollResults.forEach(entity -> {
            String[] subVassals = prepareVassalList(entity);
            IStatusTreeNode<String> currentNode = defineCurrentNode(getCurrentVassalName(0, subVassals));
            addSubVassalsToCurrentNode(subVassals, currentNode);
        });
    }

    private void addSubVassalsToCurrentNode(String[] vassals, IStatusTreeNode<String> currentNode) {
        Stream.of(vassals).skip(1).map(String::trim).forEach(subVassal -> addVassalToCurrentNode(currentNode, subVassal));
    }

    private void addVassalToCurrentNode(IStatusTreeNode<String> currentNode, String subVassalName) {
        IStatusTreeNode<String> subVassalConnectedNode = findIfPresentConnectedNode(this.rootNode, subVassalName);
        currentNode.addChild(subVassalConnectedNode != null ? subVassalConnectedNode : newConnectedNode(currentNode, subVassalName));
    }

    private String getCurrentVassalName(int i, String[] vassals) {
        return vassals[i].trim();
    }

    private String[] prepareVassalList(String entity) {
        return entity.split("[:,]");
    }

    private IStatusTreeNode<String> defineCurrentNode(String vassalName) {
        IStatusTreeNode<String> connectedNode = findIfPresentConnectedNode(this.rootNode, vassalName);
        return connectedNode != null ? connectedNode : newConnectedNode(this.rootNode,vassalName);
    }

    private IStatusTreeNode<String> newConnectedNode(IStatusTreeNode<String> currentNode, String vassalName) {
        IStatusTreeNode<String> vassalNode = new MyStatusTreeNode<>(vassalName, currentNode);
        currentNode.addChild(vassalNode);
        return vassalNode;
    }

    //search if such node exists recursively and return found node
    private IStatusTreeNode<String> findIfPresentConnectedNode(IStatusTreeNode<String> rootNode, String vassalName) {
        if (rootNode.hasChild(vassalName)) {
            return rootNode.getChildren().get(vassalName);
        } else {
            for (IStatusTreeNode<String> currentNode : rootNode.getChildren().values()) {
                IStatusTreeNode<String> processingNode = findIfPresentConnectedNode(currentNode, vassalName);
                if (processingNode != null) return processingNode;
            }
        }
        return null;
    }

    public IStatusTreeNode<String> getRootNode() { return rootNode; }
}
