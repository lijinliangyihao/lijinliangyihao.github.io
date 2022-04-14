package treasure.alg.jianzhioffer;

/**
 * 在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。
 * 输入一个数组，求出这个数组中的逆序对的总数
 *
 * 其实就是考你归并排序，不会做！
 *
 * */
public class Offer51 {
    
    public static class Lufei {
        // 7,5,6,4
        // 这个我肯定是写不出来的，因为我不会写归并排序
        public int reversePairs(int[] nums) {
            tmp = new int[nums.length];
            return mergeSort(0, nums.length - 1, nums);
        }
        int[] tmp;
        int mergeSort(int l, int r, int[] nums) {
            if(l >= r) return 0;
            int mid = (l + r) / 2;
            // 这一步是关键，把两个子数组已经合并好了，递归调用
            int res = mergeSort(l, mid, nums) + mergeSort(mid + 1, r, nums);
        
            // 这是真正合并过程
            int i = l, j = mid + 1;
            // 数据先拷贝进临时数组
            for (int k = l; k <= r; k++) {
                tmp[k] = nums[k];
            }
            for (int k = l; k <= r; k++) {
                // i 用完了，j 过去？
                if (i == mid + 1)
                    nums[k] = tmp[j++];
                    // j 用完了或 i 比较小，i 拷过去
                else if (j == r + 1 || tmp[i] <= tmp[j])
                    nums[k] = tmp[i++];
                    // j 没用完，i 比较大，j 要拷过去，同时 res 得累加
                else {
                    nums[k] = tmp[j++];
                    // 因为它连个子数组是有序的，所以如果 i 比较大，后面的都比较大
                    // 所以 i 开始的个数都要累加到 res 里
                    // 他不是先看 i，然后找到比 i 大的 j，再累加
                    // 难道是那样搞不太好搞？
                    // 我的时间不多了，晚上十点半见！
                    res += mid - i + 1;
                }
            }
        
            return res;
        }
    }
    
    /**
     * 这是我的一份拷贝，抄错了好几处
     * */
    public static class MyCopy {
        public int reversePairs(int[] nums) {
            tmp = new int[nums.length];
            return merge(0, nums.length - 1, nums);
        }
        int[] tmp;
        int merge(int l, int r, int[] nums) {
            if(l >= r) return 0;
            int m = (l + r) / 2;
            int res = merge(l, m, nums) + merge(m + 1, r, nums);
            int i = l, j = m + 1;
            for (int k = l; k <= r; k++) {
                tmp[k] = nums[k];
            }
            for (int k = l; k <= r; k++) {
                if(i == m + 1) nums[k] = tmp[j++];
                else if(j == r + 1 || tmp[i] <= tmp[j]) nums[k] = tmp[i++];
                else {
                    nums[k] = tmp[j++];
                    res += m - i + 1;
                }
            }
            return res;
        }
    }
}
