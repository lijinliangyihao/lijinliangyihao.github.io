package treasure.jianzhioffer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 输入数字 n，按顺序打印出从 1 到最大的 n 位十进制数。
 * 比如输入 3，则打印出 1、2、3 一直到最大的 3 位数 999
 *
 * */
public class Offer17 {
    
    /**
     * 这个解法是不对的
     * 剑指 offer 里，需要考虑大数情况，大到 long 都装不下
     * */
    public static class MyAnswer {
    
        public int[] printNumbers(int n) {
            int count = (int) Math.pow(10, n);
            return IntStream.range(1, count).toArray();
        }
    }
    
    /**
     * 正统解法，使用全排列，返回字符串
     * 不过这道题要求返回数字
     * 但原题本意是拿大数折磨你
     * */
    public static class Solution {
    
        
        public int[] printNumbers(int n) {
            for(int i = 1; i <= n; i++)
                dfs(0, i);
            return res.stream().mapToInt(Integer::intValue).toArray();
        }
    
        char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        List<Integer> res = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        
        void dfs(int index, int len) {
            if(index == len) {
                res.add(Integer.parseInt(sb.toString()));
                return;
            }
            // 这不这么写会出问题
            int start = index == 0 ? 1 : 0;
            for (int i = start; i < 10; i++) {
                sb.append(chars[i]);
                dfs(index + 1, len);
                sb.deleteCharAt(sb.length() - 1);
            }
        }
    }

}
