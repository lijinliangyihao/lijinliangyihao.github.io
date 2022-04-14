package treasure.alg.jianzhioffer;

/**
 * 给定一个数字，我们按照如下规则把它翻译为字符串：
 *      0 翻译成 “a” ，1 翻译成 “b”，……，11 翻译成 “l”，……，25 翻译成 “z”。
 * 一个数字可能有多个翻译。请编程实现一个函数，用来计算一个数字有多少种不同的翻译方法
 *
 *  12258有5种不同的翻译，分别是"bccfi", "bwfi", "bczi", "mcfi"和"mzi"
 *
 *  这个题我一看，就是回溯！
 * */
public class Offer46 {
    
    /**
     * 看着性能不高，其实也不慢
     * 我没能自己写出来！
     * */
    public static class Backtrack {
        public int translateNum(int num) {
            String seq = String.valueOf(num);
            return backtrack(seq, 0);
        }
    
        int backtrack(String seq, int index){
            if(index == seq.length()) {
                return 1;
            }
            if(index == seq.length() - 1 || seq.charAt(index) == '0' || !check(seq, index, index + 1))
                return backtrack(seq, index + 1);
            return backtrack(seq, index + 1) + backtrack(seq, index + 2);
        }
    
        boolean check(String seq, int start, int end) {
            int val = Integer.parseInt(seq.substring(start, end + 1));
            return val >= 0 && val <= 25;
        }
    }

    /**
    * 滚动数组 dp
     *
     * 我们发现它和「198. 打家劫舍」非常相似。
     * 我们可以用 f(i) 表示以第 i 位结尾的前缀串翻译的方案数，
     * 考虑第 i 位单独翻译和与前一位连接起来再翻译对 f(i) 的贡献。
     *
     * 单独翻译对 f(i) 的贡献为 f(i - 1)；
     * 如果第 i - 1 位存在，并且第 i - 1 位和第 i 位形成的数字 x 满足 10 ≤ x ≤ 25，
     * 那么就可以把第 i - 1 位和第 i 位连起来一起翻译，对 f(i) 的贡献为 f(i - 2)，否则为 0。
     *
     * 我们可以列出这样的动态规划转移方程：
     *      f(i) = f(i − 1) + f(i − 2)[i − 1 ≥ 0,10 ≤ x ≤ 25]
     * 边界条件是 f(-1) = 0，f(0) = 1。方程中 [c] 的意思是 c 为真的时候 [c] = 1，否则 [c] = 0
     *
     * 有了这个方程我们不难给出一个时间复杂度为 O(n)，
     * 空间复杂度为 O(n) 的实现。
     * 考虑优化空间复杂度：这里的 f(i) 只和它的前两项 f(i - 1) 和 f(i - 2) 相关，
     * 我们可以运用「滚动数组」思想把 f 数组压缩成三个变量，这样空间复杂度就变成了 O(1)
     *
     * 经过精简后的代码
    * */
    public static class Dp{
        
        public int translateNum(int num) {
            String src = String.valueOf(num);
            int m2 = 0, m1 = 1, cur = 1;
            for (int i = 1; i < src.length(); ++i) {
                m2 = m1;
                m1 = cur;
                String pre = src.substring(i - 1, i + 1);
                if (pre.compareTo("25") <= 0 && pre.compareTo("10") >= 0) {
                    cur += m2;
                }
            }
            return cur;
        }
        
        public int version2(int num) {
            String s = String.valueOf(num);
            int a = 1, b = 1;
            for(int i = 2; i <= s.length(); i++) {
                String tmp = s.substring(i - 2, i);
                int c = tmp.compareTo("10") >= 0 && tmp.compareTo("25") <= 0 ? a + b : a;
                b = a;
                a = c;
            }
            return a;
        }
    }
    
    /**
     * 数字求余解法，从右往左找，是对称的
     * */
    public static class Mod {
        public int translateNum(int num) {
            int a = 1, b = 1, x, y = num % 10;
            while(num != 0) {
                // 他这一上来就跑到第二位了，所以前面 y = num % 10;
                num /= 10;
                x = num % 10;
                int tmp = 10 * x + y;
                int c = (tmp >= 10 && tmp <= 25) ? a + b : a;
                b = a;
                a = c;
                y = x;
            }
            return a;
        }
    }
    
}
