import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//TODO remove hardcoded test data to file
public class LazyKingTest {

    private static List<String> pollResults = new ArrayList<>();
    static StatusTree tree;

    @BeforeEach
    public void prepareNode() {
        pollResults = new ArrayList<>();
        tree = new StatusTree();
        System.out.println("--------------------------------------");
    }

    @Test
    public void rootNodeAddingTest() {
        System.out.println("  tree.getOrganizedNodes(pollResults) adding");
        assertEquals(tree.getOrganizedNodes(pollResults).toString(), "король");
    }

    @Test
    public void checkChildWhen1stNodeAddedTest() {
        System.out.println("Check 1st child node creation");
        pollResults = List.of("служанка Аня");
        assertEquals(tree.getOrganizedNodes(pollResults).amountOfChildren(), 1, "Wrong number of children");
    }

    @Test
    public void rootNodeChildrenNegativeTest() {
        System.out.println("Check king doesn't have children");
        assertFalse(tree.getOrganizedNodes(pollResults).hasChildren(), String.format("Expected result: Root node should have %d, it has %d children nodes", 0, tree.getOrganizedNodes(pollResults).amountOfChildren()));
    }


    @Test
    public void duplicatedNodesCreationTest() {
        System.out.println("Check duplicated nodes creation");
        pollResults = List.of("служанка Аня", "служанка Аня");
        assertEquals(tree.getOrganizedNodes(pollResults).amountOfChildren(), 1);
    }

    @Test
    public void secondLevelNodeAddTest() {
        System.out.println("Check the Node with subVassals added successfully");
        pollResults = List.of("управляющий Семен Семеныч: крестьянин Федя, доярка Нюра");
        assertTrue(tree.getOrganizedNodes(pollResults).hasChild("управляющий Семен Семеныч"));
    }

    @Test
    public void vassalsAmountTest() {
        System.out.println("Check amount of vassals for 2nd level node");
        pollResults = List.of("управляющий Семен Семеныч: крестьянин Федя, доярка Нюра");
        assertEquals(tree.getOrganizedNodes(pollResults).getChildren().get("управляющий Семен Семеныч").amountOfChildren(), 2);
    }

    @Test
    public void nodeCheckChildTest() {
        System.out.println("Check the 2nd level node contains correct child");
        pollResults = List.of("управляющий Семен Семеныч: крестьянин Федя, доярка Нюра");
        assertTrue(tree.getOrganizedNodes(pollResults).getChildren().get("управляющий Семен Семеныч").hasChild("крестьянин Федя"));
    }

    @Test
    public void changeTheLevelOfNodeTest() {
        System.out.println("Move 1st level node to 2nd level node");
        pollResults = List.of("управляющий Семен Семеныч: крестьянин Федя, доярка Нюра", "дворянин Кузькин: управляющий Семен Семеныч, жена Кузькина, экономка Лидия Федоровна");
        assertTrue(tree.getOrganizedNodes(pollResults).getChildren().get("дворянин Кузькин").hasChild("управляющий Семен Семеныч"));
    }

    @Test
    public void checkParentAfterMovingNodeToAnotherLevelTest() {
        System.out.println("Check 2nd level node parent");
        pollResults = List.of("управляющий Семен Семеныч: крестьянин Федя, доярка Нюра", "дворянин Кузькин: управляющий Семен Семеныч, жена Кузькина, экономка Лидия Федоровна");
        assertEquals(tree.getOrganizedNodes(pollResults).getChildren().get("дворянин Кузькин").getChildren().get("управляющий Семен Семеныч").getParent().getData(), "дворянин Кузькин");
    }

    @Test
    public void checkParentAfterMovingNodeToAnotherLevelNegativeTest() {
        System.out.println("Negative test. 1st level node removed to 2nd level. Check   tree.getOrganizedNodes(pollResults) doesn't have a child of removed node");
        pollResults = List.of("управляющий Семен Семеныч: крестьянин Федя, доярка Нюра", "дворянин Кузькин: управляющий Семен Семеныч, жена Кузькина, экономка Лидия Федоровна");
        assertFalse(tree.getOrganizedNodes(pollResults).hasChild("управляющий Семен Семеныч"));
    }

    @Test
    public void check2ndLevelNodeContentChangesTest() {
        System.out.println("change content of 2nd level node after creation");
        pollResults = List.of("управляющий Семен Семеныч: крестьянин Федя, доярка Нюра", "дворянин Кузькин: управляющий Семен Семеныч, жена Кузькина, экономка Лидия Федоровна", "управляющий Семен Семеныч: тестер Петя");
        assertTrue(tree.getOrganizedNodes(pollResults).getChildren().get("дворянин Кузькин").getChildren().get("управляющий Семен Семеныч").hasChild("тестер Петя"));
    }


