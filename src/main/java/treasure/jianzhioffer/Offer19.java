package treasure.jianzhioffer;

public class Offer19 {
    
    /**
     * 这个 solution 耗时 1000 余秒
     * */
    public static class Solution1 {
        public boolean isMatch(String s, String p) {
            return doMatch(s, 0, p, 0);
        }
    
        boolean doMatch(String s, int si, String p, int pi) {
            // 模式串用完，字符串必须用完；字符串用完，模式串可能还有，后面判断
            if(pi == p.length()) return si == s.length();
            // 先把星号作为一个整体判断
            if(pi < p.length() - 1 && p.charAt(pi + 1) == '*') {
                // 前一个字符匹配，可以考虑丢弃、继续匹配或匹配一个
                // 此时字符串可能用完了，所以得判断
                if(si < s.length() && (p.charAt(pi) == '.' || p.charAt(pi) == s.charAt(si))) {
                    return doMatch(s, si + 1, p, pi) || // 继续匹配
                        doMatch(s, si + 1, p, pi + 2) || // 匹配一个
                        doMatch(s, si, p, pi + 2); // 放弃匹配
                    // 前一个字符不匹配，只能丢弃
                } else {
                    return doMatch(s, si, p, pi + 2);
                }
            }
            // s 没用完，和 p 相等
            if(si < s.length() && (p.charAt(pi) == '.' || s.charAt(si) == p.charAt(pi))) {
                return doMatch(s, si + 1, p, pi + 1);
            }
            // s 用完了，或 s 当前字符和 p 不等
            return false;
        }
    }
    
    
    
}
