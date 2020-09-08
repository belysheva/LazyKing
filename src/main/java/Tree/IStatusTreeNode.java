package Tree;

import java.util.Map;

public interface IStatusTreeNode<T> {
     T getData();

     Map<T, IStatusTreeNode<T>>getChildren();

     void addChild(IStatusTreeNode<T> child);

     IStatusTreeNode<T> getParent();

     void setParent(IStatusTreeNode<T> parent);

     boolean hasChild(T child);

     boolean hasChild(IStatusTreeNode<T> childData);

     boolean hasChildren();

     boolean hasParent();

     int amountOfChildren();

}
