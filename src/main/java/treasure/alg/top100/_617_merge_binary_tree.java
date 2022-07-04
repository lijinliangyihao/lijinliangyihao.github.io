package treasure.alg.top100;

import treasure.alg.common.TreeNode;

public class _617_merge_binary_tree {
    
    /*
    * 本来想混的不小心做出来了，纯属意外
    *
    * 我这个解法不能说错，但是有点煞笔
    *
    * */
    public TreeNode mergeTrees(TreeNode left, TreeNode right) {
        if (left == null && right == null)
            return null;
        int leftVal = left == null ? 0 : left.val;
        int rightVal = right == null ? 0 : right.val;
        TreeNode res = new TreeNode(leftVal + rightVal);
        
        res.left = mergeTrees(safeGet(left, true), safeGet(right, true));
        res.right = mergeTrees(safeGet(left, false), safeGet(right, false));
        
        return res;
    }
    TreeNode safeGet(TreeNode node, boolean left) {
        return node == null ? null : left ? node.left : node.right;
    }
    
    public TreeNode mergeTrees_v2(TreeNode left, TreeNode right) {
        if (left == null) return right;
        if (right == null) return left;
        TreeNode root = new TreeNode(left.val + right.val);
        root.left = mergeTrees_v2(left.left, right.left);
        root.right = mergeTrees_v2(left.right, right.right);
        return root;
    }
    
    /*
    * TODO 广度优先还没看答案
    * */
    
    
}
