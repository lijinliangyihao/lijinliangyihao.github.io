package treasure.alg.top100;

import java.util.*;

public class _438_find_anagrams {
    
    public static class mine {
    
        /**
         * 很快啊
         * 一看就是滑动窗口！
         *
         * 搞了半天，也算搞出来了！
         *
         * */
        public List<Integer> findAnagrams(String s, String p) {
            Map<Character, Integer> dict = new HashMap<>();
            for (char c : p.toCharArray()) {
                dict.put(c, dict.getOrDefault(c, 0) + 1);
            }
            Map<Character, Integer> currentDict = new HashMap<>();
            LinkedList<Integer> q = new LinkedList<>();
            List<Integer> res = new ArrayList<>();
        
            for (int i = 0; i < s.length(); i++) {
                currentDict.put(s.charAt(i), currentDict.getOrDefault(s.charAt(i), 0) + 1);
                q.offer(i);
                if (q.size() >= p.length()) {
                    if (same(currentDict, dict))
                        res.add(q.peekFirst());
                    int idx = q.poll();
                    Character c = s.charAt(idx);
                    int count = currentDict.get(c) - 1;
                    if (count == 0)
                        currentDict.remove(c);
                    else
                        currentDict.put(c, count);
                }
            }
            return res;
        }
    
        boolean same(Map<Character, Integer> a, Map<Character, Integer> b) {
            if (a.size() != b.size())
                return false;
            for (Map.Entry<Character, Integer> eachA : a.entrySet()) {
                Character key = eachA.getKey();
                Integer value = eachA.getValue();
            
                if (!b.containsKey(key) || !b.get(key).equals(value))
                    return false;
            }
            return true;
        }
    }
    
    /**
     * 我的解法也不是不对，只是这种定长的可以用两个数组来搞
     * */
    public static class array {
    
        public List<Integer> findAnagrams(String s, String p) {
            
            // 这种条件有什么意义？
            if (p.length() > s.length())
                return Collections.emptyList();
            
            int[] dict = new int[26];
            int[] curDict = new int[26];
            
            // 没有十年脑血栓写不出这种代码
//            for (char c : p.toCharArray()) {
//                dict[c - 'a']++;
//                curDict[c - 'a']++;
//            }
    
            for (int i = 0, len = p.length(); i < len; i++) {
                dict[p.charAt(i) - 'a']++;
                curDict[s.charAt(i) - 'a']++;
            }
    
            List<Integer> res = new ArrayList<>();
            if (Arrays.equals(dict, curDict))
                res.add(0);
            
            // 你这写成 p.length()，你没有理解
            for (int i = 0, len = s.length() - p.length(); i < len; i++) {
                curDict[s.charAt(i) - 'a']--;
                curDict[s.charAt(i + p.length()) - 'a']++;
        
                // 你这写成 i，你不理解
                if (Arrays.equals(dict, curDict))
                    res.add(i + 1);
            }
            return res;
        }
    }
    
    /**
     * 两个数组也不是不对，只是可以用一个数组加一个变量来搞
     * */
    public static class diff {
        public static void main(String[] args) {
            System.out.println(new diff().findAnagrams("baa", "aa"));
        }
        public List<Integer> findAnagrams(String s, String p) {
            if (p.length() > s.length())
                return Collections.emptyList();
            
            int[] dict = new int[26];
            /*
             * 这容易出错，s 是 ++，增加了 diff，p 是 --，存在的话减少了 diff
             * 写反的话 gg
             * */
            for (int i = 0; i < p.length(); i++) {
                dict[s.charAt(i) - 'a']++;
                dict[p.charAt(i) - 'a']--;
            }
            
            List<Integer> res = new ArrayList<>();
            int diff = (int) Arrays.stream(dict).filter(i -> i != 0).count();
            if (diff == 0)
                res.add(0);
    
            for (int i = 0; i < s.length() - p.length(); i++) {
                if (dict[s.charAt(i) - 'a'] == 1)
                    diff--;
                else if (dict[s.charAt(i) - 'a'] == 0)
                    diff++;
                dict[s.charAt(i) - 'a']--;
    
                if (dict[s.charAt(i + p.length()) - 'a'] == -1)
                    diff--;
                else if (dict[s.charAt(i + p.length()) - 'a'] == 0)
                    diff++;
                dict[s.charAt(i + p.length()) - 'a']++;
                
                if (diff == 0)
                    res.add(i + 1);
            }
            
            return res;
        }
    
    }
}
