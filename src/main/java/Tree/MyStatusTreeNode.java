package Tree;

import Tree.IStatusTreeNode;
import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

@Data
public class MyStatusTreeNode<T> implements IStatusTreeNode<T> {

    private final T data;
    private final Map<T, IStatusTreeNode<T>> children = new TreeMap<>();
    private IStatusTreeNode<T> parent;

    public MyStatusTreeNode(T data) {
        this.data = data;
    }

    public MyStatusTreeNode(T data, IStatusTreeNode<T> parent) {
        this.data = data;
        this.parent = parent;
    }

    public void addChild(IStatusTreeNode<T> child) {
        if (child.hasParent() && child.getParent().hasChild(child)) {
            child.getParent().getChildren().remove(child.getData());
        }
        child.setParent(this);
        this.children.put(child.getData(), child);
    }

    @Override
    public boolean hasChild(IStatusTreeNode<T> childData) {
        return !this.children.isEmpty() && this.children.containsValue(childData);
    }

    @Override
    public boolean hasChild(T childData) {
        return !this.children.isEmpty() && this.children.containsKey(childData);
    }

    @Override
    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    @Override
    public boolean hasParent() {
        return this.parent != null;
    }

    @Override
    public int amountOfChildren() {
        return this.children.size();
    }

    @Override
    public String toString() {
        return data.toString();
    }
}