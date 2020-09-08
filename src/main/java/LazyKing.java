import Tree.IStatusTree;
import Tree.IStatusTreeNode;
import Tree.MyStatusTreeNode;
import Tree.StatusTree;

import java.util.List;

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

    public static void main(String[] args) {
        IStatusTreeNode<String> rootNode = new MyStatusTreeNode<>("король");

        IStatusTree tree = new StatusTree(rootNode);
        IUnluckyVassalService<String> unluckyVassal = new UnluckyVassal();
        unluckyVassal.printResult(tree.getOrganizedNodes(pollResults), "");
    }
}

class UnluckyVassal implements IUnluckyVassalService<String> {
    @Override
    public void printResult(IStatusTreeNode<String> root, String tab) {
        System.out.println(tab + root.toString());
        if (root.hasChildren()) root.getChildren().values().forEach(value -> printResult(value, tab + '\t'));
    }
}