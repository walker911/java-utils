package com.walker.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author walker
 * @date 2018/12/5
 */
public class DateUtil {
    private static final ThreadLocal<DateFormat> df =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    /**
     * 定义一个全局的 SimpleDateFormat
     */
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 使用 ThreadFactoryBuilder 定义一个线程池
     */
    private static ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("demo-pool-%d").build();

    private static ExecutorService pool = new ThreadPoolExecutor(5, 200,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(1024), threadFactory,
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * 定义一个 CountDownLatch，保证所有子线程执行完之后再执行
     */
    private static CountDownLatch countDownLatch = new CountDownLatch(100);

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static void main(String[] args) {
        // JDK 8
        LocalDateTime dateTime = LocalDateTime.now();
        System.out.println(dateTime.format(formatter));

        Set<String> dates = Collections.synchronizedSet(new HashSet<>());
        for (int i = 0; i < 100; i++) {
            Calendar calendar = Calendar.getInstance();
            int finalI = i;

            pool.execute(() -> {
                calendar.add(Calendar.DATE, finalI);
                String dateString = df.get().format(calendar.getTime());
                System.out.printf("%d ---> %s\n", finalI, dateString);
                dates.add(dateString);
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(dates.size());
    }
}
