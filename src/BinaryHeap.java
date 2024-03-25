
class Node<T> {

    T data;
    Node<T> parent;
    Node<T> left;
    Node<T> right;

    public Node(T data) {
        this.data = data;
        this.parent = null;
        this.left = null;
        this.right = null;
    }
}

class BinaryHeap<T extends Comparable<T>> {

    private Node<T> root;

    public BinaryHeap() {
        this.root = null;
    }

    private int size(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right);
    }

    public int size() {
        return size(root);
    }

    public boolean isEmpty() {
        return root == null;
    }

    private Node<T> findNode(Node<T> node, T data) {
        if (node == null) {
            return null;
        }
        if (node.data.compareTo(data) == 0) {
            return node;
        }
        Node<T> leftResult = findNode(node.left, data);
        if (leftResult != null) {
            return leftResult;
        }
        return findNode(node.right, data);
    }

    private void swap(Node<T> node1, Node<T> node2) {
        T temp = node1.data;
        node1.data = node2.data;
        node2.data = temp;
    }

    private Node<T> findLastNode(Node<T> root) {
        if (root == null) {
            return null;
        }
        if (root.left == null && root.right == null) {
            return root;
        }
        if (size(root.left) > size(root.right)) {
            return findLastNode(root.left);
        } else {
            return findLastNode(root.right);
        }
    }

    private void heapifyDown(Node<T> node) {
        while (true) {
            Node<T> maxChild = node.left;

            if (node.right != null && node.right.data.compareTo(maxChild.data) > 0) {
                maxChild = node.right;
            }

            if (maxChild != null && maxChild.data.compareTo(node.data) > 0) {
                swap(node, maxChild);
                node = maxChild;
            } else {
                break;
            }
        }
    }

    private void heapifyUp(Node<T> node) {
        while (node.parent != null && node.data.compareTo(node.parent.data) > 0) {
            swap(node, node.parent);
            node = node.parent;
        }
    }

    private Node<T> deleteRecursive(Node<T> root, T data) {
        if (root == null) {
            return null;
        }

        Node<T> nodeToDelete = findNode(root, data);

        if (nodeToDelete == null) {
            return root;
        }

        Node<T> lastNode = findLastNode(root);

        // Swap data of node to delete with data of last node
        swap(nodeToDelete, lastNode);

        // Remove last node
        Node<T> parent = lastNode.parent;
        if (parent != null) {
            if (parent.left == lastNode) {
                parent.left = null;
            } else {
                parent.right = null;
            }
        }

        // Heapify down from the node we swapped
        heapifyDown(nodeToDelete);

        return root;
    }

    private int countNodes(Node<T> root) {
        if (root == null) {
            return 0;
        }
        return 1 + countNodes(root.left) + countNodes(root.right);
    }

//    private int findLastNodeIndex(Node<T> root) {
//        int count = countNodes(root);
//        return count + 1; // Vị trí của nút mới
//    }

    private void insertAtLast(Node<T> root, T data, Node<T> newNode) {
        if (root.left == null) {
            root.left = newNode;
            newNode.parent = root;
        } else if (root.right == null) {
            root.right = newNode;
            newNode.parent = root;
        } else if (countNodes(root.left) <= countNodes(root.right)) {
            insertAtLast(root.left, data, newNode);
        } else {
            insertAtLast(root.right, data, newNode);
        }
    }

    private Node<T> insertRecursive(Node<T> root, T data) {
        if (root == null) {
            return new Node<>(data);
        }

        Node<T> newNode = new Node<>(data);

        if (root.left == null) {
            root.left = newNode;
            newNode.parent = root;
        } else if (root.right == null) {
            root.right = newNode;
            newNode.parent = root;
        } else if (countNodes(root.left) <= countNodes(root.right)) {
            insertAtLast(root.left, data, newNode);
        } else {
            insertAtLast(root.right, data, newNode);
        }
        heapifyUp(newNode);

        return root;
    }

    public void insert(T data) throws IllegalArgumentException {
        if (contains(data)) {
            throw new IllegalArgumentException("Key already exists in the heap");
        }
        root = insertRecursive(root, data);
    }

    public void delete(T data) throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException("Binary heap is empty");
        }
        root = deleteRecursive(root, data);
    }

    public Node<T> search(T data) {
        return searchRecursive(root, data);
    }

    private Node<T> searchRecursive(Node<T> node, T data) {
        if (node == null) {
            return null;
        }
        if (node.data.equals(data)) {
            return node;
        }
        Node<T> leftResult = searchRecursive(node.left, data);
        if (leftResult != null) {
            return leftResult;
        }
        return searchRecursive(node.right, data);
    }

    public boolean checkDataExist(T data) {
        return checkDataExist(root, data);
    }

    private boolean checkDataExist(Node<T> node, T data) {
        if (node == null) {
            return false;
        }
        if (node.data.equals(data)) {
            return true;
        }
        return checkDataExist(node.left, data) || checkDataExist(node.right, data);
    }

    public T deleteMax() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException("Binary heap is empty");
        }

        T max = root.data;
        if (size() == 1) {
            root = null;
        } else {
            Node<T> lastNode = findLastNode(root);
            root.data = lastNode.data;
            if (lastNode.parent.left == lastNode) {
                lastNode.parent.left = null;
            } else {
                lastNode.parent.right = null;
            }
            heapifyDown(root);
        }
        return max;
    }

    private boolean contains(T data) {
        return containsRecursive(root, data);
    }

    private boolean containsRecursive(Node<T> node, T data) {
        if (node == null) {
            return false;
        }
        if (node.data.equals(data)) {
            return true;
        }
        return containsRecursive(node.left, data) || containsRecursive(node.right, data);
    }

    ///////////////duyet cay///////////////////
    public void levelOrderTraversal() {
        int height = height(root);
        for (int i = 1; i <= height; i++) {
            printLevel(root, i);
            System.out.println(); // Xuống dòng giữa các cấp
        }
    }

    private int height(Node<T> node) {
        if (node == null) {
            return 0;
        } else {
            int leftHeight = height(node.left);
            int rightHeight = height(node.right);

            return Math.max(leftHeight, rightHeight) + 1;
        }
    }

    private void printLevel(Node<T> node, int level) {
        if (node == null) {
            return;
        }
        if (level == 1) {
            System.out.print(node.data + " ");
        } else if (level > 1) {
            printLevel(node.left, level - 1);
            printLevel(node.right, level - 1);
        }
    }
    

}

