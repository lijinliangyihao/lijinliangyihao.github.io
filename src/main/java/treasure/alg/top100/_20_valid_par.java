package treasure.alg.top100;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class _20_valid_par {
    
    /**
    * 妈的。。。改了五次才改对！
    * */
    public boolean isValid(String s) {
        char[] stack = new char[s.length()];
        int i = -1;
        for (int j = 0; j < s.length(); j++) {
            char c = s.charAt(j);
            switch (c) {
                case ')': if (i < 0 || stack[i--] != '(') return false;
                    break;
                case ']': if (i < 0 || stack[i--] != '[') return false;
                    break;
                case '}': if (i < 0 || stack[i--] != '{') return false;
                    break;
                default: stack[++i] = c;
            }
        }
        return i == -1;
    }
    
    /**
    * 这一版优化了点空时使用。。。这次改了 7 次才蒙对！
    * 去你妈的不改了！
    * */
    public boolean isValid_v2(String s) {
        if (s.length() % 2 != 0) return false;
        int stackLen = Math.max(s.length()/2, 1);
        char[] stack = new char[stackLen];
        int i = -1;
        for (int j = 0; j < s.length(); j++) {
            char c = s.charAt(j);
            switch (c) {
                case ')': if (i < 0 || i >= stackLen || stack[i--] != '(') return false;
                    break;
                case ']': if (i < 0 || i >= stackLen || stack[i--] != '[') return false;
                    break;
                case '}': if (i < 0 || i >= stackLen || stack[i--] != '{') return false;
                    break;
                default: if (++i >= stackLen) return false; stack[i] = c;
            }
        }
        return i == -1;
    }
    
    /**
    * 这一把改的正常了点，上面那个 -1 令人不快！
    * */
    public boolean isValid_v3(String s) {
        if (s.length() % 2 != 0) return false;
        int stackLen = Math.max(s.length()/2, 1);
        char[] stack = new char[stackLen];
        int i = 0;
        for (int j = 0; j < s.length(); j++) {
            char c = s.charAt(j);
            switch (c) {
                case ')': if (i == 0 || stack[--i] != '(') return false;
                    break;
                case ']': if (i == 0 || stack[--i] != '[') return false;
                    break;
                case '}': if (i == 0 || stack[--i] != '{') return false;
                    break;
                default: if (i >= stackLen) return false; stack[i++] = c;
            }
        }
        return i == 0;
    }
    
    static Map<Character, Character> map = new HashMap<>();
    static {
        map.put(')', '(');
        map.put('}', '{');
        map.put(']', '[');
        map.put('?', '?');
    }
    
    /**
     * 路飞的解法
     * 路飞搞了一个 映射，和一个毒丸，挺妙的
     * */
    public boolean isValid_lufi(String s) {
        if (s.length() % 2 != 0) return false;
        int maxLen = s.length() / 2 + 1;
        char[] stack = new char[maxLen];
        stack[0] = '?';
        int i = 1;
        for (int j = 0; j < s.length(); j++) {
            char c = s.charAt(j);
            if (map.containsKey(c)) {
                if (map.get(c) != stack[i - 1])
                    return false;
                i--;
            } else {
                if (i == maxLen)
                    return false;
                stack[i++] = c;
            }
        }
        return i == 1;
    }
    
    /**
     * 前面用了数组，用栈试试吧
     * 数组下标维护起来有一定易错性
     *
     * 卧槽，这个居然比数组还快，空间还省
     * 我再也不装逼了！
     *
     * */
    public boolean isValid_lufi_v2(String s) {
        if (s.length() % 2 != 0) return false;
        LinkedList<Character> stack = new LinkedList<>();
        stack.offer('?');
        for (int j = 0; j < s.length(); j++) {
            char c = s.charAt(j);
            if (map.containsKey(c)) {
                if (map.get(c) != stack.pop())
                    return false;
            } else {
                stack.push(c);
            }
        }
        return stack.size() == 1;
    }
    
}
