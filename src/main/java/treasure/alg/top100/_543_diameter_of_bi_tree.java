package treasure.alg.top100;

import treasure.alg.common.TreeNode;

public class _543_diameter_of_bi_tree {
    
    /*
    * 感觉
    * 得有个全局最大值
    * 然后有左右的高度
    * 每个节点都算经过它的长度，和全局最大值比较
    * */
    public int diameterOfBinaryTree(TreeNode root) {
        /*
        * 我有点不太明白，我注释的这一段如果打开注释结果就错了
        * 我感觉这两个结果应该是一样的！
        * 哪出了问题？
        *
        * 这个 max 值，不对
        * 你把右边这两个，放到前面，先算出来左右子树高度，再求 max 就对了
        * 我试了
        * 耶稣来了也没用我说的！
        * */
//        max = Math.max(max, height(root.left) + height(root.right));
        height(root);
        return max;
    }
    int max;
    int height(TreeNode tree) {
        if (tree == null)
            return 0;
        int left = height(tree.left);
        int right = height(tree.right);
        max = Math.max(max, left + right);
        return Math.max(left, right) + 1;
    }
}
