package treasure.alg.top100;

import java.util.Arrays;
import java.util.Stack;

/**
    temperatures = [73,74,75,71,69,72,76,73]
    [1,1,4,2,1,1,0,0]
    数字表示几天后出现更高的温度
*/
public class _739_daily_temperature {
    
    /**
     * 永远不要忘了暴力法
     *
     * 没有超时，过了
     * 但是我们有更高的追求
     * */
    public static class force {
        public int[] dailyTemperatures(int[] temperatures) {
            int n = temperatures.length;
            int[] res = new int[n];
            
            for (int i = 0; i < n; i++) {
                int cur = temperatures[i];
                for (int j = i + 1; j < n; j++) {
                    if (temperatures[j] > cur) {
                        res[i] = j - i;
                        break;
                    }
                }
            }
            return res;
        }
    }
    
    /**
     * 我以为暴力只有一种没想到有聪明的暴力！
     * */
    public static class smart_force {
        /**
         * 观察到每天的温度不超过 100 度，不小于 30 度！
         *
         * 这个感觉非常难理解，看了半天也没看懂
         *
         * 这个暴力法还挺快的！
         *
         * */
        public int[] dailyTemperatures(int[] temperatures) {
            int n = temperatures.length;
            int[] res = new int[n];
            // next 含义是，每个温度第一次出现的下标！
            int[] next = new int[101];
            Arrays.fill(next, Integer.MAX_VALUE);
    
            for (int i = n - 1; i >= 0 ; i--) {
                int warmerIndex = Integer.MAX_VALUE;
                for (int j = temperatures[i] + 1; j < 101; j++) {
                    // 这一行是从比当前温度高的所有温度里找下标最小的那个
                    if (next[j] < warmerIndex)
                        warmerIndex = next[j];
                }
                if (warmerIndex < Integer.MAX_VALUE)
                    res[i] = warmerIndex - i;
                next[temperatures[i]] = i;
            }
            
            return res;
        }
    }


    public static class official {
    
        /**
         * 我知道用单调栈，但是怎么写呢？
         *
         * 不会！操他妈
         * */
        public int[] dailyTemperatures(int[] temperatures) {
            int n = temperatures.length;
            // Deque<Integer> stack = new LinkedList<>();
            // 不知道为什么 stack 比 deque 慢五倍！没理由啊
            Stack<Integer> stack = new Stack<>();
            int[] res = new int[n];
    
            for (int i = 0; i < n; i++) {
                int t = temperatures[i];
                while (!stack.isEmpty() && t > temperatures[stack.peek()]) {
                    int prev = stack.pop();
                    res[prev] = i - prev;
                    
                }
                stack.push(i);
            }
            
            return res;
        }
    }
}
