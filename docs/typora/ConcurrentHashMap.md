```
final int batchFor(long b) {
    long n;
    if (b == Long.MAX_VALUE || (n = sumCount()) <= 1L || n < b)
        return 0;
    int sp = ForkJoinPool.getCommonPoolParallelism() << 2; // slack of 4
    return (b <= 0L || (n /= b) >= sp) ? sp : (int)n;
}
```

对于并行度 b 每个批次多大？用于控制如何 split。

并行度太高，返回 0 意思是不 split。

然后最大每个批次，并行度 * 4

如果并行度很小，或每批数量大于 sp，则上限 sp；否则，如果每批次小于 sp，那就设置成这个小的数。

