package treasure.jianzhioffer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 写一个函数 StrToInt，实现把字符串转换成整数这个功能。不能使用 atoi 或者其他类似的库函数
 *
 * 这个题写过好几遍了，每次都不能顺利写出来！
 * */
public class Offer67_my_atoi {
    
    /**
     * 还是得看路飞的
     * */
    public static class LuFei {
        
        public int strToInt(String str) {
            char[] arr = str.trim().toCharArray();
            if(arr.length == 0) return 0;
            int res = 0, bound = Integer.MAX_VALUE / 10;
            int i = 1, sign = 1;
            if(arr[0] == '-') sign = -1;
            else if(arr[0] != '+') i = 0;
            for (; i < arr.length; i++) {
                if(arr[i] < '0' || arr[i] > '9') break;
                if(res > bound || res == bound && arr[i] > '7') return sign > 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                res = res * 10 + arr[i] - '0';
            }
            return sign * res;
        }
    }
    
    public static class MyLongAnswer {
        
        public int strToInt(String str) {
            if(str == null || str.length() == 0) return 0;
            int len = str.length();
            int i = 0;
            char[] arr = str.toCharArray();
            while(i < len && arr[i] == ' ') i++;
            if(i == len) return 0;
            if(!(Character.isDigit(arr[i]) || arr[i] == '+' || arr[i] == '-')) return 0;
            boolean neg = arr[i] == '-';
            i = Character.isDigit(arr[i]) ? i : i + 1;
            int res = 0;
            for (; i < len; i++) {
                if(!Character.isDigit(arr[i])) break;
                int cur = arr[i] - '0';
            
                /*
                 * 不要靠记忆，推导一下！
                 * */
                if((Integer.MIN_VALUE + cur) / 10 > res) {
                    return neg ? Integer.MIN_VALUE : Integer.MAX_VALUE;
                }
                res = res * 10 - cur;
            }
            if(!neg && res == Integer.MIN_VALUE) return Integer.MAX_VALUE;
            return neg ? res : -res;
        }
    }
    
}
