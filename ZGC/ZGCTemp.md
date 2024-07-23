其中不论在那种Xmx下都是25%和75%总暂停时间最短，所以设计一共更小的粒度进行，每百分之一进行一次日志记录
脚本如下 //todo

```shell
@echo off
setlocal enabledelayedexpansion

for /L %%i in (1,1,10) do (
    set "logfile=gc_%%i.log"
    java -XX:+UseZGC -Xmx128m -XX:InitiatingHeapOccupancyPercent=%%i -Xlog:gc*:file=./!logfile!:time,uptime,tags GC/ZGC/ZgcOptimization
)

```
