package treasure.jianzhioffer;

public class Offer16 {
    
    public static class SolutionRecursive {
        
        public double myPow(double x, int n) {
            if(x == 0) return 0;
            if(n == 0) return 1;
            if(n == 1) return x;
            boolean neg = n < 0;
        
            /*
             * 这块得强转一下，不然会溢出
             * 狗逼 n 可以取到 Integer.MIN_VALUE
             * */
            long n2 = neg ? -(long)n : n;
            double t = myPow(x * x, (int)(n2 / 2));
            if((n & 1) == 1) {
                return neg ? 1 / (x * t) : x * t;
            }
            return neg ? 1 / t : t;
        }
    }
    
    public static class SolutionIterative {
        
        public double myPow(double x, int n) {
            if(x == 0) return 0;
            if(n == 0) return 1;
            if(n == 1) return x;
            boolean neg = n < 0;
            long n2 = neg ? -(long)n : n;
            double res = 1;
            
            while (n2 > 0) {
                if((n2 & 1) == 1) {
                    res *= x;
                }
                x *= x;
                n2 >>= 1;
            }
            return neg ? 1 / res : res;
        }
    }

}
