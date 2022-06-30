package treasure.alg.top100;

import treasure.alg.common.ListNode;

public class _21_merge_2_sorted_linked_list {
    
    /**
     * 这个题要是做错了，那就确实只配拿 13k
     *
     * 可以，但是不好
     *
     * */
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode head = new ListNode();
        ListNode p = head;
        while (list1 != null || list2 != null) {
            int v1 = list1 == null ? 101 : list1.val;
            int v2 = list2 == null ? 101 : list2.val;
            
            if (v1 <= v2) {
                p.next = new ListNode(v1);
                p = p.next;
                list1 = list1.next;
            } else {
                p.next = new ListNode(v2);
                p = p.next;
                list2 = list2.next;
            }
        }
        return head.next;
    }
    
    /**
    * 润色润色
    * */
    public ListNode mergeTwoLists_v1(ListNode list1, ListNode list2) {
        ListNode head = new ListNode();
        ListNode p = head;
        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                p.next = list1;
                list1 = list1.next;
            } else {
                p.next = list2;
                list2 = list2.next;
            }
            p = p.next;
        }
        p.next = list1 == null ? list2 : list1;
        return head.next;
    }
    
    /**
     * 递归非常的秒
     * */
    public ListNode mergeTwoLists_v2(ListNode list1, ListNode list2) {
        if (list1 == null)
            return list2;
        if (list2 == null)
            return list1;
        if (list1.val <= list2.val) {
            list1.next = mergeTwoLists_v2(list1.next, list2);
            return list1;
        }
        list2.next = mergeTwoLists_v2(list1, list2.next);
        return list2;
    }
}
