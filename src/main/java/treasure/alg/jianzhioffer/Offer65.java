package treasure.alg.jianzhioffer;

/**
 * 写一个函数，求两个整数之和，要求在函数体内不得使用 “+”、“-”、“*”、“/” 四则运算符号
 * 输入: a = 1, b = 1
 * 输出: 2
 *
 * 1111
 *  111
 *
 *  记录低位
 *  两个数移走低位
 *  保存进位
 *  累加结果
 *  掩码位移
 *
 * 0 1 1 1 0 1
 * 提示：
 *
 * a, b 均可能是负数或 0
 * 结果不会溢出 32 位整数
 *
 * 这个题你好意思标记简单？
 * */
public class Offer65 {
    
    /**
     * 两个数相加，分为两块，一块是直接和，另一块是进位
     * 进位，每次都是累加的，可能影响后面所有位，需要循环一位一位的进行运算
     *
     * n = a + b = s + c
     * 这么写有些问题，a + b 是一次性加的，s 和 c 是分好几次加的，每次丢弃一次 c 的低位
     *
     * 按着这个思路代码呼之欲出了
     *
     * */
    public static class Luffy {
    
        /*
        * s，和 c
        * */
        public int add(int a, int b) {
            int s = a ^ b;
            int c = (a & b) << 1;
            while(c != 0) {
                int o = s;
                s ^= c;
                c = (o & c) << 1;
            }
            return s;
        }
        
        /*
        * 这个更简短但是职责不分明，不好
        * */
        public int add1(int a, int b) {
            while(b != 0) { // 当进位为 0 时跳出
                int c = (a & b) << 1;  // c = 进位
                a ^= b; // a = 非进位和
                b = c; // b = 进位
            }
            return a;
        }
        
    }
    
    /**
     * 这次我感觉我不是傻逼
     * 写了一个多小时，这个题，一个半小时以上
     * 每行代码，都有我的血汗
     * */
    public static class Mine {
        public int add(int a, int b) {
            int res = 0;
            int carry = 0;
            int shift = 0;
            int cur;
            while(a != 0 || b != 0 || carry != 0) {
                // 尤其是这行代码
                if(shift == 32) break;
                int bit1 = a & 1;
                int bit2 = b & 1;
                a >>>= 1;
                b >>>= 1;
            
                // 还有这两行代码
                if(bit1 != bit2) cur = 1 ^ carry;
                else cur = carry;
            
                res |= cur << shift;
                shift++;
            
                if((bit1 & bit2) == 1) carry = 1;
                else if((bit1 | bit2) == 1) carry &= 1;
                else carry = 0;
            }
            return res;
        }
    }

}
