package treasure.jianzhioffer;

import treasure.common.ListNode;

import java.util.LinkedList;
import java.util.Queue;

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
