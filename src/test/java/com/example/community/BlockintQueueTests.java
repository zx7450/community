package com.example.community;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 * @author zx
 * @date 2022/6/6 22:57
 */

public class BlockintQueueTests {
    public static void main(String[] args) {
        //阻塞队列在队空时阻塞消费线程，队满时阻塞生产进程
        BlockingQueue queue = new ArrayBlockingQueue(10);//默认数组长度为10，最多只能存10个数
        //一个生产者三个消费者
        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
    }
}

class Producer implements Runnable {

    private BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                Thread.sleep(20);//每20秒生产一个数据
                queue.put(i);
                System.out.println(Thread.currentThread().getName() + "生产:" + queue.size());//谁生产了数据，生产之后数据量
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {
    private BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {//只要有数据就一直消费
                Thread.sleep(new Random().nextInt(1000));//消费者速度没有生产者快
                queue.take();
                System.out.println(Thread.currentThread().getName() + "消费:" + queue.size());//谁消费了数据，消费之后数据量
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
