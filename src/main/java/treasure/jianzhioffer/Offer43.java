package treasure.jianzhioffer;

/**
 * 输入一个整数 n ，求1～n这n个整数的十进制表示中1出现的次数。
 *
 * 例如，输入12，1～12这些整数中包含1 的数字有1、10、11和12，1一共出现了5次
 *
 *
 * 这是一道让人见阎王的题，非常的难！
 *
 * 我不可能做出来这道题，never！
 * */
public class Offer43 {
    
    /**
     * 1234
     * 考虑 10 位这一位，左边的称为 high，右边的称为 low
     * 1. 这一位为 0，该位贡献的 1 个数为
     *      high * digit10
     * 2. 这一位是 1，该位贡献的 1 个数
     *      high * digit10 + low + 1
     *      low 是之前积攒的数据，因为自己是 1 所以每个 low 都会贡献一个 1
     * 3. 自己大于 1，那该位贡献得 1 个数
     *      (high + 1) * digit10
     *
     * */
    public int countDigitOne(int n) {
        int digit = 1;
        int low = 0;
        int high = n / 10;
        int cur = n % 10;
        int res = 0;
        while(high != 0 || cur != 0) {
            if(cur == 0) res += high * digit;
            else if(cur == 1) res += high * digit + low + 1;
            else if(cur > 1) res += (high + 1) * digit;
            
            /*
            * 注意这里不能自作聪明写成 cur = .... 然后再累加到 res 里
            * 因为 cur 是 cur，是固定值
            * 上面累加的是它造成的 1 的个数，不是一回事
            * */
            
            low += cur * digit;
            cur = high % 10;
            high /= 10;
            digit *= 10;
        }
        return res;
    }
    

}
