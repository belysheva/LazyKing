import java.util.List;

public interface IUnluckyVassalService<T> {
    void printReportForKing(IStatusTreeNode<String> rootNode, List<T> pollResults);
}
