package treasure.alg.jianzhioffer;

import treasure.alg.common.TreeNode;

import java.util.Stack;

/**
 * 给定一棵二叉搜索树，请找出其中第 k 大的节点的值。
 *
 * 没做出来，干！
 * */
public class Offer54 {
    
    /**
     * 别人是聪明的，我是笨的
     * */
    public static class SmartSolution {
        public int kthLargest(TreeNode root, int k) {
            this.k = k;
            dfs(root, k);
            return res;
        }
        int res;
        int k;
        void dfs(TreeNode root, int k) {
            if(root == null) return;
            dfs(root.right, k);
            if(k == 0) return;
            if(--k == 0) res = root.val;
            else dfs(root.left, k);
        }
    }
    
    /**
     * 笨办法脑子是死的，只知道快慢指针
     * 而且还写不出来，快慢指针咋写啊，是递归的又不是迭代
     *
     * 别再跟爹提快慢指针拉！
     * */
    public static class StupidSolution {
    
        Stack<Integer> stack = new Stack<>();
        public int kthLargest(TreeNode root, int k) {
            dfs(root);
            for (int i = 0; i < k - 1; i++) {
                stack.pop();
            }
            return stack.peek();
        }
        
        void dfs(TreeNode root) {
            if(root == null) return;
            dfs(root.left);
            stack.push(root.val);
            dfs(root.right);
        }
        
    
    }
    
}
