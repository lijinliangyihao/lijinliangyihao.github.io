package treasure.jianzhioffer;

import treasure.common.ListNode;

public class Offer22 {
    
    public ListNode getKthFromEnd(ListNode head, int k) {
        if(head == null) return null;
        ListNode fast = head;
        while(fast != null && k > 0) {
            fast = fast.next;
            k--;
        }
        if(k > 0) return null;
        ListNode slow = head;
        while(fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }
    
}
