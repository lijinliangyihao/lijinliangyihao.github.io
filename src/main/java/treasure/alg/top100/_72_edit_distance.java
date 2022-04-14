package treasure.alg.top100;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class _72_edit_distance {
    
    /*
    * def minDistance(self, word1: str, word2: str) -> int:
        import functools
        @functools.lru_cache(None)
        def helper(i, j):
            if i == len(word1) or j == len(word2):
                return len(word1) - i + len(word2) - j
            if word1[i] == word2[j]:
                return helper(i + 1, j + 1)
            else:
                inserted = helper(i, j + 1)
                deleted = helper(i + 1, j)
                replaced = helper(i + 1, j + 1)
                return min(inserted, deleted, replaced) + 1
        return helper(0, 0)

    * */
    
    /**
     * 来个自顶向下版本
     * 这个必须加个缓存，不然完全没法用，重复子问题太多了
     * */
    public static class TopDown {
        public int minDistance(String word1, String word2) {
            return helper(word1, 0, word2, 0);
        }
        static class Pos {
            int a, b;
            static Pos of(int a, int b) {
                Pos p = new Pos();
                p.a = a; p.b = b;
                return p;
            }
            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Pos pos = (Pos) o;
                return a == pos.a && b == pos.b;
            }
            @Override
            public int hashCode() {
                return Objects.hash(a, b);
            }
        }
        Map<Pos, Integer> cache = new HashMap<>();
        public int helper(String word1, int ind1, String word2, int ind2) {
            Pos p = Pos.of(ind1, ind2);
            if(cache.containsKey(p)) return cache.get(p);
            if(ind1 == word1.length()) return word2.length() - ind2;
            if(ind2 == word2.length()) return word1.length() - ind1;
            if(word1.charAt(ind1) == word2.charAt(ind2)) return helper(word1, ind1 + 1, word2, ind2 + 1);
            int insert = helper(word1, ind1, word2, ind2 + 1);
            int delete = helper(word1, ind1 + 1, word2, ind2);
            int replace = helper(word1, ind1 + 1, word2, ind2 + 1);
            int d = Math.min(insert, Math.min(delete, replace)) + 1;
            cache.put(p, d);
            return d;
        }
    }
    
    
    /**
     * 没做出来，我日
     * */
    public static class Dp {
        public int minDistance(String word1, String word2) {
            int m = word1.length();
            int n = word2.length();
            if(m == 0) return n;
            if(n == 0) return m;
            int[][] dp = new int[m + 1][n + 1];
        
            for (int i = 1; i <= m; i++) {
                dp[i][0] = i;
            }
            for (int i = 1; i <= n; i++) {
                dp[0][i] = i;
            }
        
            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    int leftUp = dp[i - 1][j - 1];
                    if(word1.charAt(i - 1) != word2.charAt(j - 1)) leftUp += 1;
                    dp[i][j] = Math.min(leftUp, Math.min(dp[i - 1][j], dp[i][j - 1]) + 1);
                }
            }
        
            return dp[m][n];
        }
    }
    
}
