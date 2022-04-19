这应该是核心代码了：

```
TimSort<T> ts = new TimSort<>(a, c, work, workBase, workLen);

// 值在 (0, 32) 之间，目前不明原因？
int minRun = minRunLength(nRemaining);
do {
	// 这个方法，找到连续递增的最大长度
    int runLen = countRunAndMakeAscending(a, lo, hi, c);
    // If run is short, extend to min(minRun, nRemaining)
    if (runLen < minRun) {
    	// minRun 不到 32 个
    	// 如果剩余数量较大，那就不断地二分排序
    	// 如果剩的少，那就一把梭哈结束了
        int force = nRemaining <= minRun ? nRemaining : minRun;
        binarySort(a, lo, lo + force, lo + runLen, c);
        runLen = force;
    }
    // 感觉上是把这一段放到栈里（因为用了 push）等会归并
    // Push run onto pending-run stack, and maybe merge
    ts.pushRun(lo, runLen);
    ts.mergeCollapse();
    // Advance to find next run
    lo += runLen;
    nRemaining -= runLen;
} while (nRemaining != 0);
// Merge all remaining runs to complete sort
// 不懂
ts.mergeForceCollapse();
```

这是 minRunLength，有 java doc 说明它的行为，但是行为的解释在 listsort.txt！还得找。

```
private static int minRunLength(int n) {
    assert n >= 0;
    int r = 0;      // Becomes 1 if any 1 bits are shifted off
    while (n >= MIN_MERGE) {
        r |= (n & 1);
        n >>= 1;
    }
    return n + r;
}
```



gallopRight 是找 run2 第一个元素在 run1 中的位置，找到以后，run1 的 base 就往后挪那么多，因为 run1 自己是有序的；在 run1 中找 run2 是为了尽量少的挪动元素吧。

gallopLeft 是找 run1 最后一个元素在 run2 中的位置。这样找也是尽量少的挪动元素，如果 run1 最后一个元素在 run2 中是第一个元素，那，岂不是很好，直接拼接上就行了

比较 run1 剩余长度和 run2 剩余长度，应该还是尽量少挪动元素：如果 run1 长，肯定是 mergeLo 吧。

​	操，反了。等会，我先看看原因。



`gallopLeft`

```
不要先入为主！此时 hint 不一定是最后一个
如果 key 太大尝试右移
if (c.compare(key, a[base + hint]) > 0) {
    // Gallop right until a[base+hint+lastOfs] < key <= a[base+hint+ofs]
    // 记录最大偏移量 
    int maxOfs = len - hint;
    如果当前偏移量可用而且还是大，当前偏移量 2n+1 这么往上递增
    while (ofs < maxOfs && c.compare(key, a[base + hint + ofs]) > 0) {
        lastOfs = ofs;
        ofs = (ofs << 1) + 1;
        if (ofs <= 0)   // int overflow
            ofs = maxOfs;
    }
    但是不能增过了头
    if (ofs > maxOfs)
        ofs = maxOfs;
	调整 ofs
    // Make offsets relative to base
    lastOfs += hint;
    ofs += hint;
}
```

```
else { // key <= a[base + hint]
    // Gallop left until a[base+hint-ofs] < key <= a[base+hint-lastOfs]
    虽然 key 比 hint 的小，还是要往左挪，因为要找最左，最大偏移量 hint + 1 不能达到
    final int maxOfs = hint + 1;
    while (ofs < maxOfs && c.compare(key, a[base + hint - ofs]) <= 0) {
        lastOfs = ofs;
        ofs = (ofs << 1) + 1;
        if (ofs <= 0)   // int overflow
            ofs = maxOfs;
    }
    if (ofs > maxOfs)
        ofs = maxOfs;
	这个感觉是换了换地，还是让 lastOfs 在左，ofs 在右
    // Make offsets relative to base
    int tmp = lastOfs;
    lastOfs = hint - ofs;
    ofs = hint - tmp;
}
```



mergeLo 和 mergeHi 之前：

run1 第一个元素大于 run2 第一个元素，且 run1 最后一个元素大于所有  run2 元素

```
Merges two adjacent runs in place, in a stable fashion.  The first
element of the first run must be greater than the first element of the
second run (a[base1] > a[base2]), and the last element of the first run
(a[base1 + len1-1]) must be greater than all elements of the second run.
```



先把 run1 最后一个元素都考进去，如果 run1 用完了，那把 run2 所有元素都考进去；如果 run2 就一个元素，那把 run2 唯一那个元素放开头，再把 run1 所有元素拷进去



mergeHi 这个感觉比 mergeLow 难懂：

```
do {
    assert len1 > 0 && len2 > 1;
    先 gallopRight 找到 cursor2 位置，它右边都是 run1 的，拷贝到目的地，如果不是 0
    如果 cursor 在中间，意料之中
    如果 cursor 在最左，它出不了这个 if
    如果 cursor 在最右，它不进这个 if
    count1 = len1 - gallopRight(tmp[cursor2], a, base1, len1, len1 - 1, c);
    if (count1 != 0) {
        dest -= count1;
        cursor1 -= count1;
        len1 -= count1;
        System.arraycopy(a, cursor1 + 1, a, dest + 1, count1);
        if (len1 == 0)
            break outer;
    }
    拷贝完之后，把 cursor2 放到那；这为什么是 cursor2？如果 cursor2 小，先拷贝比它大的再拷贝它；如果 cursor2 大，上面 count1 == 0，也没问题。
    a[dest--] = tmp[cursor2--];
    if (--len2 == 1)
        break outer;
	
	交错着来？我怎么感觉上面应该 while 循环。。
	因为有先后次序，如果大小一样，优先 run2，所以 gallopLeft 找到第一个等于 cursor1 的位置，然后把 run2 中后面的元素，都拷贝进去
    count2 = len2 - gallopLeft(a[cursor1], tmp, tmpBase, len2, len2 - 1, c);
    if (count2 != 0) {
        dest -= count2;
        cursor2 -= count2;
        len2 -= count2;
        System.arraycopy(tmp, cursor2 + 1, a, dest + 1, count2);
        if (len2 <= 1)  // len2 == 1 || len2 == 0
            break outer;
    }
    a[dest--] = a[cursor1--];
    if (--len1 == 0)
        break outer;
        
    拷贝完不知道为什么，最小跳跃长度 -1
    minGallop--;
} while (count1 >= MIN_GALLOP | count2 >= MIN_GALLOP);
```









