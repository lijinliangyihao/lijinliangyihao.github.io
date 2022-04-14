package treasure.alg.moka;

import treasure.alg.common.ManyChildrenTreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Round1 {
    
    /**
     * 多个孩子的层序遍历。。
     * 差点没写出来！
     * */
    static List<List<Integer>> level(ManyChildrenTreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if(root == null) return res;
        Queue<ManyChildrenTreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while(!queue.isEmpty()) {
            int n = queue.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                ManyChildrenTreeNode poll = queue.poll();
                level.add(poll.val);
                
                if(poll.children != null) {
                    for (ManyChildrenTreeNode child : poll.children) {
                        queue.offer(child);
                    }
                }
            }
            res.add(level);
        }
        return res;
    }
}
