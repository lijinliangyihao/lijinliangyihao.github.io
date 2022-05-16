package treasure.alg.top100;

public class _215_kth_largest_number {
    
    /**
     * 辛辛苦苦写了半天错了，操他妈！
     *
     * */
    public static class quick_select {
    
        public int findKthLargest(int[] nums, int k) {
            int n = nums.length;
            int index = n - k;
            qs(nums, 0, nums.length - 1, index);
            return nums[index];
        }
    
        void qs(int[] nums, int start, int end, int k) {
            if (start >= end) return;
            int pivot = nums[start];
            int l = start, r = end;
            while (l < r) {
                while (l < r && nums[r] >= pivot) r--;
                nums[l] = nums[r];
                while (l < r && nums[l] <= pivot) l++;
                nums[r] = nums[l];
            }
            nums[l] = pivot;
        
            if (l == k) return;
            if (l < k)
                qs(nums, l + 1, end, k);
            else
                qs(nums, start, l - 1, k);
        }
    }
    
    // 答案爱用的一种很多次交换、但是效率没低多少的这么一种解法，我上次没搞出来
    // 这次可能也搞不出来！
    /**
     * 歪日！这次居然。。。搞出来了！虽然是抄着写的！
     * */
    public static class quick2 {
        public int findKthLargest(int[] nums, int k) {
            return quick(nums, 0, nums.length - 1, nums.length - k);
        }
        
        int quick(int[] nums, int start, int end, int index) {
            int i = partition(nums, start, end);
            if (i == index)
                return nums[i];
            return i > index ? quick(nums, start, i - 1, index) : quick(nums, i + 1, end, index);
        }
        
        int partition(int[] nums, int start, int end) {
            int l = start - 1, x = nums[end];
            for (int i = start; i < end; i++) {
                if (nums[i] <= x)
                    swap(nums, ++l, i);
            }
            swap(nums, end, ++l);
            return l;
        }
        
        void swap(int[] nums, int i, int j) {
            if (i != j) {
                int t = nums[i];
                nums[i] = nums[j];
                nums[j] = t;
            }
        }
    }
    
    /**
     * 相当于又复习了一遍堆排序，还是不太熟！
     * */
    public static class use_heap {
    
        public int findKthLargest(int[] nums, int k) {
            build(nums);
            for (int i = nums.length - 1, end = nums.length - k + 1; i >= end ; i--) {
                swap(nums, i, 0);
                down(nums, 0, i);
            }
            return nums[0];
        }
        
        void build(int[] nums) {
            for (int i = nums.length / 2; i >= 0 ; i--) {
                down(nums, i, nums.length);
            }
        }
        
        // 这个操作叫下滤，每次把一个更小的元素往下移动
        void down(int[] nums, int i, int size) {
            int child = 2 * i + 1;
            // 大于，还是小于？大顶堆，肯定是要求，父和子中更大的那个比较！
            if (child + 1 < size && nums[child + 1] > nums[child])
                child++;
            if (nums[i] < nums[child]) {
                swap(nums, child, i);
                down(nums, child, size);
            }
        }
        void down2(int[] nums, int i, int size) {
            int t = nums[i], child = 2 * i + 1;
            while (child < size) {
                if (child + 1 < size && nums[child + 1] > nums[child])
                    child++;
                if (t < nums[child]) {
                    nums[i] = nums[child];
                    i = child;
                    child = 2 * child + 1;
                } else
                    break;
            }
            nums[i] = t;
        }
        
        void swap(int[] nums, int i, int j) {
            int t = nums[i];
            nums[i] = nums[j];
            nums[j] = t;
        }
    }
}
