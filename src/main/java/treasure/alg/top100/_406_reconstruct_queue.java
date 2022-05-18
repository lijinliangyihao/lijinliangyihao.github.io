package treasure.alg.top100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 感觉上不难。。但是没想出来？
 *
 * 其实挺难的，我三言两语也说不清
 * 不如直接再去做一遍原题，日后。注意，是日后。
 *
 * */
public class _406_reconstruct_queue {
    
    public int[][] reconstructQueue(int[][] people) {
        Arrays.sort(people, (a, b) -> {
            int res = a[0] - b[0];
            return res == 0 ? b[1] - a[1] : res;
        });
        int n = people.length;
        int[][] res = new int[n][];
        for (int[] person : people) {
            int pos = person[1] + 1;
            for (int i = 0; i < n; i++) {
                if (res[i] == null) {
                    pos--;
                    if (pos == 0) {
                        res[i] = person;
                        break;
                    }
                }
            }
        }
        return res;
    }
    
    /**
     * 这个方法你看着简单其实里面有很多思考
     * 没那么简单！
     * */
    public int[][] reconstructQueue2(int[][] people) {
        Arrays.sort(people, (a, b) -> {
            int res = b[0] - a[0];
            return res == 0 ? a[1] - b[1] : res;
        });
        List<int[]> res = new ArrayList<>();
        for (int[] person : people) {
            res.add(person[1], person);
        }
        return res.toArray(new int[0][]);
    }
    
}
