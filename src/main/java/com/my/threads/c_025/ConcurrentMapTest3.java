package com.my.threads.c_025;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;

/**
 * Created by sunjinwei on 2018/12/4.
 *
 * @author sunjinwei
 * <p>
 * 高并发下Map问题
 * @see http://blog.csdn.net/sunxianghuang/article/details/52221913
 * <p>
 * 仔细看下面的打印会出问题吗？
 * <p>
 * <p>
 * 如果不添加阻塞，打印时其实线程并没有完全启动完成，可能只启动了50个线程就开始打印了
 * <p>
 * 使用 跳表 保证高并发排序, 但是注意对于字符串排序比较问题
 * key--82069,value--2519
 * key--8207,value--1233
 * key--82070,value--7173
 *
 *
 */
public class ConcurrentMapTest3 {

    public static void main(String[] args) {
        //跳表结构 skiplist
        Map<String, String> map = new ConcurrentSkipListMap<>();

        //定义100个线程，每个线程随机存储 1000个 int数字
        Random random = new Random();
        Thread[] ths = new Thread[100];

        //定义100个门闩
        CountDownLatch latch = new CountDownLatch(ths.length);

        for (int i = 0; i < ths.length; i++) {
            ths[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    map.put("key--" + random.nextInt(100000), "value--" + random.nextInt(100000));
                }
                latch.countDown();
            });
        }

        //启动线程
        Arrays.asList(ths).forEach(tt -> tt.start());

        //门闩等待
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //打印map值
        Set<Integer> mapSize = new HashSet<>();
        map.forEach((k, v) -> {
            System.out.println(k + "," + v);
            mapSize.add(map.size());
        });

        System.out.println("map大小:" + mapSize.size()); //map大小:1
    }


}
