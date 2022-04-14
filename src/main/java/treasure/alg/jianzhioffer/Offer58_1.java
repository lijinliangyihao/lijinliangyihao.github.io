package treasure.alg.jianzhioffer;

import treasure.alg.common.TreeNode;

/**
 * 输入一个英文句子，翻转句子中单词的顺序，但单词内字符的顺序不变。
 * 为简单起见，标点符号和普通字母一样处理。例如输入字符串"I am a student. "，
 * 则输出"student. a am I"
 *
 * 我去这个题这么难吗，半天了没写对
 * */
public class Offer58_1 {
    
    /**
     * 我用了最聪明的办法只击败了 16% ？
     * */
    public static class MySolution {
        char[] arr;
        public String reverseWords(String s) {
            s = s.trim().replaceAll(" +", " ");
            arr = s.toCharArray();
            reverse(arr, 0, s.length() - 1);
        
            int start = 0;
            for (int i = 0; i < arr.length; i++) {
                if(i == arr.length - 1 || arr[i + 1] == ' ') {
                    reverse(arr, start, i);
                    start = i + 2;
                }
            }
        
            return new String(arr);
        }
    
        public void reverse(char[] arr, int start, int end) {
            while(start < end) {
                char t = arr[start];
                arr[start] = arr[end];
                arr[end] = t;
                start++;
                end--;
            }
        }
    }
    
    /**
     * 还有个正则 split 然后倒序 append 就不写了，没啥意思！
     * */
    public static class Faster {
        
        public String reverseWords(String s) {
            StringBuilder sb = new StringBuilder();
            int n = s.length();
            int i = n - 1;
            while(i >= 0 && s.charAt(i) == ' ') i--;
            int j = i;
            while(i >= 0) {
                while(i >= 0 && s.charAt(i) != ' ') i--;
                sb.append(s, i + 1, j + 1).append(" ");
                while(i >= 0 && s.charAt(i) == ' ') i--;
                j = i;
            }
            if(sb.length() == 0) return "";
            return sb.deleteCharAt(sb.length() - 1).toString();
        }
    }
    
}
