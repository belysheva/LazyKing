import Tree.IStatusTreeNode;

public interface IUnluckyVassalService<T> {
    void printResult(IStatusTreeNode<T> root, String tab);
}
