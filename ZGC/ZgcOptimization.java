package com.code.tryOne.jvmGc.ZGC;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;

public class ZgcOptimization {
    public static void main(String[] args) {

        // 创建一个内存管理bean
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        // 创建一个列表来存储内存占用的数据
        List<byte[]> memoryHog = new ArrayList<>();
        int k = 1;
        int allAllocate = 10000;            //程序一共分配10000M
        int onceAllocate = 10;              //一次分配10M
        // 当k小于或等于allAllocate / oneAllocate时，进行循环
        while (k <= allAllocate / onceAllocate) {
            for (int i = 0; i < onceAllocate; i++) {
                // 分配1MB的数组
                byte[] array = new byte[1024 * 1024];
                // 将数组添加到列表中
                memoryHog.add(array);
            }

            // 获取堆内存使用情况
            MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
            // 获取已使用的堆内存大小
            long usedHeapMemorySize = heapMemoryUsage.getUsed();
            // 获取已分配的堆内存大小
            long committedHeapMemorySize = heapMemoryUsage.getCommitted();
            // 打印内存分配情况
            System.out.println("第" + k++ + "次分配内存。已使用的堆内存大小：" + usedHeapMemorySize / (1024 * 1024) + " MB。已分配的堆内存：" + committedHeapMemorySize / (1024 * 1024) + " MB");
            // 模拟内存使用情况
            if (memoryHog.size() > onceAllocate) {
                // 清除列表中的数据
                memoryHog.subList(0, memoryHog.size()).clear();
            }

            try {
                // 暂停一段时间，模拟应用程序的其他工作
                Thread.sleep(onceAllocate);
            } catch (InterruptedException e) {
                // 打印异常信息
                e.printStackTrace();
            }
        }
    }
}
