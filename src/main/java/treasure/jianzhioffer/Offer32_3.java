package treasure.jianzhioffer;

import treasure.common.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Offer32_3 {
    
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if(root == null) return res;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        LinkedList<Integer> list = new LinkedList<>();
        boolean even = true;
        while (!queue.isEmpty()) {
            int n = queue.size();
            for (int i = 0; i < n; i++) {
                TreeNode each = queue.poll();
                if(even) {
                    list.add(each.val);
                } else {
                    list.offerFirst(each.val);
                }
                if(each.left != null) queue.offer(each.left);
                if(each.right != null) queue.offer(each.right);
            }
            res.add(list);
            list = new LinkedList<>();
            even = !even;
        }
        return res;
    }
}
