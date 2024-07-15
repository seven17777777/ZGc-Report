package com.code.tryOne.jvmGc.ZGC;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class ZGCNoAllocateThread {
    // 用于控制程序是否应继续运行的标志
    private static volatile boolean running = true;

    public static void main(String[] args) {
        // 获取 MemoryMXBean 实例
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

        // 创建并启动一个线程，用于在20秒后停止主线程
        Thread timerThread = new Thread(() -> {
            try {
                Thread.sleep(20000); // 休眠20秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            running = false; // 设置标志位为false
            System.out.println("程序运行时间达到20秒，程序暂停。");
        });

        timerThread.start(); // 启动计时线程

        int k = 1;
        while (running) { // 当标志位为true时，继续运行
            for (int i = 0; i < 1000; i++) {
                // 分配1MB的数组
                byte[] array = new byte[1024 * 1024];
            }

            // 每次循环后重新获取堆内存使用情况
            MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
            // 获取已使用的堆内存大小
            long usedHeapMemorySize = heapMemoryUsage.getUsed();
            // 获取已分配的堆内存大小
            long committedHeapMemorySize = heapMemoryUsage.getCommitted();

            System.out.println("第" + k++ + "次分配内存。已使用的堆内存大小：" + usedHeapMemorySize / (1024 * 1024) + " MB。已分配的堆内存：" + committedHeapMemorySize / (1024 * 1024) + " MB");

            // 模拟内存使用情况
            try {
                // 暂停一段时间，模拟应用程序的其他工作
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
