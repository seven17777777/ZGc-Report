# 5、调优 
## 1、修改程序的ConcGCThreads
参数为
```
-XX:+UseZGC
-Xms2g
-Xmx4g
-Xlog:gc*:file=./gc.log:time,uptime
```
增加调整参数
```
-XX:ConcGCThreads=1
-XX:ConcGCThreads=2
-XX:ConcGCThreads=3
-XX:ConcGCThreads=4
```

## 2、修改程序的SoftMaxHeapSize

参数为
```
-XX:+UseZGC
-Xlog:gc*:file=./gc.log:time,uptime
```
增加调整参数(默认值为最大堆内存4G)
```
-XX:SoftMaxHeapSize=1g	
-XX:SoftMaxHeapSize=2g	
-XX:SoftMaxHeapSize=4g	
```


## 3、修改程序的InitiatingHeapOccupancyPercent
参数为
```
-XX:+UseZGC
-Xlog:gc*:file=./gc.log:time,uptime
```
堆内存使用达到指定百分比时开始垃圾收集的阈值
```
-XX:InitiatingHeapOccupancyPercent=25	
-XX:InitiatingHeapOccupancyPercent=50	
-XX:InitiatingHeapOccupancyPercent=75	
-XX:InitiatingHeapOccupancyPercent=100	
```