package treasure.alg.top100;

import treasure.alg.common.ListNode;

public class _92_reverse_list_between {
    
    // 中间那一段需要遍历两次
    public ListNode reverseBetween(ListNode head, int left, int right) {
        ListNode dummy = new ListNode(0, head);
        ListNode pre = dummy;
        int reverseLen = right - left + 1;
        // 这个 left 是有用的下标，长度应该是 left - 1，所以这容易错
        while(left-- - 1> 0) {
            pre = pre.next;
        }
        ListNode next = pre.next;
        while(reverseLen-- > 0) {
            next = next.next;
        }
        ListNode oldPre = pre;
        ListNode cur = oldPre.next;
        pre = next;
        while(cur != next) {
            ListNode n = cur.next;
            cur.next = pre;
            pre = cur;
            cur = n;
        }
        oldPre.next = pre;
        return dummy.next;
    }
    
    // 使用头插法，中间那一段只需要遍历一次
    // 12345 13245 14325
    public ListNode reverseBetween2(ListNode head, int left, int right) {
        ListNode dummy = new ListNode(0, head);
        ListNode pre = dummy;
        for (int i = 0; i < left - 1; i++) {
            pre = pre.next;
        }
        ListNode cur = pre.next;
        for (int i = 0, len = right - left; i < len; i++) {
            ListNode next = cur.next;
            cur.next = next.next;
            next.next = pre.next;
            pre.next = next;
        }
        return dummy.next;
    }
}
