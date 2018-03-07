package com.fsy.spider.forkjoin;

import java.util.concurrent.RecursiveTask;

/**
 * Created by fushiyong on 2018/3/7.
 */
public class SumTask extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 20;

    private int[] array;
    private int low;
    private int high;

    public SumTask(int[] array, int low, int high) {
        this.array = array;
        this.low = low;
        this.high = high;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        if (high - low + 1 <= THRESHOLD) {
            System.out.println(low + " - " + high + "  计算");
//            测试并行的个数，统计输出过程中的文字，看看有多少线程停止在这里就知道有多少并行计算
//            参考 ForkJoinPool 初始化设置的并行数
//            try {
//                Thread.sleep(11111111);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            // 小于阈值则直接计算
            for (int i = low; i <= high; i++) {
                sum += array[i];
            }
        } else {
            System.out.println(low + " - " + high + "  切分");
            // 1. 一个大任务分割成两个子任务
            int mid = (low + high) / 2;
            SumTask left = new SumTask(array, low, mid);
            SumTask right = new SumTask(array, mid + 1, high);

            // 2. 分别并行计算
            invokeAll(left, right);

            // 3. 合并结果
            sum = left.join() + right.join();

            // 另一种方式
            try {
                sum = left.get() + right.get();
            } catch (Throwable e) {
                System.out.println(e.getMessage());
            }
        }
        return sum;
    }
}