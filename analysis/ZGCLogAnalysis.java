package com.code.tryOne.jvmGc.analysis;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZGCLogAnalysis {
    public static void analysis(String filePath) {
        File file = new File(filePath);
        int gcCount = 0; //暂停次数
        double totalPauseTime = 0;
        double maxPauseTime = 0;
        double lastTime = 0;
        Pattern gcPattern = Pattern.compile("\\[gc,phases   \\] GC\\((\\d+)\\)");
        Pattern pausePattern = Pattern.compile("\\[(.*?)s\\].*?Pause (Mark Start|Mark End|Relocate Start) (.*?)ms");
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                Matcher gcMatcher = gcPattern.matcher(line);
                if (gcMatcher.find()) {
                    gcCount++;
                }
                Matcher pauseMatcher = pausePattern.matcher(line);
                if (pauseMatcher.find()) {
                    String[] pauseTimes = pauseMatcher.group(3).split("/");
                    for (String pauseTimeStr : pauseTimes) {
                        String[] pauseTimeParts = pauseTimeStr.trim().split("\\s+");
                        for (String pauseTimePart : pauseTimeParts) {
                            double pauseTime = Double.parseDouble(pauseTimePart);
                            totalPauseTime += pauseTime;
                            maxPauseTime = Math.max(maxPauseTime, pauseTime);
                        }
                    }
                    lastTime = Double.parseDouble(pauseMatcher.group(1).split("\\]")[1].replace("[", ""));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("无法找到文件: " + filePath);
        }

        gcCount = gcCount / 9;
        double avgPauseTime = totalPauseTime / gcCount / 3;
        double throughput = 100 * (1 - totalPauseTime / lastTime / 1000);

        String fileName = new File(filePath).getName();
        System.out.printf("文件名称：%25s |文件运行总时间：%.3fms| GC事件数量: %5d | 平均暂停时间: %.6fms | 最大暂停时间: %.3fms | 吞吐量: %.2f%% | 总暂停时间: %.3fms%n",
                fileName, lastTime, gcCount, avgPauseTime, maxPauseTime, throughput, totalPauseTime);

    }

    public static void main(String[] args) {
        String directoryPath = "./"; // 指定目录路径
        File directory = new File(directoryPath);
        File[] logFiles = directory.listFiles((dir, name) -> name.endsWith(".log"));

        for (File logFile : logFiles) {
            analysis(logFile.getAbsolutePath());
        }
    }
}