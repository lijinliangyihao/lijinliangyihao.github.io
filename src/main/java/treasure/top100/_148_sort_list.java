package treasure.top100;

import treasure.common.ListNode;

import java.util.Comparator;
import java.util.PriorityQueue;

public class _148_sort_list {
    
    public static class MergeBottomUp {
        /**
         * 这个解法虽然空间复杂度小但是比归并慢不少
         * */
        public ListNode sortList(ListNode head) {
            if(head == null || head.next == null) return head;
            int len = 0;
            ListNode p = head;
            while(p != null) {
                len++;
                p = p.next;
            }
            ListNode dummy = new ListNode(0, head);
            for (int subLen = 1; subLen < len; subLen <<= 1) {
                // 不能为了图省事，让 cur = dummy
                ListNode cur = dummy.next, prev = dummy;
                // 这个循环，每两个 subLen merge 一次，两两 merge
                while(cur != null) {
                    // 你这不能这么搞，第一轮是对的，后面全错了
//                    ListNode head1 = cur.next;
                    ListNode head1 = cur;
                    int i = subLen;
                    while(cur != null && i-- > 1) {
                        cur = cur.next;
                    }
                    ListNode head2 = null;
                    if(cur != null) {
                        head2 = cur.next;
                        cur.next = null;
                        cur = head2;
                        i = subLen;
                        while(cur != null && i-- > 1) {
                            cur = cur.next;
                        }
                    }
                    ListNode next = null;
                    if(cur != null) {
                        next = cur.next;
                        cur.next = null;
                    }
                    cur = next;
                    prev.next = merge(head1, head2);
                    while(prev.next != null) prev = prev.next;
                }
            }
            return dummy.next;
        }
        
        ListNode merge(ListNode l1, ListNode l2) {
            return new MergeTopDown().mergeTwo(l1, l2);
        }
    }
    
    public static class MergeTopDown {
    
        /**
        * 这个 fast 和 slow 的值还是得想清楚的，没完全搞明白背过没有意义！
        * */
        public ListNode sortList(ListNode head) {
            if(head == null || head.next == null) return head;
            ListNode fast = head.next;
            ListNode slow = head;
            // a b c d slow = b, fast = b
            // a b slow = a fast = a
            while(fast != null && fast.next != null) {
                fast = fast.next.next;
                slow = slow.next;
            }
            ListNode head2 = slow.next;
            slow.next = null;
            ListNode l1 = sortList(head);
            ListNode l2 = sortList(head2);
            return mergeTwo(l1, l2);
        }
        
        /**
         * sortList
         *  的官方写法，它使用两个参数
         *
         * */
        public ListNode sortList2(ListNode head) {
            return sort(head, null);
        }
        
        ListNode sort(ListNode head, ListNode tail) {
            if(head == null) return null;
            if(head.next == tail) {
                head.next = null;
                return head;
            }
            ListNode slow = head, fast = head;
            while(fast != tail) {
                slow = slow.next;
                fast = fast.next;
                if(fast != tail)
                    fast = fast.next;
            }
            // 这为什么是 slow 而不是 slow.next
            // 主要是考虑到 两个元素 的情况
            ListNode mid = slow;
            ListNode l1 = sort(head, mid);
            ListNode l2 = sort(mid, tail);
            return mergeTwo(l1, l2);
        }
    
        public ListNode mergeTwo(ListNode l1, ListNode l2) {
            if(l1 == null || l2 == null) return l1 == null ? l2 : l1;
            ListNode dummy = new ListNode();
            ListNode p = dummy;
            while(l1 != null && l2 != null) {
                if(l1.val <= l2.val) {
                    p.next = l1; l1 = l1.next;
                } else {
                    p.next = l2; l2 = l2.next;
                }
                p = p.next;
            }
            p.next = l1 == null ? l2 : l1;
            return dummy.next;
        }
    }
    
    // 我第一反应就是优先队列，只干过了 5% 的人！
    public static class Pri {
        public ListNode sortList(ListNode head) {
            if(head == null) return null;
            PriorityQueue<ListNode> q = new PriorityQueue<>(Comparator.comparingInt(i -> i.val));
            ListNode p = head;
            while(p != null) {
                q.offer(p);
                p = p.next;
            }
            ListNode res = new ListNode();
            p = res;
            while(!q.isEmpty()) {
                ListNode node = q.poll();
                p.next = node;
                node.next = null;
                p = node;
            }
            return res.next;
        }
    }
}
