package treasure.alg.top100;

import java.util.HashMap;
import java.util.Map;

public class _76_least_cover_subsequence {
    
    
    // 普通答案，有优化空间，它没优化，我也懒得找了
    public static class Solution1 {
    
        public String minWindow(String s, String t) {
            if (s.length() == 0 || s.length() < t.length()) return "";
            Map<Character, Integer> tmap = new HashMap<>();
            for (int i = 0; i < t.length(); i++) {
                tmap.put(t.charAt(i), tmap.getOrDefault(t.charAt(i), 0) + 1);
            }
            Map<Character, Integer> smap = new HashMap<>();
            int begin = -1, len = Integer.MAX_VALUE;
            for (int l = 0, r = 0; r < s.length(); r++) {
                smap.put(s.charAt(r), smap.getOrDefault(s.charAt(r), 0) + 1);
                while(contains(smap, tmap) && l <= r) {
                    if (r - l + 1 < len) {
                        begin = l;
                        len = r - l + 1;
                    }
                    smap.put(s.charAt(l), smap.get(s.charAt(l)) - 1);
                    l++;
                }
            }
            return begin == -1 ? "" : s.substring(begin, begin + len);
        }
        
        boolean contains(Map<Character, Integer> smap, Map<Character, Integer> tmap) {
            // 可以多不能少
            for (Map.Entry<Character, Integer> each : tmap.entrySet()) {
                if(smap.getOrDefault(each.getKey(), 0) < each.getValue()) return false;
            }
            return true;
        }
    }
    
    /**
     * 我自己的思路，比较两个 map 看是不是一样
     * 当然了，没做出来，放弃了
     *
     * 修复了一些问题，剩下的其实和答案差不多（除了没写出来的部分）
     *
     * */
    public static class MyBadAnswer {
        public String minWindow(String s, String t) {
            if (s.length() == 0 || s.length() < t.length()) return "";
            Map<Character, Integer> tmap = new HashMap<>();
            for (int i = 0; i < t.length(); i++) {
                tmap.put(t.charAt(i), tmap.getOrDefault(t.charAt(i), 0) + 1);
            }
            Map<Character, Integer> smap = new HashMap<>();
            // 这是最终结果，还需要两个指针
            int from = 0, len = 0;
            for (int i = 0; i < s.length(); i++) {
                smap.put(s.charAt(i), smap.getOrDefault(s.charAt(i), 0) + 1);
                while (contains(smap, tmap)) {
                
                }
            }
            return s.substring(from, from + len);
        }
    
        boolean contains(Map<Character, Integer> a, Map<Character, Integer> b) {
            // 这不能相等，需要注释掉
            if (a.size() == b.size()) {
                // 应该遍历 b 看 a 是否包含
                for (Map.Entry<Character, Integer> each : a.entrySet()) {
                    if(each.getValue() < b.get(each.getKey())) return false;
                }
            }
            return false;
        }
    }
}
