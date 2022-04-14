package treasure.alg.common;

public class ListNode {
    public int val;
    public ListNode next;
    
    public ListNode() {
    }
    
    public ListNode(int val) {
        this.val = val;
    }
    
    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
    
    @Override
    public String toString() {
        return "ListNode{" +
            "val=" + val +
            ", next=" + next +
            '}';
    }
    
    /**
     * 返回 [1,2,3,4,5,6]
     */
    public static ListNode genList() {
        ListNode head = new ListNode(1);
        ListNode head1 = new ListNode(2);
        ListNode head2 = new ListNode(3);
        ListNode head3 = new ListNode(4);
        ListNode head4 = new ListNode(5);
        ListNode head5 = new ListNode(6);
        head.next = head1;
        head1.next = head2;
        head2.next = head3;
        head3.next = head4;
        head4.next = head5;
        return head;
    }
}
