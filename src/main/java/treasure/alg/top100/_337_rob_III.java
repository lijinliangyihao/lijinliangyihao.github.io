package treasure.alg.top100;

import treasure.alg.common.TreeNode;

import java.util.HashMap;
import java.util.Map;

/**
 * 稍作思考，意识到不会做，直接放弃
 * */
public class _337_rob_III {
    
    public static class Solution1 {
        /**
         * 稍作思考，意识到不会做，直接放弃
         *
         * 而且这个题确实难，看答案的话很好做
         * 这可能说明也没那么难
         *
         * f(i) 选择 i 时最大值
         * g(i) 不选 i 时最大值
         * l 和 r 是左右孩子
         *
         * 如果选了 i，i 左右孩子没法选了
         *      f(i) = g(l) + g(r)
         * 如果不选 i，i 左右孩子可选可不选
         *      g(i) = max(g(l), f(l)) + max(g(r), f(r))
         * */
        public int rob(TreeNode root) {
            choose.put(null, 0);
            unChoose.put(null, 0);
            dfs(root);
            return Math.max(choose.get(root), unChoose.get(root));
        }
        Map<TreeNode, Integer> choose = new HashMap<>();
        Map<TreeNode, Integer> unChoose = new HashMap<>();
    
        void dfs(TreeNode root) {
            if (root == null) return;
            dfs(root.left);
            dfs(root.right);
            choose.put(root, unChoose.get(root.left) + unChoose.get(root.right) + root.val);
            unChoose.put(root, Math.max(choose.get(root.left), unChoose.get(root.left)) +
                Math.max(choose.get(root.right), unChoose.get(root.right)));
        }
    }
    
    /**
     * 这个加了一点小小的优化，每次 dfs 返回两个值，一个选的最大值，一个不选的最大值，省去了两个 map
     *
     * 这个很快，超过了 100%
     * 但是不好想，除非你练过
     * */
    public static class Solution2 {
        public int rob(TreeNode root) {
            int[] res = dfs(root);
            return Math.max(res[0], res[1]);
        }
        int[] nil = {0, 0};
        int[] dfs(TreeNode root) {
            if(root == null) return nil;
            int[] left = dfs(root.left);
            int[] right = dfs(root.right);
            int choose = root.val + left[1] + right[1];
            int unChoose = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
            return new int[]{choose, unChoose};
        }
    }
}
