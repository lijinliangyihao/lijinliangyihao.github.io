package treasure.alg.top100;

import treasure.alg.common.TreeNode;

import java.util.LinkedList;

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
    我的思路是，修改左树，返回左树
    我思路不行，用别人的思路把还是，不会搞
    虽然别人三个队列，看起来比较傻逼，我这个搞不出来，难道就不傻逼了吗
    * */
    public TreeNode mergeTrees_iterate(TreeNode left, TreeNode right) {
        if (left == null) return right;
        if (right == null) return left;
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.offer(left);
        queue.offer(right);
        while (!queue.isEmpty()) {
        
        }
        return left;
    }
    
}
