package treasure.alg.jianzhioffer;

/**
 * 给定一个数组 A[0,1,…,n-1]，请构建一个数组 B[0,1,…,n-1]，
 * 其中B[i] 的值是数组 A 中除了下标 i 以外的元素的积,
 * 即B[i]=A[0]×A[1]×…×A[i-1]×A[i+1]×…×A[n-1]。
 *
 * 不能使用除法
 *
 * 输入: [1,2,3,4,5]
 * 输出: [120,60,40,30,24]
 *
 * 这个题我自然是不会的。
 * 但是我会试试，毕竟也三十多岁的人了！
 *
 * 一点都不难我自己写出来了我日！
 *
 * */
public class Offer66_cheng_ji_shu_zu {
    
    // not very effective but works!
    public static class Mine {
        /*
    
    b0   A[1] x A[2] x ... x A[n-2] x A[n-1] x A[n]
    b1   A[0] x A[2] x ... x A[n-2] x A[n-1] x A[n]
    ...
    bn-1 A[0] x A[1] x ... x A[n-3] x A[n-2] x A[n]
    bn   A[0] x A[1] x ... x A[n-3] x A[n-2] x A[n-1]
    
    本来把所有 a 乘起来，然后每个 b 除掉对应的 ai 就行了，现在不让用除法，只能看递推了
    假设 bi = ci * di
    
    ci 从上往下递增，di 从下往上递增
    然后乘起来就是 bi，放到数组里返回
    
    ci = a0....ai-1
    di = ai+1...an-1
    
    是不是搞两个数组分别存 c 和 d？
    c: 1 a0 a0a1
    
    
    * */
        public int[] constructArr(int[] a) {
            int n = a.length;
            if(n == 0) return new int[0];
            int[] c = new int[n];
            c[0] = 1;
            for (int i = 1; i < n; i++) {
                c[i] = c[i - 1] * a[i - 1];
            }
            int[] d = new int[n];
            d[n - 1] = 1;
            for (int i = n - 2; i >= 0 ; i++) {
                d[i] = d[i + 1] * a[i + 1];
            }
            // 好在我这没 new 一个结果集出来而是复用了 c
            for (int i = 0; i < n; i++) {
                c[i] *= d[i];
            }
            return c;
        }
    }
    
    /**
     * 路飞果然是比我聪明，他只用了一个辅助数组，我上来就搞两个！
     * 路飞少用了一个数组，内存没比我更省，应该是数字大小限制导致的，数字不会太大
     * */
    public static class Luffy {
        public int[] constructArr(int[] a) {
            int len = a.length;
            if(len == 0) return new int[0];
            int[] b = new int[len];
            b[0] = 1;
            int tmp = 1;
            for(int i = 1; i < len; i++) {
                b[i] = b[i - 1] * a[i - 1];
            }
            for(int i = len - 2; i >= 0; i--) {
                tmp *= a[i + 1];
                b[i] *= tmp;
            }
            return b;
        }
        
    }

}
