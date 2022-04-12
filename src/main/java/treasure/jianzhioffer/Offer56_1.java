package treasure.jianzhioffer;

/**
 * 一个整型数组 nums 里除两个数字之外，其他数字都出现了两次。
 * 请写程序找出这两个只出现一次的数字。要求时间复杂度是O(n)，空间复杂度是O(1)
 *
 * 位运算是吧，跟我斗？
 * */
public class Offer56_1 {
    
    /**
     * 我凭记忆写下来的，牛逼吧
     * */
    public int[] singleNumbers(int[] nums) {
        int xor = 0;
        for (int num : nums) {
            xor ^= num;
        }
        // 得到异或结果后，这两个数字最低位必不同
        // 根据最低位是 0 还是 1 把数字分成两组，分别异或
        // 最终结果就是答案
        int mask = xor ^ (xor & xor - 1);
    
        int num1 = 0, num2 = 0;
        for (int num : nums) {
            if((num & mask) != 0) {
                num1 ^= num;
            } else {
                num2 ^= num;
            }
        }
        return new int[]{num1, num2};
    }
    
}
