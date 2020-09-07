import java.util.List;
import java.util.stream.Stream;

public class LazyKing {
    private static List<String> pollResults = List.of(
            "служанка Аня",
            "экономка Лидия Федоровна: дворник Гена, служанка Аня",
            "кот Василий: человеческая особь Катя",
            "доярка Нюра",
            "дворник Гена: посыльный Тошка",
            "киллер Гена",
            "зажиточный холоп: крестьянка Таня",
            "шпион Т: кучер Д",
            "секретарь короля: зажиточный холоп, шпион Т",
            "посыльный Тошка: кот Василий",
            "аристократ Клаус",
            "управляющий Семен Семеныч: крестьянин Федя, доярка Нюра",
            "дворянин Кузькин: управляющий Семен Семеныч, жена Кузькина, экономка Лидия Федоровна",
            "просветленный Антон"
    );
    static IStatusTreeNode<String> rootNode = new MyStatusTreeNode<>("король");

    public static void main(String[] args) {
        IUnluckyVassalService<String> unluckyVassal = new UnluckyVassal();
        unluckyVassal.printReportForKing(rootNode, pollResults);
    }
}

class UnluckyVassal implements IUnluckyVassalService<String> {

    @Override
    public void printReportForKing(IStatusTreeNode<String> rootNode, List<String> pollResults) {
        printResult(generateStatusTree(rootNode, pollResults), "\t");
    }

    private IStatusTreeNode<String> generateStatusTree(IStatusTreeNode<String> rootNode, List<String> pollResults) {
        pollResults.forEach(entity -> {
            String[] subVassals = prepareVassalList(entity);
            IStatusTreeNode<String> currentNode = defineCurrentNode(rootNode, getCurrentVassalName(0, subVassals));
            addSubVassalsToCurrentNode(rootNode, subVassals, currentNode);
        });
        return rootNode;
    }

    private void addSubVassalsToCurrentNode(IStatusTreeNode<String> rootNode, String[] vassals, IStatusTreeNode<String> currentNode) {
        Stream.of(vassals).skip(1).map(String::trim).forEach(subVassal -> addVassalToCurrentNode(rootNode, currentNode, subVassal));
    }

    private void addVassalToCurrentNode(IStatusTreeNode<String> rootNode, IStatusTreeNode<String> currentNode, String subVassalName) {
        IStatusTreeNode<String> subVassalConnectedNode = findIfPresentConnectedNode(rootNode, subVassalName);
        currentNode.addChild(subVassalConnectedNode != null ? subVassalConnectedNode : newConnectedNode(currentNode, subVassalName));
    }

    private String getCurrentVassalName(int i, String[] vassals) {
        return vassals[i].trim();
    }

    private String[] prepareVassalList(String entity) {
        return entity.split("[:,]");
    }

    private IStatusTreeNode<String> defineCurrentNode(IStatusTreeNode<String> rootNode, String vassalName) {
        IStatusTreeNode<String> connectedNode = findIfPresentConnectedNode(rootNode, vassalName);
        return connectedNode != null ? connectedNode : newConnectedNode(rootNode, vassalName);
    }

    private IStatusTreeNode<String> newConnectedNode(IStatusTreeNode<String> rootNode, String vassalName) {
        IStatusTreeNode<String> vassalNode = new MyStatusTreeNode<>(vassalName, rootNode);
        rootNode.addChild(vassalNode);
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

    private void printResult(IStatusTreeNode<String> root, String tab) {
        System.out.println(tab + root.toString());
        if (root.hasChildren()) root.getChildren().values().forEach(value -> printResult(value, tab + '\t'));
    }
}