package treasure.alg.jianzhioffer;

import treasure.alg.common.ListNode;

/**
 * 警告：过于简单
 * */
public class Offer18 {
    
    public ListNode deleteNode(ListNode head, int val) {
        ListNode dummy = new ListNode(0, head);
        ListNode prev = dummy;
        ListNode current = head;
        while (current.val != val) {
            prev = current;
            current = current.next;
        }
        prev.next = current.next;
        return dummy.next;
    }

}
