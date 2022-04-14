package treasure.alg.top100;

public class _338_count_bits {
    
    /*
    * 3 个动态规划解法
    * 其实我的 notgood2 也用到了一些动态规划思路了
    * */
    public static class Dp3 {
    
        // 最高有效位
        /*
        * 思路是：每个数都有个最高有效位，先找到这个值，这个值只有一个 1
        *   然后当前值比 当前值 - 这个值 大 1
        *   而 当前值 - 这个值 之前已经求出来了，所以直接取就行了
        * */
        public int[] countBits1(int n) {
            int[] bits = new int[n + 1];
            int highBit = 0;
            for (int i = 1; i <= n; i++) {
                if ((i & (i - 1)) == 0) {
                    highBit = i;
                }
                bits[i] = bits[i - highBit] + 1;
            }
            return bits;
        }
    
        // 最低有效位
        /*
        * 这个也是利用比它小的数已经求出来了
        * 我那个笨方法其实就是这个思路！但是没他这个简洁
        * 挪走低位，低位是 1 加 1，否则不变
        * */
        public int[] countBits2(int n) {
            int[] bits = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                bits[i] = bits[i >> 1] + (1 & i);
            }
            return bits;
        }
    
        // 最低设置位
        /*
        * 我那个笨方法其实和这个一样。。。
        * */
        public int[] countBits3(int n) {
            int[] bits = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                bits[i] = bits[i & (i - 1)] + 1;
            }
            return bits;
        }
    }
    
    /**
     * 我这个解法改改就是上面的 dp
     * */
    public static class NotGood2 {
    
        int[] res;
        int index = 1;
        public int[] countBits(int n) {
            res = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                calc(i);
            }
            return res;
        }
        
        void calc(int n) {
            int count = 0;
            int p = n;
            while(p > 0) {
                /*
                * 这有优化空间，应该用 n & (n - 1) 消一个 1
                * p &= p - 1;
                * count++;
                *
                * 这样快了很多！
                * */
                if ((p & 1) == 1) {
                    count++;
                }
                p >>= 1;
                if(p < index) {
                    res[n] = count + res[p];
                    index++;
                    break;
                }
            }
        }
    }
    
    
    /**
     * 这样挺快，但是还有别的办法
     * */
    public static class NotGood {
        public int[] countBits(int n) {
            int[] res = new int[n + 1];
            for (int i = 0; i <= n; i++) {
                res[i] = Integer.bitCount(i);
            }
            return res;
        }
    }
}
