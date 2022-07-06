package hyy;


/**
 * 反转链表
 */
public class _0706_206_reverseList {

    public static void main(String[] args) {
        ListNode threeNode = new ListNode(3,null);
        ListNode twoNode = new ListNode(2,threeNode);
        ListNode oneNode = new ListNode(1,twoNode);
        ListNode head = reverseList(oneNode);
        System.out.println(head.val);
    }

    /**
     * 假设链表 : 1->2->3->null    reverse后  1<-2<-3<-null
     * @param head 单链表的头结点head
     * @return 返回新的头引用
     */
    public static ListNode reverseList(ListNode head) {
        //当前节点的前一个结点
        ListNode prev = null;
        //当前结点
        ListNode curr = head;
        while(curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }
}

/**
 * 结点类
 */
class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}