    //e2e test
    @Test
    public void checkPollResultsProceedTest() {
        System.out.println("Check the pollResults processed succesfully");
        pollResults = List.of("служанка Аня", "экономка Лидия Федоровна: дворник Гена, служанка Аня", "кот Василий: человеческая особь Катя", "доярка Нюра", "дворник Гена: посыльный Тошка", "киллер Гена", "зажиточный холоп: крестьянка Таня", "шпион Т: кучер Д", "секретарь короля: зажиточный холоп, шпион Т", "посыльный Тошка: кот Василий", "аристократ Клаус", "управляющий Семен Семеныч: крестьянин Федя, доярка Нюра", "дворянин Кузькин: управляющий Семен Семеныч, жена Кузькина, экономка Лидия Федоровна", "просветленный Антон");

        //check the rootNode has correct amount of children
        assertEquals(tree.getOrganizedNodes(pollResults).amountOfChildren(), 5);

        //check the root Node has correct children
        assertTrue(tree.getOrganizedNodes(pollResults).hasChild("аристократ Клаус"));
        assertTrue(tree.getOrganizedNodes(pollResults).hasChild("дворянин Кузькин"));
        assertTrue(tree.getOrganizedNodes(pollResults).hasChild("киллер Гена"));
        assertTrue(tree.getOrganizedNodes(pollResults).hasChild("просветленный Антон"));
        assertTrue(tree.getOrganizedNodes(pollResults).hasChild("секретарь короля"));

        //check correct Nodes have subvassals (children)
        assertTrue(tree.getOrganizedNodes(pollResults).getChildren().get("дворянин Кузькин").hasChildren());
        assertTrue(tree.getOrganizedNodes(pollResults).getChildren().get("секретарь короля").hasChildren());

        assertFalse(tree.getOrganizedNodes(pollResults).getChildren().get("аристократ Клаус").hasChildren());
        assertFalse(tree.getOrganizedNodes(pollResults).getChildren().get("киллер Гена").hasChildren());
        assertFalse(tree.getOrganizedNodes(pollResults).getChildren().get("просветленный Антон").hasChildren());

        //check sorted order for 1st level vassals
        List<String> list = new ArrayList<>(tree.getOrganizedNodes(pollResults).getChildren().keySet());
        List<String> listSorted = new ArrayList<String>(list);
        listSorted.sort(Comparator.naturalOrder());
        assertEquals(listSorted, list);

        //check sorted order for 2st level vassals
        list = new ArrayList<>(tree.getOrganizedNodes(pollResults).getChildren().get("дворянин Кузькин").getChildren().keySet());
        listSorted = new ArrayList<String>(list);
        listSorted.sort(Comparator.naturalOrder());
        assertEquals(listSorted, list);

        //check sorted order for 2st level vassals
        list = new ArrayList<>(tree.getOrganizedNodes(pollResults).getChildren().get("секретарь короля").getChildren().keySet());
        listSorted = new ArrayList<String>(list);
        listSorted.sort(Comparator.naturalOrder());

        //check sorted order for 2st level vassals
        list = new ArrayList<>(tree.getOrganizedNodes(pollResults).getChildren().get("дворянин Кузькин").getChildren().get("управляющий Семен Семеныч").getChildren().keySet());
        listSorted = new ArrayList<String>(list);
        listSorted.sort(Comparator.naturalOrder());

    }

    //e2e test
    @Test
    public void checkLazyKingExpectedResult() {
        System.out.println("check that LazyKing has the most number of vassals comparing with 1st level Vassals");
        pollResults = List.of("служанка Аня", "экономка Лидия Федоровна: дворник Гена, служанка Аня", "кот Василий: человеческая особь Катя", "доярка Нюра", "дворник Гена: посыльный Тошка", "киллер Гена", "зажиточный холоп: крестьянка Таня", "шпион Т: кучер Д", "секретарь короля: зажиточный холоп, шпион Т", "посыльный Тошка: кот Василий", "аристократ Клаус", "управляющий Семен Семеныч: крестьянин Федя, доярка Нюра", "дворянин Кузькин: управляющий Семен Семеныч, жена Кузькина, экономка Лидия Федоровна", "просветленный Антон");
        Integer maxSubvassals =
                tree.getOrganizedNodes(pollResults).getChildren().entrySet().stream()
                        .map(vassal -> vassal.getValue().getChildren().size()).peek(System.out::println)
                        .max(Comparator.naturalOrder()).get();
        assertTrue(tree.getOrganizedNodes(pollResults).amountOfChildren() > maxSubvassals, "King is looser");
    }

    //TODO complete according test plan
}