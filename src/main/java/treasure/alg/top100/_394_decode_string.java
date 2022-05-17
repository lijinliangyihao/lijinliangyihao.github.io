package treasure.alg.top100;

import java.util.Collections;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * 别人比我聪明的多，我只需要把别人的答案粘过来
 *
 * 这样我也变得聪明了
 *
 * 3[a]2[bc]
 * aaabcbc
 *
 * */
public class _394_decode_string {
    
    /**
     * 一个路人提供的答案，这个解法很快，也很优雅
     * */
    public static class passerby {
        
        public String decodeString(String s) {
            // res 乍一看是个 res，其实代表当前 res
            StringBuilder res = new StringBuilder();
            Stack<Integer> numberStack = new Stack<>();
            Stack<StringBuilder> bufStack = new Stack<>();
        
            int num = 0;
            for (char c : s.toCharArray()) {
                if (Character.isDigit(c))
                    num = num * 10 + c - '0';
                else if (c == '[') {
                    numberStack.push(num);
                    num = 0;
                    bufStack.push(res);
                    res = new StringBuilder();
                } else if (c == ']') {
                    StringBuilder lastBuf = bufStack.pop();
                    int count = numberStack.pop();
                    for (int i = 0; i < count; i++) {
                        lastBuf.append(res);
                    }
                    res = lastBuf;
                } else {
                    res.append(c);
                }
            }
        
            return res.toString();
        }
    }
    
    /**
     * 这个解法只用一个栈，但是代码要复杂一些稍微蠢点就是
     * */
    public static class official1 {
    
        public static void main(String[] args) {
            new official1().decodeString("2[a]3[b]");
        }
        
        public String decodeString(String s) {
            Stack<String> stack = new Stack<>(); // 所有东西都在这个栈里了
            int p = 0;
            char c;
            while (p < s.length()) {
                c = s.charAt(p);
                // 我这 c 写成 p 了，找了好几个小时没找到
                // 咋整啊，这种低级错误！！！！！！
                if (Character.isDigit(c)) {
                    String numStr = scanNumStr(s, p);
                    p += numStr.length();
                    stack.push(numStr);
                } else if (Character.isLetter(c) || c == '[') {
                    stack.push(String.valueOf(c));
                    p++;
                } else {
                    p++;
                    Stack<String> tmp = new Stack<>();
                    String token;
                    while (!"[".equals(token = stack.pop())) {
                        tmp.push(token);
                    }
                    Collections.reverse(tmp);
                    int count = Integer.parseInt(stack.pop());
                    String part = toString(tmp);
                    StringBuilder sb = new StringBuilder();
                    while (count-- > 0)
                        sb.append(part);
                    stack.push(sb.toString());
                }
            }
            return toString(stack);
        }
        
        String scanNumStr(String s, int p) {
            StringBuilder sb = new StringBuilder();
            char c;
            while (p < s.length() && Character.isDigit(c = s.charAt(p))) {
                sb.append(c);
                p++;
            }
            return sb.toString();
        }
        
        String toString(Stack<String> stack) {
            return String.join("", stack);
        }
    }
    
    /**
     * 这个解法用到了编译原理，所以我想看看
     *
     * 这是我理想的答案
     * 反思一下自己为什么写不出来
     *
     * */
    public static class official2 {
        int p;
        String buf;
        public String decodeString(String s) {
            buf = s;
            return getString();
        }
        
        String getString() {
            if (p == buf.length() || buf.charAt(p) == ']')
                return "";
            
            String ret = null;
            int count;
            char c = buf.charAt(p);
            if (Character.isDigit(c)) {
                count = scanInt();
                p++;
                ret = getString();
                p++;
                StringBuilder sb = new StringBuilder();
                while (count-- >= 0)
                    sb.append(ret);
                ret = sb.toString();
            } else if (Character.isLetter(c)) {
                ret = scanStr();
            }
            return ret + getString();
        }
        
        int scanInt() {
            int num = 0;
            char c;
            while (p < buf.length() && Character.isDigit(c = buf.charAt(p))) {
                num = num * 10 + c - '0';
                p++;
            }
            return num;
        }
        String scanStr() {
            StringBuilder sb = new StringBuilder();
            char c;
            while (p < buf.length() && Character.isLetter(c = buf.charAt(p))) {
                sb.append(c);
                p++;
            }
            return sb.toString();
        }
    }
    
}
