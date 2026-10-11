public class BST {
    private Node root;

    public boolean insert(int value) {
        if (root == null) {
            root = new Node(value);
            return true;
        }

        Node current = root;
        while (true) {
            int comparison = current.compare(value);
            if (comparison == 0) {
                return false;
            }
            if (comparison > 0) {
                if (current.getLeftChild() == null) {
                    current.setLeftChild(new Node(value));
                    return true;
                }
                current = current.getLeftChild();
            } else {
                if (current.getRightChild() == null) {
                    current.setRightChild(new Node(value));
                    return true;
                }
                current = current.getRightChild();
            }
        }
    }

    public boolean contains(int value) {
        Node current = root;
        while (current != null) {
            int comparison = current.compare(value);
            if (comparison == 0) {
                return true;
            }
            current = comparison > 0 ? current.getLeftChild() : current.getRightChild();
        }
        return false;
    }

    public boolean delete(int value) {
        if (!contains(value)) {
            return false;
        }
        root = deleteNode(root, value);
        return true;
    }

    private Node deleteNode(Node node, int value) {
        int comparison = node.compare(value);
        if (comparison > 0) {
            node.setLeftChild(deleteNode(node.getLeftChild(), value));
        } else if (comparison < 0) {
            node.setRightChild(deleteNode(node.getRightChild(), value));
        } else if (node.getLeftChild() == null) {
            return node.getRightChild();
        } else if (node.getRightChild() == null) {
            return node.getLeftChild();
        } else {
            Node successor = findMin(node.getRightChild());
            node.setValue(successor.getValue());
            node.setRightChild(deleteNode(node.getRightChild(), successor.getValue()));
        }
        return node;
    }

    private Node findMin(Node node) {
        while (node.getLeftChild() != null) {
            node = node.getLeftChild();
        }
        return node;
    }
}
