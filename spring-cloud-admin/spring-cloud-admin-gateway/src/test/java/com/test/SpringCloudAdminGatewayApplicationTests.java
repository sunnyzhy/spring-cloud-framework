package com.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.admin.gateway.SpringCloudAdminGatewayApplication;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * @author zhy
 * @date 2021/12/7 11:50
 */
@SpringBootTest(classes = SpringCloudAdminGatewayApplication.class)
public class SpringCloudAdminGatewayApplicationTests {
    @Autowired
    private ForkJoinPool forkJoinPool;

    @Test
    void task2(){
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello")
                .thenApply(s -> s + " World").thenApply(String::toUpperCase);

        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    void task1() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> task1 = CompletableFuture.supplyAsync(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("任务1开始执行" + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return 0;
        }, forkJoinPool);
        int i = task1.get();
        System.out.println("ok");
        while (true) {
            Thread.sleep(5000);
        }
    }

    @Test
    void task() {
        //任务一
        CompletableFuture<Integer> task1 = CompletableFuture.supplyAsync(() -> {
            int i = 5;
            System.out.println("任务1开始执行" + i);
            return i;
        }, forkJoinPool);

        //任务二
        CompletableFuture<Integer> task2 = CompletableFuture.supplyAsync(() -> {
            int i = 10;
            System.out.println("任务2开始执行" + i);
            return i;
        }, forkJoinPool);

        //要求：任务一、二都完成后才执行任务三
//      runAfterBothAsync：无传入值、无返回值
        task1.runAfterBothAsync(task2, () -> {
            System.out.println("任务3：无传入值、无返回值  ");
        }, forkJoinPool);

//      thenAcceptBothAsync：有传入值、无返回值
        task1.thenAcceptBothAsync(task2, (x, y) -> {
            System.out.println("任务4：无传入值、无返回值  task1的结果是x ,task2的结果是y");
        }, forkJoinPool);


//      thenCombineAsync：有传入值、有返回值
        task1.thenCombineAsync(task2, (x, y) -> {
            System.out.println("任务5开始执行-runAfterBothAsync：无传入值、无返回值  task1的结果是x ,task2的结果是y,task3返回hello");
            return "hello";
        }, forkJoinPool);

    }
}
