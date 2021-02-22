package com.ymj.locktest;

/**
 * @author Yemingjie
 * @version 1.0
 * @date 2021/2/22 17:13
 *
 *  自旋锁
 *      是指尝试获取锁的线程不会立即阻塞，而是采用循环的方式去尝试获取锁，这样的好处是减少线程上下文切换的消耗，缺点是循环会消耗CPU
 */
public class SpinlockDemo {
}
