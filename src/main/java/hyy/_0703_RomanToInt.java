package hyy;

import java.util.HashMap;
import java.util.Map;

public class _0703_RomanToInt {
    public static void main(String[] args) {
        System.out.println(romanToInt("III"));
        System.out.println(romanToInt("IV"));
    }

    static Map<Character, Integer> symbolValues = new HashMap<Character, Integer>() {{
        put('I', 1);
        put('V', 5);
        put('X', 10);
        put('L', 50);
        put('C', 100);
        put('D', 500);
        put('M', 1000);
    }};

    /**
     * 罗马数字转整数 :
     * 1、自己没有思路,直接看官方题解
     * 2、一般而言,罗马数字中小的数字在大的数字右边,所以直接用加即可
     * 3、特殊情况,罗马数字中小的数字在大的数字左边,所以直接把小的数字变成负数即可
     * @param s 罗马数字
     * @return int 整数
     */
    public static int romanToInt(String s) {
        int ans = 0;
        int n = s.length();
        for(int i=0;i<n;i++) {
            int value = symbolValues.get(s.charAt(i));
            if(i < n-1 && value < symbolValues.get(s.charAt(i+1))){
                ans = ans - value;
            } else {
                ans = ans + value;
            }
        }
        return  ans;
    }
}
