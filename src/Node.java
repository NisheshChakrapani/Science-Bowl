public class Node {
    QuestionAndBonus data;
    Node next;

    public Node(QuestionAndBonus data, Node next) {
        this.data = data;
        this.next = next;
    }
    public Node(QuestionAndBonus data) {
        this(data, null);
    }
    public Node() {
        this(null, null);
    }
}
