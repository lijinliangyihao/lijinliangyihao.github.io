package treasure.jianzhioffer;

import treasure.common.TreeNode;

import java.util.Stack;

/**
 * 给定一个二叉搜索树, 找到该树中两个指定节点的最近公共祖先。
 *
 * 百度百科中最近公共祖先的定义为：
 * “对于有根树 T 的两个结点 p、q，最近公共祖先表示为一个结点 x，
 * 满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。”
 *
 *
 * 这个题难度是简单，我怎么感觉没思路呢
 * */
public class Offer68_1 {
    
    /**
     * 我是服了路飞了，确实厉害
     * */
    public static class Lufei {
        public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
            // 确定大小减少判断
            if(p.val > q.val) {
                TreeNode t = p; p = q; q = t;
            }
            while(true) {
                if(root.val < p.val) root = root.right;
                else if(root.val > q.val) root = root.left;
                else break;
            }
            return root;
        }
    
        /**
         * 递归版本
         * */
        public TreeNode lowestCommonAncestor1(TreeNode root, TreeNode p, TreeNode q) {
            // 确定大小减少判断
            if(p.val > q.val) {
                TreeNode t = p; p = q; q = t;
            }
            return helper(root, p, q);
        }
        TreeNode helper(TreeNode root, TreeNode p, TreeNode q) {
            if(root.val < p.val) return helper(root.right, p, q);
            if(root.val > q.val) return helper(root.left, p, q);
            return root;
        }
    }
    
    /**
     * 用很笨的办法实现了。。
     * */
    public static class StupidMethod {
        public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
            Stack<TreeNode> stack1 = new Stack<>();
            dfs(root, stack1, p);
            Stack<TreeNode> stack2 = new Stack<>();
            dfs(root, stack2, q);
        
            Stack<TreeNode> bigger = stack1, smaller = stack2;
            int dif = stack1.size() - stack2.size();
            if(dif < 0) {
                bigger = stack2;
                smaller = stack1;
                dif = -dif;
            }
            while(dif-- > 0) bigger.pop();
            while(!bigger.isEmpty()) {
                TreeNode a = bigger.pop();
                TreeNode b = smaller.pop();
                if(a == b) return a;
            }
            return null;
        }
    
        void dfs(TreeNode root, Stack<TreeNode> stack, TreeNode p) {
            if(root == null) return;
            if(root.val == p.val) {
                stack.push(p);
                return;
            }
            stack.push(root);
            if(root.val > p.val) dfs(root.left, stack, p);
            else dfs(root.right, stack, p);
        }
    }
    
}
