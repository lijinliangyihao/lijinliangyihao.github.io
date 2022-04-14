package treasure.alg.jianzhioffer;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * 蒙混过关我透？
 *
 * */
public class Offer40 {
    
    /**
     * 这个思路很符合直觉但是很慢，打败了 19.91% 的人
     * */
    public static class ByPriorityQueue {
        
        public int[] getLeastNumbers(int[] arr, int k) {
            // 老阴逼，k 能取到 0
            if(k == 0) return new int[0];
            int[] res = new int[k];
            PriorityQueue<Integer> q = new PriorityQueue<>((a, b) -> b - a);
            for (int i = 0; i < arr.length; i++) {
                if(q.size() < k) {
                    q.offer(arr[i]);
                }
                else {
                    if(q.peek() > arr[i]) {
                        q.poll();
                        q.offer(arr[i]);
                    }
                }
            }
            for (int i = 0; i < k; i++) {
                res[i] = q.poll();
            }
            return res;
        }
    }
    
    public static class MyQuickSelection {
        /*
         * 我决定来一手快速选择排序，比优先队列要快不少
         * */
        public int[] getLeastNumbers(int[] arr, int k) {
            partition(arr, 0, arr.length - 1, k);
            return Arrays.copyOf(arr, k);
        }
    
        void partition(int[] arr, int left, int right, int k) {
            if(left == right) return;
            int pivot = arr[left];
            int l = left;
            int r = right;
            while(l < r) {
                while(l < r && arr[r] >= pivot) r--;
                arr[l] = arr[r];
                while(l < r && arr[l] <= pivot) l++;
                arr[r] = arr[l];
            }
            // 这个很关键，最后一个位置 pivot 放回去
            arr[l] = pivot;
            // 此时 l 左侧都比它小右侧都比它大
            if(l == k) return;
            if(l < k) {
                partition(arr, l + 1, right, k);
            } else {
                partition(arr, left, l - 1, k);
            }
        }
    }
    
    /**
     * 他怎么分这么多函数，我吐了！
     *
     * 这么长我都不想写一遍了，下次一定
     * */
    public static class OthersQuickSelection {
        
        public int[] getLeastNumbers(int[] arr, int k) {
            randomizedSelected(arr, 0, arr.length - 1, k);
            int[] vec = new int[k];
            for (int i = 0; i < k; ++i) {
                vec[i] = arr[i];
            }
            return vec;
        }
    
        private void randomizedSelected(int[] arr, int l, int r, int k) {
            if (l >= r) {
                return;
            }
            int pos = randomizedPartition(arr, l, r);
            int num = pos - l + 1;
            if (k == num) {
                return;
            } else if (k < num) {
                randomizedSelected(arr, l, pos - 1, k);
            } else {
                randomizedSelected(arr, pos + 1, r, k - num);
            }
        }
    
        // 基于随机的划分
        private int randomizedPartition(int[] nums, int l, int r) {
            int i = new Random().nextInt(r - l + 1) + l;
            swap(nums, r, i);
            return partition(nums, l, r);
        }
    
        private int partition(int[] nums, int l, int r) {
            int pivot = nums[r];
            // i 是小于 pivot 的下标，从 -1 开始
            int i = l - 1;
            // 最多不能超过 r - 1 因为 r 是 pivot
            for (int j = l; j <= r - 1; ++j) {
                // j 这个位置的元素 <= pivot，和 ++i 交换位置
                if (nums[j] <= pivot) {
                    i = i + 1;
                    swap(nums, i, j);
                }
            }
            // i 是最后一个符合 <= pivot 的位置所以得 +1 放 pivot
            swap(nums, i + 1, r);
            return i + 1;
        }
    
        private void swap(int[] nums, int i, int j) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
    
    }
    
}
