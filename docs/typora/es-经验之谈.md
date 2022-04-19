#### 场景

疯狂往 es 打日志，需要考虑写优化

|      |      |      |      |      |      |
| ---- | ---- | ---- | ---- | ---- | ---- |
|      |      |      |      |      |      |
|      |      |      |      |      |      |
|      |      |      |      |      |      |
|      |      |      |      |      |      |
|      |      |      |      |      |      |

要把 mysql 数据同步到 es，要考虑读优化



#### 写优化

某场景下，百T数据，每秒写10w+ 数据。

考虑调整 es 的 `flush、refresh 和 merge`，在性能和数据可靠性之间进行权衡。

##### translog 异步化

es 有个事务日志，类似 mysql redo log，默认一秒一刷，改成异步的， 60 s一刷：

```curl
curl -H"Content-Type: application/json" -XPUT 'http://localhost:9200/_all/_settings\
?preserve_existing=true' \
-d '{
    "index.translog.durability" : "async",
    "index.translog.flush_threshold_size" : "512mb",
    "index.translog.sync_interval" : "60s"
}'
```

可能丢数，看你了。



##### 增加 refresh 间隔

es 写完 translog，还要写一个缓冲，写完缓冲还得刷（refresh）到 segment 才能被查到，这个间隔是一秒。

属性 `index.refresh_interval` 负责，改它为 120s：

```
curl -H "Content-Type: application/json" -XPUT 'http://localhost:9200/_all/_settings\
?preserve_existing=true' \
-d '{
  "index.refresh_interval" : "120s"
}'
```

这样能看到两分钟前的日志，看你了！

##### merge

进 segment 后，有个线程在不停地 merge，节约空间，但是很费 I/O 和 cpu，可以把间隔调大。

***没说怎么改参数，默认值是多少也不知道***



#### 读优化

默认生成随机 id，查询时每个分片都查。可以人工指定个路由，让它直接路由过去：

```
shard = hash(routing) % number_of_primary_shards

// 查询时
GET my-index-000001/_search
{
  "query": {
    "terms": {
      "_routing": [ "user1" ] 
    }
  }
}
```

##### rollover冷热分离

提了一嘴，不知道咋搞，说是类似 logback 的滚动日志，旧数据新数据分开。

滚动索引一般可以与索引模板结合使用，实现按一定条件自动创建索引，ES的官方文档有具体的_rollover建立方法



##### 太复杂的数据分开存

在代码里拼接数据



##### 增加第一次索引的速度

为了缓解这种情况，建议在创建索引的时候，把副本数量设置成1，即没有从副本。等所有数据索引完毕，再将副本数量增加到正常水平。

这样，数据能够快速索引，副本会在后台慢慢复制。



#### 线程池

用监控接口或 trace 工具如果发现线程池有瓶颈，则或许需要调整。

不需要配置复杂的 `search、bulk、index` 线程池

根据接口：`_cat/thread_pool` 观看

- thread_pool.get.size

- thread_pool.write.size

-  thread_pool.listener.size

-  thread_pool.analyze.size

  

#### 磁盘冷热分离

在配置 `elasticsearch.yml`，给节点打标签

```
//热节点
node.attr.temperature: hot 
//冷节点
node.attr.temperature: cold 
```

***ES提供了index shard filtering功能来实现索引的迁移***

​	？？？

可以对索引也设置冷热属性

```
PUT hot_data_index/_settings
{
    "index.routing.allocation.require.temperature": "hot"
}
```



***我们可以写一些定时任务，通过_cat接口的数据，自动的完成这个转移过程***

​	怎么写？



#### 多磁盘分散I/O

ES支持在一台机器上配置多块磁盘，所以在存储规模上有更大的伸缩性。在配置文件中，配置path.data属性，即可挂载多块磁盘

```
path.data : /data1, /data2, /data3
```

如果你是在扩容，那么就需要配合reroute接口进行索引的重新分配



#### 减少单条记录的大小

Lucene的索引建立过程，非常耗费CPU，可以减少倒排索引的数量来减少CPU的损耗。

第一个优化就是减少字段的数量；

第二个优化就是减少索引字段的数量

将不需要搜索的字段，index 属性设置为 not_analyzed 或者 no