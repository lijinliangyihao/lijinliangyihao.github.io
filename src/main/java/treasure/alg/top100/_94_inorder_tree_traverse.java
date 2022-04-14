package treasure.alg.top100;

import treasure.alg.common.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class _94_inorder_tree_traverse {
    
    // 递归没意思，来写写迭代？
    public static class Iter {
    
        /**
         * 这么多年，终于能写出来了
         * */
        public List<Integer> inorderTraversal(TreeNode root) {
            Stack<TreeNode> stack = new Stack<>();
            List<Integer> res = new ArrayList<>();
            while(root != null || !stack.isEmpty()) {
                while(root != null) {
                    stack.push(root);
                    root = root.left;
                }
                TreeNode left = stack.pop();
                res.add(left.val);
                if(left.right != null)
                    root = left.right;
            }
            return res;
        }
    }
    
    public static class Recur {
        List<Integer> res = new ArrayList<>();
        public List<Integer> inorderTraversal(TreeNode root) {
            dfs(root);
            return res;
        }
    
        void dfs(TreeNode root) {
            if(root == null) return;
            dfs(root.left);
            res.add(root.val);
            dfs(root.right);
        }
    }
}
