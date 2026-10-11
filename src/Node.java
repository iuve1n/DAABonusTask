public class Node {
    int value;
    private Node leftChild;
    private Node rightChild;

    Node(int value) {
        this.value = value;
        this.leftChild = null;
        this.rightChild = null;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public int compare(int otherValue){
        return Integer.compare(this.value, otherValue);
    }
}
