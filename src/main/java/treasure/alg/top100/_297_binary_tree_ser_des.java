package treasure.alg.top100;

import treasure.alg.common.TreeNode;

/**
 * 这个题做过三次了，剑指 offer 原题，都没做出来。
 * */
public class _297_binary_tree_ser_des {
    
    /**
     * 这次算是侥幸自己做出来了吧
     * */
    public static class Codec {
        
        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            StringBuilder sb = new StringBuilder();
            dfs(root, sb);
            return sb.toString();
        }
        void dfs(TreeNode root, StringBuilder sb) {
            if(root == null) {
                sb.append("-,");
                return;
            }
            sb.append(root.val).append(",");
            dfs(root.left, sb);
            dfs(root.right, sb);
        }
        /*
        * 1
        * 2 3
        * 1,2,-,-,3,-,-,
        * */
        
        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {
            String[] arr = data.split(",");
            return desr(arr);
        }
        int i;
        TreeNode desr(String[] arr) {
            if (arr[i].equals("-")) {
                i++;
                return null;
            }
            TreeNode root = new TreeNode(Integer.parseInt(arr[i++]));
            root.left = desr(arr);
            root.right = desr(arr);
            return root;
        }
        
    }
}
