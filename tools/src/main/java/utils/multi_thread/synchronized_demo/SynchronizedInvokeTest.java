package code.multi_thread.synchronized_demo;

import java.util.concurrent.CountDownLatch;

/**
 * @author lwq
 * @date 2022/6/17 0017
 * @since
 */
public class SynchronizedInvokeTest {

	/**
	 * synchronized不同情况锁效果演示
	 */
	public static class Client {

		/**
		 * 不同对象访问类锁（静态方法锁效果一样）测试
		 */
		private static void testClass() {
			final CountDownLatch c = new CountDownLatch(1);
			new Thread(() -> {
				SynchronizedTest t1 = new SynchronizedTest();
				try {
					System.out.println(Thread.currentThread().getName() + "启动");
					c.await();
					// t1.testClass3();
					t1.testClass1();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			).start();
			new Thread(() -> {
				SynchronizedTest t1 = new SynchronizedTest();
				try {
					System.out.println(Thread.currentThread().getName() + "启动");
					c.await();
					// t1.testClass2();
					t1.testClass2();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
			c.countDown();

			/* 结果：可以发现 静态方法的synchronized（和类锁效果一样），一个线程获得不释放，其他线程就不能访问 通过日志可以看到Thread1 一直在等待Thread0
			Thread-0启动
			Thread-1启动
			Thread-0---testClass3 Doing
			Thread-0---testClass2 Doing
			Thread-0---testClass3 Doing
			Thread-0---testClass2 Doing
			Thread-0---testClass3 Doing
			Thread-0---testClass2 Doing
			*/
		}

		/**
		 * 直接通过类静态方法访问静态方法锁测试(同{@link #testClass()}效果一样)
		 */
		private static void testClass2() {
			final CountDownLatch c = new CountDownLatch(1);
			new Thread(() -> {
				try {
					System.out.println(Thread.currentThread().getName() + "启动");
					c.await();
					SynchronizedTest.testClass2();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			).start();
			new Thread(() -> {
				try {
					System.out.println(Thread.currentThread().getName() + "启动");
					c.await();
					SynchronizedTest.testClass3();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
			c.countDown();

			/* 结果：可以发现 静态方法的synchronized（和类锁效果一样），一个线程获得不释放，其他线程就不能访问 通过日志可以看到Thread1 一直在等待Thread0
			Thread-0启动
			Thread-1启动
			Thread-0---testClass3 Doing
			Thread-0---testClass2 Doing
			Thread-0---testClass3 Doing
			Thread-0---testClass2 Doing
			Thread-0---testClass3 Doing
			Thread-0---testClass2 Doing
			*/
		}

		/**
		 * 不同对象访问对象锁测试
		 */
		public void test() {
			final CountDownLatch c = new CountDownLatch(1);
			new Thread(() -> {
				SynchronizedTest test = new SynchronizedTest();
				System.out.println(Thread.currentThread().getName() + "启动");
				try {
					c.await();
					test.test2();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}).start();
			new Thread(() -> {
				System.out.println(Thread.currentThread().getName() + "启动");
				SynchronizedTest test = new SynchronizedTest();
				try {
					c.await();
					test.test1();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
			c.countDown();

			/* 结果：完全没问题。因为是不同对象持有自己的锁
			Thread-1启动
			Thread-0启动
			Thread-0--- test1 Doing
			Thread-1---test2 Doing
			Thread-0---test2 Doing
			Thread-1--- test1 Doing
			Thread-0--- test1 Doing
			*/
		}

		/**
		 * 同一个对象访问对象锁测试
		 */
		public void test2() {
			final CountDownLatch c = new CountDownLatch(1);
			SynchronizedTest test = new SynchronizedTest();
			new Thread(() -> {
				System.out.println(Thread.currentThread().getName() + "启动");
				try {
					c.await();
					test.test2();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}).start();
			new Thread(() -> {
				System.out.println(Thread.currentThread().getName() + "启动");
				try {
					c.await();
					test.test1();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
			c.countDown();

			/* 结果：同一个对象，两个线程访问对象锁，会有一个先拿到锁，另一个需要等待直到锁释放，如果一直不释放就会一直阻塞
			Thread-1启动
			Thread-1--- test1 Doing
			Thread-0启动
			Thread-1---test2 Doing
			Thread-1--- test1 Doing
			*/
		}


		public static void main(String[] args) {
			// testClass();
			// testClass2();

			Client client = new Client();
			// client.test();
			client.test2();
		}
	}

	/**
	 * 死锁演示
	 */
	public static class Client2 {

		/**
		 * 类锁下的死锁演示
		 */
		public static void test() {
			CountDownLatch c = new CountDownLatch(1);
			new Thread(() -> {
				try {
					System.out.println(Thread.currentThread().getName() + "启动");
					c.await();
					SynchronizedTest.E.test();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
			new Thread(() -> {
				try {
					System.out.println(Thread.currentThread().getName() + "启动");
					c.await();
					SynchronizedTest.E1.test();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
			c.countDown();

			/* 结果：E1.test调用E.test 和 E.test调用E1.test出现了相互等待，造成了死锁
			Thread-0启动
			Thread-0---E.test Doing
			Thread-1启动
			Thread-1---E1.test Doing
			Thread-1---E1.test ready to do E.test
			Thread-0---E.test ready to do E1.test
			*/
		}

		/**
		 * 对象锁下的死锁演示
		 */
		public static void test2() {
			CountDownLatch c = new CountDownLatch(1);
			SynchronizedTest test = new SynchronizedTest();
			String s = "hello";
			Object obj = new Object();
			new Thread(() -> {
				try {
					System.out.println(Thread.currentThread().getName() + "启动");
					c.await();
					test.test1(s, obj);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
			new Thread(() -> {
				try {
					System.out.println(Thread.currentThread().getName() + "启动");
					c.await();
					test.test2(s, obj);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
			c.countDown();

			/* 结果：Thread-0持有s锁，需要锁obj;同时Thread-1持有obj锁，需要锁s；都在等对方对方的锁释放，造成死锁
			Thread-0启动
			Thread-0---test1 Doing
			Thread-1启动
			Thread-1---test2 Doing
			Thread-1---test2 obj locked, ready to lock s
			Thread-0---test1 s locked, ready to lock obj
			*/
		}

		public static void main(String[] args) {
			// test();
			test2();
		}
	}
}
