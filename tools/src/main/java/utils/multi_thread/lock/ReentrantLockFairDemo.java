package utils.multi_thread.lock;

import cn.hutool.core.thread.ThreadUtil;
import utils.tools.NumberUtil;
import utils.tools.random.RandomUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jimmy
 *
 */
public class ReentrantLockFairDemo {

	private static final ReentrantLock LOCK = new ReentrantLock();
	private static final ReentrantLock FAIR_LOCK = new ReentrantLock(true);

	/**
	 * 非公平锁
	 */
	public static class Task implements Runnable {
		@Override
		public void run() {
			LOCK.lock();
			try {
				System.out.print(Thread.currentThread().getName() + " ");
				TimeUnit.MILLISECONDS.sleep(RandomUtil.randomNumber(5));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				LOCK.unlock();
			}
		}
	}

	/**
	 * 公平锁
	 */
	static int count = 0;
	public static class FairTask implements Runnable {
		@Override
		public void run() {
			FAIR_LOCK.lock();
			try {
				count++;
				System.out.print(Thread.currentThread().getName() + " ");
				TimeUnit.MILLISECONDS.sleep(RandomUtil.randomNumber(5));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				FAIR_LOCK.unlock();
			}
		}
	}

	public static class TryFairTask implements Runnable {
		@Override
		public void run() {
			try {
				if(FAIR_LOCK.tryLock(10L, TimeUnit.SECONDS)){
					System.out.print(Thread.currentThread().getName() + " ");
					TimeUnit.MILLISECONDS.sleep(RandomUtil.randomNumber(5));
				}
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} finally {
				FAIR_LOCK.unlock();
			}
		}
	}


	public static void main(String[] args) throws InterruptedException {

		System.out.println("非公平锁结果：");

		List<Thread> threads = new ArrayList<>();
		for(int i = 0; i< 100; i++){
			threads.add(new Thread(new Task(), i+""));
		}
		ThreadUtil.sleep(1000);
		threads.forEach(t -> {
			ThreadUtil.sleep(1);
			t.start();
		});

		ThreadUtil.sleep(1000 * 5);
		System.out.println("");
		System.out.println("公平锁结果：");
		threads = new ArrayList<>();
		for(int i = 0; i< 100; i++){
			threads.add(new Thread(new FairTask(), i+""));
		}
		/**
		 * 多个线程的执行顺序是不确定的，这是由操作系统调度器所决定的。如果多个线程执行start()方法的时间非常接近，那么它们的启动顺序也可能是不确定的。
		 */
		//threads.forEach(Thread::start);
		threads.forEach(t -> {
				ThreadUtil.sleep(1);
				t.start();
		});
		ThreadUtil.sleep(1000 * 5);

		System.out.println("");
		System.out.println("=======count:" + count);

		System.out.println("TryLock()公平锁结果：");
		threads = new ArrayList<>();
		for(int i = 0; i< 100; i++){
			threads.add(new Thread(new TryFairTask(), i+""));
		}
		/**
		 * 多个线程的执行顺序是不确定的，这是由操作系统调度器所决定的。如果多个线程执行start()方法的时间非常接近，那么它们的启动顺序也可能是不确定的。
		 */
		//threads.forEach(Thread::start);
		threads.forEach(t -> {
			ThreadUtil.sleep(1);
			t.start();
		});
		ThreadUtil.sleep(1000 * 5);


	}
}
