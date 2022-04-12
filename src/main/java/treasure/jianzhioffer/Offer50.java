package treasure.jianzhioffer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * 在字符串 s 中找出第一个只出现一次的字符。如果没有，返回一个单空格。 s 只包含小写字母。
 *
 * 只有小字母，这是个好消息，但是索引怎么存？用 node 吗
 *
 * 我的第二次提交通过了，感觉方法有些愚蠢！
 *
 *
 * */
public class Offer50 {
    
    public static class MyAnswer {
        
        public char firstUniqChar(String s) {
            class Node {
                int index;
                int count;
            }
            // 26 letter
            Node[] chars = new Node[26];
            for (int i = 0; i < s.length(); i++) {
                int index = s.charAt(i) - 'a';
                Node node = chars[index];
                if(node == null) {
                    node = new Node();
                    node.count = 1;
                    node.index = i;
                    chars[index] = node;
                } else {
                    node.count++;
                }
            }
            int min = s.length();
            char res = ' ';
            for (int i = 0; i < 26; i++) {
                if(chars[i] != null) {
                    if(chars[i].count == 1 && chars[i].index < min) {
                        min = chars[i].index;
                        res = (char)('a' + i);
                    }
                }
            }
            return res;
        }
    }
    
    /**
     * 它这个比我的慢，但是想法还可以，我们来试试
     * 它的空间使用也比我的小
     * */
    public static class Solution {
    
        public char firstUniqChar(String s) {
            Map<Character, Integer> map = new HashMap<>();
            Queue<Character> q = new LinkedList<>();
            for (int i = 0; i < s.length(); i++) {
                if(!map.containsKey(s.charAt(i))) {
                    map.put(s.charAt(i), 1);
                    q.offer(s.charAt(i));
                } else {
                    map.put(s.charAt(i), -1);
                    while(!q.isEmpty() && map.get(q.peek()) == -1) {
                        q.poll();
                    }
                }
            }
            return q.isEmpty() ? ' ' : q.peek();
        }
    }
}
