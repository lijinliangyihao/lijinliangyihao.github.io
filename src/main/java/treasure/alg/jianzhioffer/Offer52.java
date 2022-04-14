package treasure.alg.jianzhioffer;

import treasure.alg.common.ListNode;

/**
 * 输入两个链表，找出它们的第一个公共节点。
 *
 * 不搞什么奇技淫巧，直接判断长度，然后快慢指针
 * */
public class Offer52 {
    
    /**
     * 这是我朴实的解法
     * */
    public static class MyNaiveAnswer {
        public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
            int len1 = len(headA);
            int len2 = len(headB);
            ListNode fast = headA;
            ListNode slow = headB;
            if(len1 < len2) {
                fast = headB;
                slow = headA;
            }
            int dif = Math.abs(len1 - len2);
            while(dif-- > 0) {
                fast = fast.next;
            }
            while(fast != slow) {
                fast = fast.next;
                slow = slow.next;
            }
            return slow;
        }
    
        int len(ListNode head) {
            int len = 0;
            while(head != null) {
                head = head.next;
                len++;
            }
            return len;
        }
    }
    
    /**
     * 骚套路！
     * */
    public class Solution {
        public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
            ListNode A = headA, B = headB;
            while (A != B) {
                A = A != null ? A.next : headB;
                B = B != null ? B.next : headA;
            }
            return A;
        }
    }
    
}
