package treasure.alg.top100;

/**
 * 实不相瞒，读完题我就看答案了
 *
 * 不会！草他妈
 * */
public class _647_count_palindrome {
    
    /**
     * 暴力枚举有两种，一个是枚举每一个子串，然后判断是不是回文！
     * 另一个是枚举每一个位置作为中心点，然后两遍扩
     * 前者 o3 后者 o2
     *
     * 怎么又把暴力方法给忘了！
     * 擦！
     *
     * */
    public static class solution1 {
    
        /**
         * 本来是要考虑中心是一个点还是两个点的，它合并成一个了，通过总结规律
         * 我感觉不好，这样虽然简短了，但是不好想
         * */
        public int countSubstrings(String s) {
            int n = s.length();
            int res = 0;
            for (int i = 0, len = 2 * n - 1; i < len; i++) {
                int l = i / 2, r = l + i % 2;
                while (l >= 0 && r < n && s.charAt(l) == s.charAt(r)) {
                    l--;
                    r++;
                    res++;
                }
            }
            return res;
        }
    
        // 我感觉两趟更好理解一点！
        // 而且这样也很快！
        public int countSubstrings_two_pass(String s) {
            int n = s.length();
            int res = 0;
            // pass1 每个点是中点
            for (int i = 0; i < n; i++) {
                int l = i, r = i;
                while (l >= 0 && r < n && s.charAt(l) == s.charAt(r)) {
                    l--;
                    r++;
                    res++;
                }
            }
            
            // pass2 每个点和它右边那个点是中点
            for (int i = 0; i < n; i++) {
                int l = i, r = i + 1;
                while (l >= 0 && r < n && s.charAt(l) == s.charAt(r)) {
                    l--;
                    r++;
                    res++;
                }
            }
            
            return res;
        }
    }
    
    public static class dp {
        
        /**
         * dp 挺好，符合我的预期！
         *
         * */
        public int countSubstrings(String s) {
            int n = s.length();
            // 最关键的就是这个东西了，他表示 i,j 这个子串是不是个回文串
            boolean[][] dp = new boolean[n][n];
            int res = 0;
            for (int j = 0; j < n; j++) {
                for (int i = 0; i <= j; i++) {
                    if (s.charAt(i) == s.charAt(j) && (j - i < 2 || dp[i + 1][j - 1])) {
                        dp[i][j] = true;
                        res++;
                    }
                }
            }
            return res;
        }
    }
}
