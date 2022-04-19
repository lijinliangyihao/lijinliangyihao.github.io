```java
Completion fields need not be declared as final or volatile because they are only visible to other threads upon safe publication
```

我瞧瞧是怎么回事？



这段没看太懂，r 可能是个异常？

```java
final boolean completeThrowable(Throwable x, Object r) {
    return UNSAFE.compareAndSwapObject(this, RESULT, null,
                                       encodeThrowable(x, r));
}
```



当前完成时，触发依赖项

f 指需要被执行的依赖；

当 stack 非空，或 f 变了不指向当前且 f 的 stack 非空，如果当前 stack 空了会重新指回来，f 和 h，干啥呢？

1.尝试替换 stack 为它的 next

2.如果成功，

2.1 如果有 next

​	2.1.1 如果 f 不是 this 了，把 h 压栈，再次循环，压栈意思是让 h 待处理

​        如果到下一次循环，f 不是 this 了，开始判断 f 的依赖看能不能跑一跑

​        如果依赖的依赖执行完了，怎么破？没提。

​		估计，他还会继续 postComplete，我猜测

​	如果 f 还是 this，那 f.next 置空

2.2 h，也就是当前栈顶的 completion，尝试以 NESTED 模式运行

​	如果返回 null，f 还是 this

​    如果不是 null，可能是还有依赖？之类的，f 设置成 h.tryFire 返回的那个对象了



tryFire 具体怎么回事，存疑



UniCompletion 的 claim 把我neng傻了，说是返回是否能 run

​	默认返回 false，如果设置成状态了，如果 executor 没了，返回 true，意思是可以跑；否则把 executor 搞没，然后用 e 跑自己。

提一嘴，因为不能放过任何一个知识点：

```
compareAndSetForkJoinTaskTag(short e, short tag)
```

注释说，如果 参数 (0, 1) 返回 true 说明被访问过了，跟图一样。访问过就不能再访问了。

​	原理很简单，它有个 0xffff 16 位 mask，把旧 tag 抹掉，换成现在的 tag

故，这个 claim 应该是尝试认领这个 completion，然后看能不能跑它，在池子里。



push(UniCompletion c)

如果 c 不是 null，当前没结果，没推进栈，那把 c 的 next 搞没。干蛋呢？为什么把我 next 搞没？

​	那它的 next 不就丢了吗？？？？

​	对了，他本来就打算把 next 设置成  stack，搞成 null，可能更容易成功一些。

​	不过它一开始为什么不是 null？不怕丢的那种？



postFire(CompletableFuture a, int mode) 这个方法 tryFire 完了之后执行

1.  如果 a 不是 null，a有依赖

   1.1 如果模式是 NESTED 或 a 没结果，尝试清除它的栈

   ​		a 没结果为啥清除栈，NESTED 可以理解

   ​			懂了，a 没结果，说明 a 还没完

   1.2 是同步或异步模式调用 a.postComplete，因为此时 a 执行完了

2. result 非空且栈非空

   是 NESTED，直接把自己返回

   不是 NESTED，调用自己的 postComplete

3. 直接返回 null，这个包含很多情况啊，操

   懂了，只有 NESTED 且自己有结果且自己还有待处理的情况时，返回自己，其它都返回 null



有些不太懂：这三种 mode 是怎么回事，同步、异步和嵌套



```
uniApplyStage(Executor e, Function f)
```

new 一个空的 completableFuture

1.  e 是空的且 d.uniApply(this, f, null) 返回 true 了！

   d 如果是空的，肯定返回 false ；我感觉，它无论如何都会进这个 if 因为永远都是 false

   1.1 进来以后

   new UniApply 推上去，d 是刚才那个空 cf

   推完了尝试同步触发。

   这我已经完全看不懂了



到这 UniApply 讲完了

后面又有 UniAccept UniXXX 几个



internalComplete 和 completeValue 都是设置 result，区别是，前者直接设置，后者，如果结果是 null，设置一个特殊对象



```
CompletableFuture<T> postFire(CompletableFuture<?> a,
                                    CompletableFuture<?> b, int mode)
```

处理 b 然后 postFire(a, mode) 为什么处理 b？？？？











