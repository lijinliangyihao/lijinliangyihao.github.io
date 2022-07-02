package hyy;

public class Plaindroom0702 {
    public static void main(String[] args) {
        isPalindroomByMyself(121);
        isPlainroomByOfficial(123);
    }


    /**
     * 看官方题解前,自己的解题思路：
     * 1、了解回文数的含义,正过来念,反过来念,都是一样的。
     * 2、想到7月1日做的题,数字反转,反转后的数=反转前的数,就是回文数了呀！
     * 3、因此开始coding吧！
     * @param x  需判断的数字
     * @return boolean true:回文数 , false: 非回文数
     */
    public static boolean isPalindroomByMyself(int x) {
        //排除负数，负数不可能为回文数
        if(x < 0 ) {
            return false;
        }

        //此处利用数字反转的逻辑
        int oldNum = x;
        int rev = 0;
        while(x > 0) {
            int digit = x % 10;
            x = x / 10;
            rev = rev * 10 + digit;
        }

        //将反转后的数字与反转前的数字比较,如相等,则是回文数,反之,不是回文数
        if(rev == oldNum) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 官方题解的思路
     * @param x
     * @return boolean
     */
    public static boolean isPlainroomByOfficial(int x) {

        //自己未考虑到过滤数字尾数为0且数字本身不为0的数字
        if(x < 0 || (x % 10 == 0 && x != 0)){
            return false;
        }

        //根据回文数的特性,可以反转数字的一半, 后半部分反转后应该与原始数字的前半部分一致
        //需要考虑数字长度为奇数、偶数的情况
        //奇数: 后半部分反转 > 前半部分 , 可以停止反转
        //偶数: 后半部分反转 = 前半部分 , 可以停止反转
        int rev = 0;
        while(x > rev) {
            rev = rev * 10 + x % 10;
            x = x / 10;
        }

        //判断是不是回文数,需要考虑数字长度奇数、偶数的情况
        //偶数: 反转后的数字 == 前半部分数字 , 即rev == x
        //奇数: 反转后的数字去掉最后一位 == 前半部分数字 , 即rev / 10 == x
        return rev == x || rev / 10 == x;

    }
}
