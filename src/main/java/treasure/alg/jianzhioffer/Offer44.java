package treasure.alg.jianzhioffer;

import treasure.alg.common.ListNode;

/**
 * 数字以0123456789101112131415…的格式序列化到一个字符序列中。
 * 在这个序列中，第5位（从下标0开始计数）是5，第13位是1，第19位是4，等等。
 * 请写一个函数，求任意第n位对应的数字
 *
 * */
public class Offer44 {
    
    public static class MyStupidWrongAnswer {
        public int findNthDigit(int n) {
            if(n == 0) return 0;
            int digit = 1;
            int start = 1;
            int count = 9;
            while(count < n) {
                n -= count;
                digit++;
                start *= 10;
                count *= digit * start;
            }
            int num = start + (n - 1) / digit;
            int p = (n - 1) % digit;
            p = digit - p - 1;
            while(p > 0) {
                num /= 10;
                p--;
            }
            return num % 10;
        }
    }
    
    /**
     * 别人想出来的解法，总是令人难以想到
     * 这个题真是非常鸡巴的阴，非常鸡巴的令人气愤
     * 不这么写，就是错！我操他妈的！
     * 无能狂怒！
     *
     * */
    public static class Solution {
        public int findNthDigit(int n) {
            if(n == 0) return 0;
            int digit = 1;
            int start = 1;
            long count = 9;
            while(count < n) {
                n -= count;
                digit++;
                start *= 10;
                count = 9L * digit * start;
            }
            int num = start + (n - 1) / digit;
            
            /*
            * 注释的部分是原书的意思，这个 n - 1
            * 把我搞得很崩溃
            * 花了太多时间，这道题，下次吧
            * */
//            int p = digit - (n - 1) % digit - 1;
//            while(p > 0) {
//                num /= 10;
//                p--;
//            }
//            return num % 10;
            return Integer.toString(num).charAt((n - 1) % digit) - '0';
        }
    }

}
