package treasure.alg.jianzhioffer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 输入一个正整数 target ，输出所有和为 target 的连续正整数序列（至少含有两个数）。
 *
 * 序列内的数字由小到大排列，不同序列按照首个数字从小到大排列
 *
 * 这题，简单？放屁！
 *
 * 就这想难倒我？做梦！
 * */
public class Offer57_2 {
    
    
    
    public static class MyAnswer {
        // 肯定是维护一个和，这个和 = target 就保存，然后右移右边那个指针
        // 类似于滑动窗口
        // 9
        // 1 2 3 4 5 6 7 8 9
        public int[][] findContinuousSequence(int target) {
            List<int[]> res = new ArrayList<>();
            int start = 1, end = 2, sum = 3;
            while(start < end) {
                if(sum == target) {
                    res.add(IntStream.rangeClosed(start, end).toArray());
                }
                if(sum <= target) {
                    end++;
                    sum += end;
                } else {
                    sum -= start;
                    start++;
                }
            }
            return res.toArray(new int[0][]);
        }
    }
    
    /**
     * 路飞的解法，它是利用求和公式，不是很推荐，比较奇怪
     * target = (j - i + 1) * (i + j) / 2
     * 元素平均值 * 元素数量 = 总和，反正这就是求和公式，也不知道咋来的，忘干净了
     * 解一元二次方程，舍去负的，得出来下面这个 j 的表达式，然后遍历 i，直到遇到 j 为止
     * 取 j 的整数解，作为结果
     *
     * i 最大能到 10^5 可能溢出，所以中间转为 long
     *
     * */
    public static class Solution {
        public int[][] findContinuousSequence(int target) {
            int i = 1;
            double j = 2.0;
            List<int[]> res = new ArrayList<>();
            while(i < j) {
                j = (-1 + Math.sqrt(1 + 4 * (2L * target + (long) i * i - i))) / 2;
                if(i < j && j == (int)j) {
                    res.add(IntStream.rangeClosed(i, (int)j).toArray());
                }
                i++;
            }
            return res.toArray(new int[0][]);
        }
    }
    
}
