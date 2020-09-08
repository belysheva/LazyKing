package Tree;

import java.util.List;

public interface IStatusTree {
   IStatusTreeNode<String> getOrganizedNodes(List<String> pollList);
}
