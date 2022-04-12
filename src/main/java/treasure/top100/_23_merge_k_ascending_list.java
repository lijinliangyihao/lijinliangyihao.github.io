package treasure.top100;

import treasure.common.ListNode;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class _23_merge_k_ascending_list {
    
    
    public static class PriorityQueued {
        public ListNode mergeKLists(ListNode[] lists) {
            if(lists.length == 0) return null;
            PriorityQueue<ListNode> q = new PriorityQueue<>(Comparator.comparingInt(l -> l.val));
            ListNode dummy = new ListNode();
            ListNode pre = dummy;
            for (ListNode list : lists) {
                q.offer(list);
            }
            while(!q.isEmpty()) {
                ListNode min = q.poll();
                if(min.next != null)
                    q.offer(min.next);
                pre.next = min;
                pre = min;
            }
            return dummy.next;
        }
    }
    
    // 答案还给了两种，一种是轮着 merge，一种是分治 merge
    
    public static class TwoTwo {
    
        /**
         * 这个没想的那么好写
         * */
        public ListNode mergeTwo(ListNode l1, ListNode l2) {
            if(l1 == null || l2 == null) return l1 == null ? l2 : l1;
            ListNode head = new ListNode();
            ListNode prev = head;
            while(l1 != null && l2 != null) {
                if(l1.val > l2.val) {
                    prev.next = l2;
                    l2 = l2.next;
                } else {
                    prev.next = l1;
                    l1 = l1.next;
                }
                prev = prev.next;
            }
            prev.next = l1 == null ? l2 : l1;
            return head.next;
        }
    
        public ListNode mergeKLists(ListNode[] lists) {
            ListNode res = null;
            for (ListNode list : lists) {
                res = mergeTwo(res, list);
            }
            return res;
        }
    }
    
    /**
     * 难以置信，这居然是最快的方式！
     * */
    public static class DivideConquer {
        
        /**
         * 唉，还得手把手教
         * 不用构造数组，传下标就行了
         * */
        public ListNode mergeKLists(ListNode[] lists) {
            return merge(lists, 0, lists.length - 1);
        }
        
        public ListNode merge(ListNode[] lists, int l, int r) {
            if(l == r) return lists[l];
            if(l > r) return null;
            int mid = (l + r) >>> 1;
            return new TwoTwo().mergeTwo(merge(lists, l, mid), merge(lists, mid + 1, r));
        }
        
    }
    
}
