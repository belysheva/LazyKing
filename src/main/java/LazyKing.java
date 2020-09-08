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
        IUnluckyVassalService<String> unluckyVassal = new UnluckyVassal(rootNode);
        unluckyVassal.printReportForKing(pollResults);
    }
}

class UnluckyVassal implements IUnluckyVassalService<String> {
    private IStatusTreeNode<String> rootNode;

    public UnluckyVassal(IStatusTreeNode<String> rootNode) {
        this.rootNode = rootNode;
    }

    @Override
    public void printReportForKing(List<String> pollResults) {
        generateStatusTree(pollResults);
        printResult(this.rootNode, "\t");
    }

    private void generateStatusTree(List<String> pollResults) {
        pollResults.forEach(entity -> {
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

    private void printResult(IStatusTreeNode<String> root, String tab) {
        System.out.println(tab + root.toString());
        if (root.hasChildren()) root.getChildren().values().forEach(value -> printResult(value, tab + '\t'));
    }
}