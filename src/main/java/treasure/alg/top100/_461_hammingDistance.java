package treasure.alg.top100;

public class _461_hammingDistance {
    
    public int hammingDistance(int x, int y) {
        return Integer.bitCount(x ^ y);
    }
}
