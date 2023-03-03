package code.multi_thread.wait_notify;

/**
 * @author Jimmy
 * @date 2020/11/26 0026
 */
public class Consumer implements Runnable {
	private QueueBuffer q;

	public Consumer(QueueBuffer q) {
		this.q = q;
		new Thread(this, "Consumer").start();
	}

	@Override
	public void run() {
		while (true) {
			q.get();
			System.out.println();
		}
	}
}
