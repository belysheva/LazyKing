import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//TODO remove hardcoded test data to file
public class LazyKingTest {

    private static List<String> pollResults = new ArrayList<>();

    static IStatusTreeNode<String> rootNode;
    static IUnluckyVassalService<String> unluckyVassal;

    @BeforeEach
    public void prepareNode() {
        pollResults = new ArrayList<>();
        rootNode = new MyStatusTreeNode<>("король", null);
        unluckyVassal = new UnluckyVassal();
        System.out.println("--------------------------------------");
    }

    @Test
    public void rootNodeAddingTest() {
        System.out.println("RootNode adding");
        unluckyVassal.printReportForKing(rootNode, pollResults);
        assertEquals(rootNode.toString(), "король");
    }

    @Test
    public void checkChildWhen1stNodeAddedTest() {
        System.out.println("Check 1st child node creation");
        pollResults = List.of("служанка Аня");
        unluckyVassal.printReportForKing(rootNode, pollResults);
        assertEquals(rootNode.amountOfChildren(), 1, "Wrong number of children");
    }

    @Test
    public void rootNodeChildrenNegativeTest() {
        System.out.println("Check RootNode doesn't have children");
        unluckyVassal.printReportForKing(rootNode, pollResults);
        assertFalse(rootNode.hasChildren(), String.format("Expected result: Root node should have %d, it has %d children nodes", 0, rootNode.amountOfChildren()));
    }


    @Test
    public void duplicatedNodesCreationTest() {
        System.out.println("Check duplicated nodes creation");

        pollResults = List.of("служанка Аня", "служанка Аня");
        IUnluckyVassalService<String> unluckyVassal = new UnluckyVassal();
        unluckyVassal.printReportForKing(rootNode, pollResults);
        assertEquals(rootNode.amountOfChildren(), 1);
    }

    @Test
    public void secondLevelNodeAddTest() {
        System.out.println("Check the Node with subVassals added successfully");

        pollResults = List.of("управляющий Семен Семеныч: крестьянин Федя, доярка Нюра");
        unluckyVassal.printReportForKing(rootNode, pollResults);
        assertTrue(rootNode.hasChild("управляющий Семен Семеныч"));
    }

    @Test
    public void vassalsAmountTest() {
        System.out.println("Check amount of vassals for 2nd level node");
        pollResults = List.of("управляющий Семен Семеныч: крестьянин Федя, доярка Нюра");
        unluckyVassal.printReportForKing(rootNode, pollResults);
        assertEquals(rootNode.getChildren().get("управляющий Семен Семеныч").amountOfChildren(), 2);
    }

    @Test
    public void nodeCheckChildTest() {
        System.out.println("Check the 2nd level node contains correct child");
        pollResults = List.of("управляющий Семен Семеныч: крестьянин Федя, доярка Нюра");
        unluckyVassal.printReportForKing(rootNode, pollResults);
        assertTrue(rootNode.getChildren().get("управляющий Семен Семеныч").hasChild("крестьянин Федя"));
    }

    @Test
    public void changeTheLevelOfNodeTest() {
        System.out.println("Move 1st level node to 2nd level node");
        pollResults = List.of("управляющий Семен Семеныч: крестьянин Федя, доярка Нюра", "дворянин Кузькин: управляющий Семен Семеныч, жена Кузькина, экономка Лидия Федоровна");
        unluckyVassal.printReportForKing(rootNode, pollResults);
        assertTrue(rootNode.getChildren().get("дворянин Кузькин").hasChild("управляющий Семен Семеныч"));
    }

    @Test
    public void checkParentAfterMovingNodeToAnotherLevelTest() {
        System.out.println("Check 2nd level node parent");
        pollResults = List.of("управляющий Семен Семеныч: крестьянин Федя, доярка Нюра", "дворянин Кузькин: управляющий Семен Семеныч, жена Кузькина, экономка Лидия Федоровна");
        unluckyVassal.printReportForKing(rootNode, pollResults);
        assertEquals(rootNode.getChildren().get("дворянин Кузькин").getChildren().get("управляющий Семен Семеныч").getParent().getData(), "дворянин Кузькин");
    }

    @Test
    public void checkParentAfterMovingNodeToAnotherLevelNegativeTest() {
        System.out.println("Negative test. 1st level node removed to 2nd level. Check rootNode doesn't have a child of removed node");

        pollResults = List.of("управляющий Семен Семеныч: крестьянин Федя, доярка Нюра", "дворянин Кузькин: управляющий Семен Семеныч, жена Кузькина, экономка Лидия Федоровна");
        unluckyVassal.printReportForKing(rootNode, pollResults);
        assertFalse(rootNode.hasChild("управляющий Семен Семеныч"));
    }

