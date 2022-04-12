package treasure.jianzhioffer;

import treasure.common.TreeNode;

public class Offer26 {
    
    public boolean isSubStructure(TreeNode A, TreeNode B) {
        if(A == null || B == null) return false;
        return contains(A, B) || isSubStructure(A.left, B) || isSubStructure(A.right, B);
    }
    
    boolean contains(TreeNode left, TreeNode right) {
        if(left == null) return right == null;
        if(right == null) return true;
        if(left.val != right.val) return false;
        return contains(left.left, right.left) && contains(left.right, right.right);
    }
    
}
