package treasure.alg.top100;

import treasure.alg.common.TreeNode;

public class _105_build_tree {
    
    /**
     * 做了 1000 遍了，我再做一遍
     *
     * 又花了我十几分钟，难道还是不熟？不应该啊
     * */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        return build(preorder, 0, preorder.length - 1, inorder, 0, inorder.length - 1);
    }
    
    TreeNode build(int[] preorder, int l1, int r1, int[] inorder, int l2, int r2) {
        if(l1 > r1 || l2 > r2) return null;
        // 提交时这写成 0 了又
        int rootVal = preorder[l1];
        TreeNode root = new TreeNode(rootVal);
        int i = l2;
        for (; i <= r2 && inorder[i] != rootVal; i++);
        int leftLen = i - l2;
        // 提交时，r1 写错了，写的 l1 + leftLen - 1
        // 不能 -1，因为 l1 是根，应该是 l1 + 1 + leftLen - 1
        root.left = build(preorder, l1 + 1, l1 + leftLen, inorder, l2, l2 + leftLen - 1);
        root.right = build(preorder, l1 + leftLen + 1, r1, inorder, l2 + leftLen + 1,r2);
        return root;
    }
}
