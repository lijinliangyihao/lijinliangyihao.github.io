package treasure.alg.top100;

import java.util.ArrayList;
import java.util.List;

public class _96_binary_search_tree_count {
    
    /**
     * 两个函数
     *  g(n) 长度 n 的数有几种排列方式
     *  f(i, n) 长度 n 的数，以 i 为根，有几种排列方式
     *
     *  g(n) = f(i, n), i = 1..n 的和
     *  f(i, n) = g(i - 1) * g(n - i)
     *
     *  所以 g(n) = g(i - 1) * g(n - i), i = 1..n 的和
     *
     * */
    public static class Dp {
        public int numTrees(int n) {
            int[] g = new int[n + 1];
            g[0] = g[1] = 1;
            
            // 这个循环意思是，长度为 i 的情况下，
            for (int i = 2; i <= n; i++) {
                // 枚举每一个节点作为根
                for (int j = 1; j <= i; j++) {
                    // 然后套公式
                    g[i] += g[j - 1] * g[i - j];
                }
            }
            return g[n];
        }
    }
    
    /**
     * 我答案是对的，但是超时
     * */
    public static class HighFootprintAnswer {
        
        int count;
        int[] each;
        int[] preorder;
        public int numTrees(int n) {
            each = new int[n];
            for (int i = 0; i < each.length; i++) {
                each[i] = i + 1;
            }
            preorder = each.clone();
            permute(each, 0);
            return count;
        }
    
        void permute(int[] each, int index) {
            if (index == each.length) {
                if(rebuild(preorder, 0, each.length - 1, each, 0, each.length - 1)) count++;
                return;
            }
            for (int i = index; i < each.length; i++) {
                swap(each, index, i);
                permute(each, index + 1);
                swap(each, index, i);
            }
        }
    
        void swap(int[] arr, int l, int r) {
            if(l == r) return;
            int t = arr[l]; arr[l] = arr[r]; arr[r] = t;
        }
    
        boolean rebuild(int[] preorder, int l1, int r1, int[] inorder, int l2, int r2) {
            if(r1 - l1 != r2 - l2) return false;
            if(l1 > r1) return true;
            int root = preorder[l1];
            int i = l2;
            while(i <= r2 && inorder[i] != root) i++;
            if(i > r2) return false;
            int leftLen = i - l2;
            return rebuild(preorder, l1 + 1, l1 + leftLen, inorder, l2, l2 + leftLen - 1) &&
                rebuild(preorder, l1 + leftLen + 1, r1, inorder, l2 + leftLen + 1, r2);
        }
    }
    
    
}
