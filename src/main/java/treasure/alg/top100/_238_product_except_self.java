package treasure.alg.top100;

import java.util.Scanner;

/**
 * 我知道剑指 offer 里有，构建乘积数组，但是好像。。
 * 我直接看答案吧！
 * */
public class _238_product_except_self {
    
    public static class s1 {
        // 看了答案，感觉也不难！
        public int[] productExceptSelf(int[] nums) {
            int n = nums.length;
            int[] l = new int[n];
            int[] r = new int[n];
            int[] res = new int[n];
            
            l[0] = 1;
            for (int i = 1; i < n; i++) {
                l[i] = l[i - 1] * nums[i - 1];
            }
            
            r[n - 1] = 1;
            for (int i = n - 2; i >= 0 ; i--) {
                r[i] = r[i + 1] * nums[i + 1];
            }
    
            for (int i = 0; i < n; i++) {
                res[i] = l[i] * r[i];
            }
            return res;
        }
    }
    
    /**
     * 上面解法已经很快了，但是他们还不满足，想进一步缩小空间利用！
     * */
    public static class s2 {
        
        /**
         * 真是秒啊！用结果数组代替了 l 和 r 数组，其中计算 l 时直接更新，计算 r 时额外使用一个变量
         * */
        public int[] productExceptSelf(int[] nums) {
            Scanner s;
            int n = nums.length;
            int[] res = new int[n];
            res[0] = 1;
            for (int i = 1; i < n; i++) {
                res[i] = res[i - 1] * nums[i - 1];
            }
            int r = 1;
            // 这有一点小拐弯，得是从最后一个开始，不然 r 就少更新一次，然后全错了
            for (int i = n - 1; i >= 0 ; i--) {
                res[i] *= r;
                r *= nums[i];
            }
            return res;
        }
    }
}
