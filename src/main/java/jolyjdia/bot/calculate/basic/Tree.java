package jolyjdia.bot.calculate.basic;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Tree<T> {
    private Tree.Node<T> root;

    @Contract(pure = true)
    public Tree(T rootData) {
        root = new Tree.Node<>(null, rootData);
    }

    public Tree.Node<T> getRoot() {
        return root;
    }

    public final void setRoot(Tree.Node<T> root) {
        this.root = root;
    }

    @NotNull
    public final List<T> sequentializePostOrder() {
        List<T> sequence = new ArrayList<>();
        root.sequentializePostOrder(sequence);
        return sequence;
    }

    public static class Node<T> {
        private final List<Tree.Node<T>> children;
        private T data;
        private Tree.Node<T> parent;

        public Node(Tree.Node<T> parent, T data) {
            this.parent = parent;
            this.data = data;
            this.children = new ArrayList<>();
        }

        public final void addChild(@NotNull Tree.Node<T> child) {
            child.parent = this;
            children.add(child);
        }

        public final void insertParent(Tree.Node<T> midParent) {
            Tree.Node<T> upParent = this.parent;
            if (upParent != null) {
                upParent.children.remove(this);
                upParent.addChild(midParent);
            }
            midParent.setParent(upParent);
            midParent.addChild(this);
        }

        public final void absorbNode(@NotNull Tree.Node<T> node) {
            for (Tree.Node<T> child : node.children) {
                addChild(child);
            }
            children.remove(node);
        }

        public final void sequentializePreOrder(@NotNull List<T> sequence) {
            sequence.add(data);
            for (Tree.Node<T> child : children) {
                child.sequentializePreOrder(sequence);
            }
        }

        public final void sequentializePostOrder(List<T> sequence) {
            for (Tree.Node<T> child : children) {
                child.sequentializePostOrder(sequence);
            }
            sequence.add(data);
        }

        @Contract(pure = true)
        public final List<Tree.Node<T>> getChildren() {
            return children;
        }

        @Contract(pure = true)
        public final Tree.Node<T> getParent() {
            return parent;
        }

        public final void setParent(Tree.Node<T> parent) {
            this.parent = parent;
            if (parent == null) {
                return;
            }
            if(parent.children.stream().anyMatch(sibling -> sibling == this)) {
                return;
            }
            parent.children.add(this);
        }

        @Contract(pure = true)
        public final T getData() {
            return data;
        }

        public final void setData(T data) {
            this.data = data;
        }
    }
}
