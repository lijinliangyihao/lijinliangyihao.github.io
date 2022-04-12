package treasure.jianzhioffer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 0,1,···,n-1这n个数字排成一个圆圈，从数字0开始，
 * 每次从这个圆圈里删除第m个数字（删除后从下一个数字开始计数）。
 * 求出这个圆圈里剩下的最后一个数字。
 *
 * 例如，0、1、2、3、4这5个数字组成一个圆圈，从数字0开始每次删除第3个数字，
 * 则删除的前4个数字依次是2、0、4、1，因此最后剩下的数字是3。
 *
 * 这个感觉没那么简单的
 * */
public class Offer62 {
    
    /**
     * 它是打算让用 dp 来解决，但是这个 dp 我感觉非常的不太好理解
     *
     * 假设第一轮，最后结果，记为 f(n, m)
     * 则第二轮，结果就是，f(n - 1, m)
     * 第一轮，去掉了 (m - 1)%n 这个元素，这个值得理解，不然后面没法弄了
     * 然后下一个值，从这个元素下一位开始，也就是 t = m % n;
     *      有 n - 1 个数，故剩余的序列为 t t+1 t+2 ... 0 1 ... t-2，t-1 被删了刚才
     * 然后看看剩下的数字的关系，本来 n 个数，现在少了一个，还剩 n - 1 个
     *
     *          下一轮下标      当前删除 t 后的下标
     *           0             t + 0
     *           1             t + 1
     *           ...
     *           n - 2         t - 1
     *
     *           这个是什么意思呢，意思是不管你 f(n-1, m) 的解是啥，
     *           总是能通过这个下标 + t) % n 求出来上一轮的下标
     *
     *          也就是得到了一个递推公式
     *              f(n, m) = (f(n-1, m) + m%n)%n
     *                      = (f(n-1, m) + m) % n
     *          f(1, m) = 0 因为只有一个元素的话，直接就停了
     *          这样就能求出来 f(n, m) 了
     * */
    public static class Dp {
        public int lastRemaining(int n, int m) {
            int x = 0;
            for(int i = 2; i <= n; i++)
                x = (x + m) % i;
            return x;
        }
    }
    
    /**
     * 我自己想的办法，能跑但是会超时
     * 结果起码是对的
     * */
    public static class CanRunButTimeout {
        public int lastRemaining(int n, int m) {
            List<Integer> l = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                l.add(i);
            }
            int size = n;
            int i = 0;
            while(size > 1) {
                int remaining = m;
                while(remaining > 0) {
                    if(l.get(i) != null) {
                        if(remaining == 1) {
                            l.set(i, null);
                        }
                        remaining--;
                    }
                    i++;
                    if(i == l.size()) i = 0;
                }
                size--;
            }
            int res = -1;
            for (i = 0; i < n; i++) {
                if(l.get(i) != null) {
                    res = l.get(i);
                }
            }
            return res;
        }
    }
    
    /**
     * 这次我用链表试试
     *
     * 试了一下，链表也超时，真是个畜生啊！
     * */
    public static class ALittleFaster {
        public int lastRemaining(int n, int m) {
            LinkedList<Integer> l = new LinkedList<>();
            for (int i = 0; i < n; i++) {
                l.add(i);
            }
            int size = n;
            
            // 因为得从下一个位置开始，所以得在最外面声明一个 it
            Iterator<Integer> it = l.iterator();
            while(size > 1) {
                int remaining = m;
                while(remaining > 0) {
                    while(it.hasNext()) {
                        it.next();
                        if(remaining == 1) {
                            it.remove();
                            remaining--;
                            break;
                        }
                        remaining--;
                    }
                    // 退出且还有富余，说明 it 到结尾了，得重开一个 it
                    if(remaining > 0) {
                        it = l.iterator();
                    }
                }
                size--;
            }
            return l.get(0);
        }
    }
    
    /**
     * 我怀疑我超时是因为一直遍历，直接移除、重新分配内存可能反而不会超时，我们试试！
     *
     * 我日他妈，过辣！
     * 这次的代码更简单，更短小，但是更迅速！
     * */
    public static class ALittleMoreFaster {
        public int lastRemaining(int n, int m) {
            List<Integer> l = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                l.add(i);
            }
            int size = n;
            int i = (m - 1) % size;
            while(size > 1) {
                l.remove(i);
                i = (i + m - 1) % --size;
            }
            return l.get(0);
        }
    }
    
}
