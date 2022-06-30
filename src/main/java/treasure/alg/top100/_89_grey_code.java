package treasure.alg.top100;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是……数学题？
 * */
public class _89_grey_code {
    
    /**
     * 假设有一个序列 n-1 已经是一个格雷码了
     * 把它翻过来，成为另一个序列
     * 然后这第二个序列的头和第一个序列的尾、它的尾和第一个的头，是一样的
     * 然后这第二个序列，每个值都加上 2^(n-1)，然后和第一个拼上，就构成新的格雷码了
     * */
    public List<Integer> grayCode(int n) {
        List<Integer> res = new ArrayList<>();
        res.add(0);
        // i 是迭代几轮
        for (int i = 1; i <= n; i++) {
            // 每轮，是之前的长度，remember？
            int m = res.size();
            for (int j = m - 1; j >= 0 ; j--) {
                res.add(res.get(j) | 1 << (i - 1));
            }
        }
        return res;
    }
    
    // g(i) = i ^ (i >> 1)
    public List<Integer> grayCode2(int n) {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < 1 << n; i++) {
            res.add(i ^ (i >> 1));
        }
        return res;
    }
}
