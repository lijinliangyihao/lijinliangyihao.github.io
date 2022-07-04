package treasure.alg.top100;

import treasure.alg.common.TreeNode;

import java.util.LinkedList;

public class _101_symmetric_tree {
    
    /*
    * 我那版稍微啰嗦点但是效果一样
    * */
    public boolean isSymmetric(TreeNode root) {
        if (root == null)
            return true;
        return check(root.left, root.right);
    }
    
    private boolean check(TreeNode left, TreeNode right) {
        if (left == null || right == null)
            return left == right;
        return left.val == right.val && check(left.left, right.right) && check(left.right, right.left);
    }
    
    /*
    * 迭代版
    * 能过但是很慢 咋回事？
    * */
    public boolean isSymmetric_iterate(TreeNode root) {
        if (root == null)
            return true;
        LinkedList<TreeNode> q = new LinkedList<>();
        q.offer(root);
        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode left = q.poll();
            TreeNode right = q.poll();
            if (left.val != right.val)
                return false;
            if (left.left == null || right.right == null) {
                if (left.left != right.right)
                    return false;
            } else {
                q.offer(left.left);
                q.offer(right.right);
            }
            if (left.right == null || right.left == null) {
                if (left.right != right.left)
                    return false;
            } else {
                q.offer(left.right);
                q.offer(right.left);
            }
        }
        return true;
    }
    
    /*
    * 这个比我的优雅点，但是也很慢
    * */
    public boolean isSymmetric_iterate_v2(TreeNode root) {
        LinkedList<TreeNode> q = new LinkedList<>();
        q.offer(root);
        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode left = q.poll();
            TreeNode right = q.poll();
            if (left == null && right == null)
                continue;
            if (left == null || right == null || left.val != right.val)
                return false;
                
            q.offer(left.left);
            q.offer(right.right);
            q.offer(left.right);
            q.offer(right.left);
        }
        return true;
    }
    
    
}
