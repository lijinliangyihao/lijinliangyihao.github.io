package treasure.alg.jianzhioffer;

import java.math.BigInteger;
import java.util.Arrays;


/**
 * 这个题太恶心了
 * 你不用数学方法，必溢出，long 都装不下，必须 biginteger
 * */
public class Offer14_2 {
    
    long MOD = 1000000007L;
    public int cuttingRope(int n) {
        // dp 意思是，对于长度 n 的绳，最大乘积是多少
        BigInteger[] dp = new BigInteger[n + 1];
        Arrays.fill(dp, BigInteger.ONE);
    
        // 这是长度，遍历一下，从 3 开始
        for (int i = 3; i <= n; i++) {
            // 这是第一刀的长度，从 2 开始
            // 从 1 开始的话，乘积会变小
            for (int j = 2; j < i; j++) {
                dp[i] = dp[i].max(BigInteger.valueOf((long) j * (i - j))).max(BigInteger.valueOf(j).multiply(dp[i - j]));
            }
        }
        return dp[n].mod(BigInteger.valueOf(MOD)).intValue();
    }
    
}
