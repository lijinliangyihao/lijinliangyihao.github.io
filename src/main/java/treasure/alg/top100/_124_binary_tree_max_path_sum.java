package treasure.alg.top100;

import treasure.alg.common.TreeNode;

import java.util.HashMap;
import java.util.Map;

/**
 * 路径被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。
 * 同一个节点在一条路径序列中至多出现一次。该路径至少包含一个节点，且不一定经过根节点。
 * 路径和是路径中各节点值的总和。
 * 给你一个二叉树的根节点 root，返回其最大路径和
 *     -10
 * 8         20
 *       15      7
 * */
public class _124_binary_tree_max_path_sum {
    
    
    /**
     *
     * 官方题解怎么和我速度一样？就是我的方法臭又长。
     *
     * 官方题解对我解法的改进在哪？
     * 1. 它一次递归把答案都搞出来了，我额外 dfs 了一次
     * 2. 它多考虑了一步，如果孩子是负的就不加了，我多了两次比较
     * 3. 由于是自底向上的，上来就递归，只花费了一些栈空间，这导致我的 cache 没什么收益了；但是我 cache 在 dfs 还是有用的，不然没法写
     * */
    public static class Solution {
    
        // 这又错了，应该设置成极小值，才能求最大
//        int max;
        int max = Integer.MIN_VALUE;
        public int maxPathSum(TreeNode root) {
            dfs(root);
            return max;
        }
        int dfs(TreeNode root) {
            if(root == null) return 0;
            int left = Math.max(dfs(root.left), 0);
            int right = Math.max(dfs(root.right), 0);
            int val = root.val + left + right;
            max = Math.max(max, val);
            // 这又错了，应该返回包含自己和左右孩子较大者（或不包含，如果左右孩子都负）的和
//            return val;
            return root.val + Math.max(left, right);
        }
    }
    
    /**
     * 我的答案错哪了？
     * 路径是 任意 节点开始到 任意 节点结束
     * 我这个，是从叶子结点到叶子结点了
     *
     * FIXED: 经过我的修改，最后对了，打败了 26% 的人
     * */
    public static class MyFirstlyWrongThenFixedAnswer {
        /*
         * 我思路是，算出来每个节点，过它的路径最大值
         * 然后遍历每个节点，算值
         * 然后有个全局最大值，最后返回它
         * */
    
        int max;
        Map<TreeNode, Integer> cache = new HashMap<>();
        public int maxPathSum(TreeNode root) {
            cache.put(null, 0);
            fillCache(root);
            dfs(root);
            return max;
        }
    
        int fillCache(TreeNode root) {
            if(root == null) return 0;
            if(cache.containsKey(root)) return cache.get(root);
            int left = fillCache(root.left);
            int right = fillCache(root.right);
            // 我之前的写法，没看清题意
//            int val = Math.max(left, right) + root.val;
            int val = Math.max(Math.max(left, right) + root.val, root.val);
            cache.put(root, val);
            return val;
        }
    
        void dfs(TreeNode root) {
            if(root == null) return;
            
            // 我之前的写法，没看清题
//            max = Math.max(max, root.val + cache.get(root.left) + cache.get(root.right));
            
            // 每个节点是三种情况，包含左边，包含右边，都包含，只包含自己
            int left = cache.get(root.left);
            int right = cache.get(root.right);
            int both = left + right;
            int val = Math.max(Math.max(left, Math.max(right, both)), 0) + root.val;
            max = Math.max(max, val);
            dfs(root.left);
            dfs(root.right);
        }
    }
    
}