class PriorityQueue<T extends Comparable<T>> {

    private BinaryHeap<T> heap;

    public PriorityQueue() {
        heap = new BinaryHeap<>();
    }

    public void enqueue(T element) throws IllegalArgumentException {
        heap.insert(element);
    }

    public T dequeue() throws IllegalStateException {
        return heap.deleteMax();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    public void print() {
        heap.levelOrderTraversal();
    }

    public boolean checkDataExistInQueue(T data) {
        return heap.checkDataExist(data);
    }

}

/////////////////////////////////////////////
class SolarSystemObject implements Comparable<SolarSystemObject> {

    private String name;
    private double mass; // in kilograms
    private double equatorialDiameter; // in kilometers

    public SolarSystemObject(String name, double mass, double equatorialDiameter) {
        this.name = name;
        this.mass = mass;
        this.equatorialDiameter = equatorialDiameter;
    }

    public String getName() {
        return name;
    }

    public double getMass() {
        return mass;
    }

    public double getEquatorialDiameter() {
        return equatorialDiameter;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SolarSystemObject other = (SolarSystemObject) obj;
        return Double.doubleToLongBits(this.equatorialDiameter) == Double.doubleToLongBits(other.equatorialDiameter);
    }
    
    

    @Override
    public int compareTo(SolarSystemObject o) {
        return Double.compare(this.equatorialDiameter, o.equatorialDiameter);
    }

    @Override
    public String toString() {
//        return  "name=" + name + ", mass=" + mass + ", equatorialDiameter=" + equatorialDiameter ;
        return equatorialDiameter + " ";
    }

}

class SolarSystemObjectPriorityQueue extends PriorityQueue<SolarSystemObject> {

    public static void main(String[] args) {
        SolarSystemObject s1 = new SolarSystemObject("1", 1, 1);
        SolarSystemObject s2 = new SolarSystemObject("2", 1, 5);
        SolarSystemObject s3 = new SolarSystemObject("3", 1, 3);
        SolarSystemObject s4 = new SolarSystemObject("4", 1, 4);
        SolarSystemObject s5 = new SolarSystemObject("5", 1, 2);
        SolarSystemObject s6 = new SolarSystemObject("6", 1, 6);
        SolarSystemObject s7 = new SolarSystemObject("6", 1, 7);
        SolarSystemObject s8 = new SolarSystemObject("6", 1, 8);
        SolarSystemObject s9 = new SolarSystemObject("6", 1, 9);
        SolarSystemObject s10 = new SolarSystemObject("5", 1, 10);
        SolarSystemObject s11= new SolarSystemObject("6", 1, 11);
        SolarSystemObject s12= new SolarSystemObject("6", 1, 12);
        SolarSystemObject s13= new SolarSystemObject("6", 1, 13);
        SolarSystemObject s14= new SolarSystemObject("6", 1, 14);
        SolarSystemObject s15= new SolarSystemObject("6", 1, 14);

        SolarSystemObjectPriorityQueue s = new SolarSystemObjectPriorityQueue();
        try{
        s.enqueue(s1);
        s.print();
        System.out.println("---------------");
        s.enqueue(s2);
        s.print();
        System.out.println("---------------");
        s.enqueue(s3);
        s.print();
        System.out.println("---------------");
        s.enqueue(s4);
        s.print();
        System.out.println("---------------");
        s.enqueue(s5);
        s.print();
        System.out.println("---------------");
        s.enqueue(s6);

        s.print();
        System.out.println("---------------");
        s.enqueue(s7);
        s.enqueue(s8);
        s.enqueue(s9);
        s.enqueue(s10);
        s.enqueue(s11);
        s.enqueue(s12);
        s.enqueue(s13);
        s.enqueue(s14);
        s.enqueue(s15);
        } catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        }
        s.print();
        System.out.println("---------------");

        try {
            System.out.println(s.dequeue().toString());
            System.out.println(s.dequeue().toString());
            System.out.println(s.dequeue().toString());
            System.out.println(s.dequeue().toString());
            System.out.println(s.dequeue().toString());
            System.out.println(s.dequeue().toString());
            System.out.println(s.dequeue().toString());

        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
