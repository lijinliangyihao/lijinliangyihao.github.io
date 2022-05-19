package treasure.alg.top100;

import treasure.alg.common.TreeNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class _230_kth_binary_search_tree {
    
    /**
     * 我想的比较诡异的写法
     * */
    public static class wired {
        int k;
        public int kthSmallest(TreeNode root, int k) {
            this.k = k;
            try {
                dfs(root);
            } catch (Exception ignored){}
            return res;
        }
        // 我这可以抛出一个异常或设置一个标记位
        // 设计标记为需要遍历完整棵树但是抛异常直接就出来了，我感觉抛异常更快一点！
        static class Signal extends RuntimeException {
            @Override
            public synchronized Throwable fillInStackTrace() {
                return this;
            }
        }
    
        boolean started;
        int res;
        void dfs(TreeNode root) {
            if (root == null) {
                started = true;
                return;
            }
            dfs(root.left);
            if (started) {
                if (--k == 0) {
                    res = root.val;
                    throw new Signal();
                }
            }
            dfs(root.right);
        }
    }
    
    public static class iterative {
        
        public int kthSmallest(TreeNode root, int k) {
            Stack<TreeNode> stack = new Stack<>();
            while (!stack.isEmpty() || root != null) {
                while (root != null) {
                    stack.push(root);
                    root = root.left;
                }
                root = stack.pop();
                if (--k == 0) {
                    break;
                }
                root = root.right;
            }
            return root.val;
        }
    }
    
    /**
     * 记忆化统计 + 二分查找
     * 这个挺慢的
     * */
    public static class count_and_binary_search {
    
        Map<TreeNode, Integer> nodeCount = new HashMap<>();
        
        public int kthSmallest(TreeNode root, int k) {
            count(root);
            return search(root, k);
        }
        int search (TreeNode node, int k) {
            while (node != null) {
                // 这有个技巧，如果你找 node，那如果大了不知道该去左边还是右边了
                // 但是如果你找 node.left，那大了，直接去左边找！
                int count = nodeCount.getOrDefault(node.left, 0);
                if (count == k - 1)
                    break;
                if (count > k - 1)
                    node = node.left;
                else {
                    node = node.right;
                    k -= count + 1;
                }
            }
            return node.val;
        }
        int count(TreeNode node) {
            if (node == null) return 0;
            // 我这没递归，找了半天，居然找了半天！
            int count = 1 + count(node.left) + count(node.right);
            nodeCount.put(node, count);
            return count;
        }
    
    }
    
}
