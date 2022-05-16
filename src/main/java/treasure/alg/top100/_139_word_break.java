package treasure.alg.top100;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class _139_word_break {
    
    /**
     * 我感觉啊，是从第一个字母开始找，找不到了再回溯
     * 说白了就是回溯
     * 怎么回溯呢？我不是很熟！
     *
     * "leetcode", wordDict = ["leet", "code"]
     *
     * 超时我透！
     *
     * */
    public static class backtrack_but_timeout {
        
        public boolean wordBreak(String s, List<String> wordDict) {
            Set<String> set = new HashSet<>(wordDict);
            for (int i = 0; i < s.length(); i++) {
                if (backtrack(s, 0, i + 1, set))
                    return true;
            }
            return false;
        }
    
        // 既然是回溯，那我先来个 helper 方法
        // 你这个 from 和 to 是固定的还传它干啥？
        boolean backtrack(String s, int from, int index, Set<String> dict) {
        
            if (!dict.contains(s.substring(from, index)))
                return false;
            else if (index == s.length())
                return true;
        
            for (int i = index; i < s.length(); i++) {
                if (backtrack(s, index, i + 1, dict))
                    return true;
            }
        
            return false;
        }
    }
    
    
    public static class official_answer {
    
        public boolean wordBreak(String s, List<String> wordDict) {
            int n = s.length();
            Set<String> dict = new HashSet<>(wordDict);
            boolean[] dp = new boolean[n + 1];
            dp[0] = true;
    
            for (int i = 0; i <= n; i++) {
                for (int j = 0; j < i; j++) {
                    if (dp[j] && dict.contains(s.substring(j, i))) {
                        dp[i] = true;
                        break;
                    }
                }
            }
    
            return dp[n];
        }
    }
    
}
