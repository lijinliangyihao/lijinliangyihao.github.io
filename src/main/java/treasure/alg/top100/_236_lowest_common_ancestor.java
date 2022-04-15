package treasure.alg.top100;

import treasure.alg.common.TreeNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 这个题做了至少五遍了，但还是很陌生
 *
 * 没做出来！
 * */
public class _236_lowest_common_ancestor {
    
    /**
     * 看看路飞是怎么写的
     * 他这个也很快
     * */
    public static class Luffy {
    
        /**
         * 路飞也是深度优先，孩子往上返回值
         * 根空或根 == p 或根 == q 返回根
         * 然后获取左右孩子的返回，有四种情况
         * 1. 都是 null，自己也是 null
         * 2. 都不是 null，返回自己
         * 3 和 4. 左或右是 null，分别返回 右 或 左
         *
         * */
        public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
            if(root == null || root == p || root == q) return root;
            TreeNode left = lowestCommonAncestor(root.left, p, q);
            TreeNode right = lowestCommonAncestor(root.right, p, q);
            if(left == null) return right;
            if(right == null) return left;
            return root;
        }
    }
    
    /**
     * 感觉这个方法也挺不错的
     * */
    public static class UseAMap {
        Map<TreeNode, TreeNode> parents = new HashMap<>();
        Set<TreeNode> visited = new HashSet<>();
        public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
            dfs(root);
            while(p != null) {
                visited.add(p);
                p = parents.get(p);
            }
            while(q != null) {
                if(visited.contains(q)) return q;
                q = parents.get(q);
            }
            return null;
        }
        
        void dfs(TreeNode root) {
            if(root.left != null) {
                parents.put(root.left, root);
                dfs(root.left);
            }
            if(root.right != null) {
                parents.put(root.right, root);
                dfs(root.right);
            }
        }
    }
    
    /**
     * 感觉我差了那么一丢丢，快接近答案了
     * */
    public static class Dfs {
    
        TreeNode res;
        public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
            dfs(root, p, q);
            return res;
        }
        
        boolean dfs(TreeNode root, TreeNode p, TreeNode q) {
            if (root == null) return false;
            boolean left = dfs(root.left, p, q);
            boolean right = dfs(root.right, p, q);
            if (left && right || (root == p || root == q) && (left || right)) {
                res = root;
            }
            return left || right || root == p || root == q;
        }
    }

    /**
     * 捣鼓了挺长时间没搞出来，放弃了
     * */
    public static class HeIsScared {
        /*
         * 自底向上遍历，发现一个节点，左边有 pq，或右边有 pq，或自己是 p或q，左右有 p或q
         * 它就是答案
         * */
        public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
            if(root == null) return null;
            boolean left = containsAtLeastOne(root.left, p, q);
            boolean right = containsAtLeastOne(root.right, p, q);
            return null;
        }
        /*
         *  5
         * 1 2
         *
         * 对于每一个节点，我只需要知道 p 或 q 中的一个，在它左边或右边，就返回 true
         *
         * */
        // 判断 root 是不是那啥
        boolean containsAtLeastOne(TreeNode root, TreeNode p, TreeNode q) {
            if(root == null) return false;
            boolean left = containsAtLeastOne(root.left, p, q);
            boolean right = containsAtLeastOne(root.right, p, q);
            return left || right || root == p || root == q;
        }
    }
}
