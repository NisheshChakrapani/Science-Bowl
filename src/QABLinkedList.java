public class QABLinkedList {
    Node head;

    public QABLinkedList(QuestionAndBonus first) {
        this.head = new Node(first);
    }
    public QABLinkedList() {
        this(null);
    }

    public QuestionAndBonus get(int index) {
        if (index < 0) throw new IndexOutOfBoundsException("Index must be 0 or greater.");
        if (index >= size()) throw new IndexOutOfBoundsException("Index is too large.");
        if (head == null) return null;
        int i = 0;
        Node curr = head;
        while (i != index) {
            curr = curr.next;
            i++;
        }
        return curr.data;
    }

    public void add(QuestionAndBonus qab) {
        if (head == null) {
            head = new Node(qab, null);
            return;
        }
        Node curr = head;
        while (curr.next != null) {
            curr = curr.next;
        }
        curr.next = new Node(qab);
    }

    public int size() {
        Node curr = head;
        int size = 0;
        while (curr != null) {
            size++;
            curr = curr.next;
        }
        return size;
    }
}
