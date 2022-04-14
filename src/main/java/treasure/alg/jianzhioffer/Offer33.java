package treasure.alg.jianzhioffer;

import treasure.alg.common.TreeNode;

import java.util.Stack;

/**
 * 输入一个整数数组，判断该数组是不是某二叉搜索树的后序遍历结果。
 * 如果是则返回 true，否则返回 false。假设输入的数组的任意两个数字都互不相同
 *
 * 30 分钟后放弃了，看答案，没通过测试
 *
 *      5
 *     / \
 *    2   6
 *   / \
 *  1   3
 *
 *  [1,3,2,6,5]
 *
 * */
public class Offer33 {
    
    public static class MyWrongAnswer {
        
        public boolean verifyPostorder(int[] postorder) {
            if(postorder == null || postorder.length == 0) return true;
            int n = postorder.length;
            int root = postorder[n - 1];
        
            return doVerify(postorder, root, n - 1, 0);
        }
    
        boolean doVerify(int[] postorder, int root, int end, int start) {
            if(end == start) return true;
            int rightChildIndex = end;
            while(rightChildIndex > start && postorder[rightChildIndex - 1] > root) {
                rightChildIndex--;
            }
            rightChildIndex++;
        
            boolean rightChildOk = true;
            if(rightChildIndex < end) {
                rightChildOk = checkRange(postorder, root, end - 1, rightChildIndex, true);
                rightChildOk |= doVerify(postorder, postorder[end - 1], end - 1, rightChildIndex);
            }
            boolean leftChildOk = true;
            if(rightChildIndex > start) {
                leftChildOk = checkRange(postorder, root, rightChildIndex - 1, start, false);
                leftChildOk |= doVerify(postorder, postorder[rightChildIndex - 1], rightChildIndex - 2, start);
            }
        
            return leftChildOk && rightChildOk;
        }
    
        boolean checkRange(int[] postorder, int root, int end, int start, boolean rootShouldBeSmaller) {
            for (int i = start; i <= end; i++) {
                if(rootShouldBeSmaller && postorder[i] < root || postorder[i] > root) {
                    return false;
                }
            }
            return true;
        }
    }
    
    /**
     * 使用递归判断
     * */
    public static class Recursive {
        public boolean verifyPostorder(int[] postorder) {
            return helper(postorder, 0, postorder.length - 1);
        }
    
        /*
        * 这么写是直觉，从右边往左找，但是判断比较多，比较奇怪
        * 尤其是第一行，必须是 i >= j，要覆盖没有左右子树的情况
        * */
        boolean helper(int[] arr, int i, int j) {
            if(i >= j) return true;
            int right = j - 1;
            while(right >= i && arr[right] > arr[j]) right--;
            int left = right;
            while(left >= i && arr[left] < arr[j]) left--;
            left++;
            return left == i && helper(arr, i, right) && helper(arr, right + 1, j - 1);
        }
    
        /*
        * 搞错了，这个也得 >= 考虑到左右子树为空
        * 这两种情况必须都得考虑为空，不然玩你妈，他妈的
        * */
        boolean helper2(int[] arr, int i, int j) {
            if(i >= j) return true;
            int left = i;
            while(arr[left] < arr[j]) left++;
            int right = left;
            while(arr[right] > arr[j]) right++;
            return right == j && helper(arr, i, left - 1) && helper(arr, left, j - 1);
        }
    }
    
    /**
     * 单调栈，非常的难以理解
     *
     * 你现在会做了，过两天肯定忘了
     *  [1,3,2,6,5]
     * 利用的是：右子树肯定比根节点大，左子树肯定比根节点小；倒序遍历这个“后续遍历”
     * 先假设这个树是一个根节点很大的树的左子树，然后倒序开始遍历，每个节点必须都小于根
     * 根初始是 +∞
     * 入栈 5
     * 入栈 6
     * 2 打算入栈前，触发了出栈：里面的节点比 2 大，所以得找 2 的父节点，要找那个大小最接近它的父节点，所以找到了 5，弹出 5，2 进栈
     * 入栈 3
     * 1 打算入栈，触发出栈：找到父节点 2，1 入栈
     * 结束了
     *
     * */
    public static class MonotonousStack {
    
        public boolean verifyPostorder1(int[] postorder) {
            Stack<Integer> stack = new Stack<>();
            int root = Integer.MAX_VALUE;
            for(int i = postorder.length - 1; i >= 0; i--) {
                if(postorder[i] > root) return false;
                while(!stack.isEmpty() && stack.peek() > postorder[i])
                    root = stack.pop();
                stack.add(postorder[i]);
            }
            return true;
        }
    
    }
    

}
