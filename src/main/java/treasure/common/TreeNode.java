package treasure.common;

public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;
    
    TreeNode(int x) {
        val = x;
    }
    
    public static TreeNode gen() {
        TreeNode root = new TreeNode(5);
        TreeNode root1 = new TreeNode(1);
        TreeNode root2 = new TreeNode(4);
        TreeNode root3 = new TreeNode(3);
        TreeNode root4 = new TreeNode(6);
        root.left = root1;
        root.right = root2;
        root2.left = root3;
        root2.right = root4;
        return root;
    }
}
