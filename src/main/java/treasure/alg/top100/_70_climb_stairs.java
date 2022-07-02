package treasure.alg.top100;

public class _70_climb_stairs {
    
    /*
    * 纯混！
    * */
    public int climbStairs(int n) {
        int a = 1, b = 1;
        while (n-- > 0) {
            int t = a;
            a = b;
            b = a + t;
        }
        return a;
    }
}
