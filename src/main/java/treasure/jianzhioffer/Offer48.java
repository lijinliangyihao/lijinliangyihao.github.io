package treasure.jianzhioffer;

import java.util.*;

/**
 * 请从字符串中找出一个最长的不包含重复字符的子字符串，计算该最长子字符串的长度
 *
 *
 *
 *
 * */
public class Offer48 {
    
    /**
     * 这个比 hash + 线性遍历、hash + 双指针都要快
     *
     * dp，每个元素表示至此最长的字符串值
     * 配合 hash 表，存每个元素上一次元素出现索引 i
     * 1. i < 0，字符串长度 j - i
     * 2. dp[i - 1] < j - i 意思是上个字符的长度，比当前字符本次上次出现到这的距离短，那直接 +1
     *      dp[i] = dp[i - 1] + 1
     * 3. dp[i - 1] >= j - i 当前字符出现在上个字符串内部了，
     *      dp[i] = j - i;
     *
     * 作者用一个变量来代替上一轮的长度，省去了 dp O(n) 空间消耗
     * */
    public static class HashAndDp {
        
        public int lengthOfLongestSubstring(String s) {
            Map<Character, Integer> dic = new HashMap<>();
            int res = 0, tmp = 0;
            for(int j = 0; j < s.length(); j++) {
                int i = dic.getOrDefault(s.charAt(j), -1); // 获取索引 i
                dic.put(s.charAt(j), j); // 更新哈希表
                tmp = tmp < j - i ? tmp + 1 : j - i; // dp[j - 1] -> dp[j]
                res = Math.max(res, tmp); // max(dp[j - 1], dp[j])
            }
            return res;
        }
    }
    
    public static class HashAndDoublePointer {
        
        public int lengthOfLongestSubstring(String s) {
            Map<Character, Integer> dic = new HashMap<>();
            int i = -1, res = 0;
            for(int j = 0; j < s.length(); j++) {
                if(dic.containsKey(s.charAt(j)))
                    i = Math.max(i, dic.get(s.charAt(j))); // 更新左指针 i
                dic.put(s.charAt(j), j); // 哈希表记录
                res = Math.max(res, j - i); // 更新结果
            }
            return res;
        }
    
        public int lengthOfLongestSubstring2(String s) {
            Map<Character, Integer> dic = new HashMap<>();
            int i = -1, res = 0;
            for (int j = 0; j < s.length(); j++) {
                if(dic.containsKey(s.charAt(j))) {
                    i = Math.max(i, dic.get(s.charAt(j)));
                }
                dic.put(s.charAt(j), j);
                res = Math.max(res, j - i);
            }
            return res;
        }
        
    }
    
    /**
     * 我的愚蠢的解法
     * */
    public static class MyAnswer {
        public int lengthOfLongestSubstring(String s) {
            if(s == null || s.length() == 0) return 0;
            Set<Character> set = new HashSet<>();
            Queue<Character> q = new LinkedList<>();
            int max = 1;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if(set.contains(c)) {
                    while(!q.isEmpty() && q.peek() != c) {
                        set.remove(q.poll());
                    }
                    q.poll();
                    q.offer(c);
                } else {
                    set.add(c);
                    q.offer(c);
                    max = Math.max(max, q.size());
                }
            }
            return max;
        }
    }
    
    
}
