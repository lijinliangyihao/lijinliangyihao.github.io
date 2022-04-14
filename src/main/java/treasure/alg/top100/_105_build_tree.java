package treasure.alg.top100;

import treasure.alg.common.TreeNode;

public class _105_build_tree {
    
    /**
     * 做了 1000 遍了，我在做一遍
     * */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        return build(preorder, 0, preorder.length - 1, inorder, 0, inorder.length - 1);
    }
    
    TreeNode build(int[] preorder, int l1, int r1, int[] inorder, int l2, int r2) {
        return null;
    }
}
