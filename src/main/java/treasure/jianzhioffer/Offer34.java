package treasure.jianzhioffer;

import treasure.common.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 蒙混过关！
 * */
public class Offer34 {
    
    List<List<Integer>> res = new ArrayList<>();
    public List<List<Integer>> pathSum(TreeNode root, int target) {
        if(root == null) return res;
        dfs(root, new LinkedList<>(), target, 0);
        return res;
    }
    
    void dfs(TreeNode root, LinkedList<Integer> path, int target, int sum) {
        if(root == null) return;
        
        path.add(root.val);
        
        if(root.left == null && root.right == null) {
            if(target + root.val == sum) {
                res.add(new ArrayList<>(path));
            }
            path.pollLast();
            return;
        }
        dfs(root.left, path, target, sum + root.val);
        dfs(root.right, path, target, sum + root.val);
        path.pollLast();
    }

}
