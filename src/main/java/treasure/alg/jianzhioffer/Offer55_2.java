package treasure.alg.jianzhioffer;

import treasure.alg.common.TreeNode;

/**
 * 输入一棵二叉树的根节点，判断该树是不是平衡二叉树。
 * 如果某二叉树中任意节点的左右子树的深度相差不超过1，那么它就是一棵平衡二叉树
 *
 * */
public class Offer55_2 {
    
    /**
     * 我的解法，求高度，有很多重复计算，怎么整呢？
     * */
    public static class MyAnswer {
        public boolean isBalanced(TreeNode root) {
            if(root == null) return true;
            boolean leftBalanced = isBalanced(root.left);
            boolean rightBalanced = isBalanced(root.right);
            int leftDepth = depth(root.left);
            int rightDepth = depth(root.right);
            return leftBalanced && rightBalanced && Math.abs(leftDepth - rightDepth) < 2;
        }
    
        int depth(TreeNode root) {
            if(root == null) return 0;
            int leftDepth = depth(root.left);
            int rightDepth = depth(root.right);
            return Math.max(leftDepth, rightDepth) + 1;
        }
    }
    
    /**
     * 这个是目前性能最高的解法，但是有没有更直观的解法了？
     * */
    public static class Solution {
        
        public boolean isBalanced(TreeNode root) {
            return height(root) > -1;
        }
    
        public int height(TreeNode root) {
            if (root == null) {
                return 0;
            }
            int leftHeight = height(root.left);
            if(leftHeight == -1) return -1;
            int rightHeight = height(root.right);
            if(rightHeight == -1) return -1;
            return Math.abs(leftHeight - rightHeight) > 1 ? -1 : Math.max(leftHeight, rightHeight) + 1;
        }
    }
    
}
