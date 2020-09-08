import java.util.List;

public interface IUnluckyVassalService<T> {
    void printResult(IStatusTreeNode<T> root, String tab);
}
