package treasure.jianzhioffer;

import java.util.Arrays;

/**
 * 在一个数组 nums 中除一个数字只出现一次之外，其他数字都出现了三次。请找出那个只出现一次的数字
 *
 * */
public class Offer56_2 {
    
    /**
     * 基于状态机的，最快的方法！
     *
     * 用两个状态表示 所有位 的状态
     * 因为有三个数，所以有三个状态，需要两个变量来存
     * 00 01 10 这三个状态交替进行，用变量 two 和 one 存，表示第二位和第一位
     * 由于有一个数字出现了一次，其它出现三次，第三个状态 10 下一个状态是 00 而不是 11
     * 最终， one 这个状态存的就是只出现一次的那个数字，其它的数字由于出现了 3 次，是状态数的整数倍，所以全部归零了
     *
     * one 和 two 是对称的，算出来 one，然后用 one 的新值计算 two，如此往复
     *      一直不理解怎么就成对称的了
     *
     * 加入来了一位，n，n = 0 或 1，现在考虑 one，two 是对称的， remember？
     * if(two == 0)
     *   if(n == 1) one = ~one;
     * if(two == 1)
     *   if(n == 1) one = 0;
     *
     *   最终总结出来一个公式，无论 n = 0 或 1，two = 0 或 1
     *      one = ~two & num ^ one
     *
     *   算 two
     *      one 什么情况下会进位？
     *      one == 0 n = 1 可能会进位（当 two == 1）
     *      one == 1 n = 1 肯定会进位
     *      这两种进位情况 one 最终都会变成 0，如果没进位，那 one 是 1
     *      故进位情况可以通过 ~one & num 来判断，0 肯定是进位了，1 肯定没进位
     *
     *      所以 two 根据新 one 和 num 共同决定
     *
     *      two = num & ~one ^ two;
     *
     *
     * */
    public static class Fast {
        public int singleNumber(int[] nums) {
            int one = 0, two = 0;
            for(int num : nums){
                // 本来 one 可以开心的和 num 异或，但是 two 如果是 1 必须归零，故后面要 & ~two
                one = one ^ num & ~two;
                // 如果 one 产生进位可能会影响到 two，所以先判断进位情况
                // num 是 1 且 one 是 0 肯定发生了进位，故 num & ~one，然后和 two 异或
                two = num & ~one ^ two;
            }
            // 最终三个状态只剩一个 01，存在 one 这个数字里
            // 如果看剩两个元素的，那就返回 two！
            return two;
        }
    
    }
    
    /**
     * 别人总是比我的简洁易懂！
     * 它思路和我是一样的，但是打败了 82.57% 的 java 用户
     * */
    public static class Concise {
    
        public int singleNumber(int[] nums) {
            int[] arr = new int[32];
            for (int num : nums) {
                for (int i = 0; i < 32; i++) {
                    arr[i] += num & 1;
                    num >>= 1;
                }
            }
            int res = 0, m = 3;
            for (int i = 31; i >= 0; i--) {
                res <<= 1;
                res |= arr[i] % m;
            }
            return res;
        }
    }
    
    /**
     * 这个是我凭记忆，背出来的
     * 比较慢，打败了 24% 的 java 用户
     * */
    public static class MySlowAnswer {
        // 我思路是搞个长度 32 数组存每一位的数，把 =1 的恢复！
        // 听起来很不错！
        /*
         * 3 3 3 4
         *   0 1 1
         *   0 1 1
         *   0 1 1
         *   1 0 0
         * */
        public int singleNumber(int[] nums) {
            int[] arr = new int[32];
            for (int num : nums) {
                // 每个数字的每一位都累加到 arr 里
                int mask = 1;
                int index = 0;
                while(index < 32) {
                    if((num & mask) != 0) {
                        arr[index]++;
                    }
                    mask <<= 1;
                    index++;
                }
            }
            // 恢复数字
            int res = 0;
            for (int i = 0; i < arr.length; i++) {
                if(arr[i] % 3 == 1) {
                    res |= 1 << i;
                }
            }
            return res;
        }
    }
    
}