    @Test
    public void check2ndLevelNodeContentChangesTest() {
        System.out.println("change content of 2nd level node after creation");

        pollResults = List.of("управляющий Семен Семеныч: крестьянин Федя, доярка Нюра", "дворянин Кузькин: управляющий Семен Семеныч, жена Кузькина, экономка Лидия Федоровна", "управляющий Семен Семеныч: тестер Петя");
        unluckyVassal.printReportForKing(rootNode, pollResults);
        assertTrue(rootNode.getChildren().get("дворянин Кузькин").getChildren().get("управляющий Семен Семеныч").hasChild("тестер Петя"));
    }

    //e2e test
    @Test
    public void checkPollResultsProceedTest() {
        System.out.println("Check the pollResults processed succesfully");

        pollResults = List.of("служанка Аня","экономка Лидия Федоровна: дворник Гена, служанка Аня","кот Василий: человеческая особь Катя","доярка Нюра","дворник Гена: посыльный Тошка","киллер Гена","зажиточный холоп: крестьянка Таня","шпион Т: кучер Д","секретарь короля: зажиточный холоп, шпион Т","посыльный Тошка: кот Василий","аристократ Клаус","управляющий Семен Семеныч: крестьянин Федя, доярка Нюра","дворянин Кузькин: управляющий Семен Семеныч, жена Кузькина, экономка Лидия Федоровна","просветленный Антон");
        unluckyVassal.printReportForKing(rootNode, pollResults);
        //check the rootNode has correct amount of children
        assertEquals(rootNode.amountOfChildren(), 5);

        //check the rootNode has correct children
        assertTrue(rootNode.hasChild("аристократ Клаус"));
        assertTrue(rootNode.hasChild("дворянин Кузькин"));
        assertTrue(rootNode.hasChild("киллер Гена"));
        assertTrue(rootNode.hasChild("просветленный Антон"));
        assertTrue(rootNode.hasChild("секретарь короля"));

        //check correct Nodes have subvassals (children)
        assertTrue(rootNode.getChildren().get("дворянин Кузькин").hasChildren());
        assertTrue(rootNode.getChildren().get("секретарь короля").hasChildren());

        assertFalse(rootNode.getChildren().get("аристократ Клаус").hasChildren());
        assertFalse(rootNode.getChildren().get("киллер Гена").hasChildren());
        assertFalse(rootNode.getChildren().get("просветленный Антон").hasChildren());

        //check sorted order for 1st level vassals
        List<String> list = new ArrayList<>(rootNode.getChildren().keySet());
        List<String> listSorted = new ArrayList<String>(list);
        listSorted.sort(Comparator.naturalOrder());
        assertEquals(listSorted, list);

        //check sorted order for 2st level vassals
        list = new ArrayList<>(rootNode.getChildren().get("дворянин Кузькин").getChildren().keySet());
        listSorted = new ArrayList<String>(list);
        listSorted.sort(Comparator.naturalOrder());
        assertEquals(listSorted, list);

        //check sorted order for 2st level vassals
        list = new ArrayList<>(rootNode.getChildren().get("секретарь короля").getChildren().keySet());
        listSorted = new ArrayList<String>(list);
        listSorted.sort(Comparator.naturalOrder());

        //check sorted order for 2st level vassals
        list = new ArrayList<>(rootNode.getChildren().get("дворянин Кузькин").getChildren().get("управляющий Семен Семеныч").getChildren().keySet());
        listSorted = new ArrayList<String>(list);
        listSorted.sort(Comparator.naturalOrder());

    }

    //e2e test
    @Test
    public void checkLazyKingExpectedResult() {
        System.out.println("check that LazyKing has the most number of vassals comparing with 1st level Vassals");

        pollResults = List.of("служанка Аня", "экономка Лидия Федоровна: дворник Гена, служанка Аня", "кот Василий: человеческая особь Катя", "доярка Нюра", "дворник Гена: посыльный Тошка", "киллер Гена", "зажиточный холоп: крестьянка Таня", "шпион Т: кучер Д", "секретарь короля: зажиточный холоп, шпион Т", "посыльный Тошка: кот Василий", "аристократ Клаус", "управляющий Семен Семеныч: крестьянин Федя, доярка Нюра", "дворянин Кузькин: управляющий Семен Семеныч, жена Кузькина, экономка Лидия Федоровна", "просветленный Антон");
        unluckyVassal.printReportForKing(rootNode, pollResults);
        Integer maxSubvassals = rootNode.getChildren().entrySet().stream().map(vassal -> vassal.getValue().getChildren().size()).peek(System.out::println).max(Comparator.naturalOrder()).get();
        assertTrue(rootNode.amountOfChildren()>maxSubvassals, "King is looser");
    }


    //TODO complete according test plan

}