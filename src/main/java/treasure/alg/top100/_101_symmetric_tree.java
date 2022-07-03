package treasure.alg.top100;

import treasure.alg.common.TreeNode;

public class _101_symmetric_tree {
    
    public boolean isSymmetric(TreeNode root) {
        if (root == null)
            return true;
        return check(root.left, root.right);
    }
    
    private boolean check(TreeNode left, TreeNode right) {
        if (left == null || right == null)
            return left == right;
        if (left.val != right.val) return false;
        TreeNode ll = left.left, lr = left.right;
        TreeNode rl = right.left, rr = right.right;
        return check(ll, rr) && check(lr, rl);
    }
}
