package treasure.alg.top100;

import treasure.alg.common.TreeNode;

import java.util.Stack;

public class _98_verify_binary_search_tree {
    
    public static class MyNotBadAnswer {
        /**
         * 思路没问题，提交时忘了给 prev 赋值
         * 此外题目要求是严格递增
         * */
        // 这个也可以设置成极小值，里面省去一个判断
        Integer prev;
        public boolean isValidBST(TreeNode root) {
            if(root == null) return true;
            boolean valid = isValidBST(root.left);
            if(!valid) return false;
            if(prev == null) prev = root.val;
            else if(prev >= root.val) return false;
            prev = root.val;
            return isValidBST(root.right);
        }
    }
    
    public static class Iterative {
        
        /**
         * 陷阱1. 最小值不能设置成 Integer.MIN_VALUE，它输入数据可能会是这个值，导致判断失败
         * 陷阱2. 这其实不是陷阱，Double.MIN_VALUE 它是个正数，你得用 -Double.MAX_VALUE
         * */
        public boolean isValidBST(TreeNode root) {
            Stack<TreeNode> stack = new Stack<>();
            double prev = -Double.MAX_VALUE;
            while(root != null || !stack.isEmpty()) {
                while(root != null) {
                    stack.push(root);
                    root = root.left;
                }
                root = stack.pop();
                if(root.val <= prev) return false;
                prev = root.val;
                root = root.right;
            }
            return true;
        }
    }
    
    public static class Dfs {
    
        public boolean isValidBST(TreeNode root) {
            return helper(root, Long.MIN_VALUE, Long.MAX_VALUE);
        }
        
        boolean helper(TreeNode root, long left, long right) {
            if (root == null) return true;
            if(root.val <= left || root.val >= right) return false;
            return helper(root.left, left, root.val) && helper(root.right,root.val, right);
        }
    }
    
}
