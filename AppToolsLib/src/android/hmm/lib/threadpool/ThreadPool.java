package android.hmm.lib.threadpool;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author heming
 * @since :JDK ?  
 * @version：1.0
 * Create at:2014年5月7日
 * Description:  
 */
public class ThreadPool {

	/**
	 * Java通过Executors提供四种线程池，分别为：
		newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
		
		newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
		
		newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
		
		newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，
		保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
	 */
	
	private void newCachedThreadPool() {
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		
//		/**
//		 * 如果第一个任务耗费非常长的时间来执行，然后其他的任务都早于它结束，那么当前线程就无法在第一个任务结束之前获得执行结果
//		 */
//		CompletionService<String> pool = new ExecutorCompletionService<String>(cachedThreadPool);
//		for(int i = 0; i < 10; i++){
//		   pool.submit(new StringTask());
//		}
//		 
//		for(int i = 0; i < 10; i++){
//		   String result = pool.take().get();
//		   //Compute the result
//		}
		
		
		cachedThreadPool.shutdown();
		for (int i = 0; i < 10; i++) {
			final int index = i;
			try {
				Thread.sleep(index * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			cachedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println(index);
				}
			});
		}
		
		
		
//		Thread d;
//		d.yield();
//		d.interrupt();
	}
}
