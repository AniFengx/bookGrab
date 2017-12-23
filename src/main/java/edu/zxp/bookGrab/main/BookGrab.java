package edu.zxp.bookGrab.main;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import edu.zxp.bookGrab.entity.Book;
import edu.zxp.bookGrab.entity.LoadPageThread;
import edu.zxp.bookGrab.entity.SortBooksThread;
import edu.zxp.bookGrab.utils.Util;

/**
* 主类
* @author Anifengx
*
*/
public class BookGrab 
{	
	
	/**  加载进程关闭标志     */
	public static boolean loadFlag = true;
	
	/**  排序进程关闭标志     */
	public static boolean sortFlag = true;
	
	/**  运行时间     */
	public static long beg ;
	public static long end ;
	
	/**  书缓存队列     */
	private static BlockingQueue<Book> bookList = new LinkedBlockingQueue<Book>();
		
	/**  线程池     */
	private static ExecutorService executorService = Executors.newFixedThreadPool(5);  
	
	
	public static void main(String[] args)  {
		beg = System.currentTimeMillis();  //计算时间
		Util.getProxyList();
		//开启五个页面加载线程
		executorService.execute(new LoadPageThread(Util.DOUBAN_URL+String.valueOf(Util.page),executorService,bookList));
		executorService.execute(new LoadPageThread(Util.DOUBAN_URL+String.valueOf(Util.page.addAndGet(20)),executorService,bookList));
		executorService.execute(new LoadPageThread(Util.DOUBAN_URL+String.valueOf(Util.page.addAndGet(20)),executorService,bookList));
		executorService.execute(new LoadPageThread(Util.DOUBAN_URL+String.valueOf(Util.page.addAndGet(20)),executorService,bookList));
		executorService.execute(new LoadPageThread(Util.DOUBAN_URL+String.valueOf(Util.page.addAndGet(20)),executorService,bookList)); 			
		
		//开启排序进程
		Thread t1 = new Thread(new SortBooksThread(bookList));
		t1.start();
		
		while(true){    //循环判断页面加载任务是否已全部添加入线程池
			if(!loadFlag){
				executorService.shutdown(); //关闭线程池
				while(true){    //循环判断是否线程池任务已全部结束 包括待做任务
					if(executorService.isTerminated()){  
						System.out.println("所有的子线程都结束了！");  
						sortFlag = false ;  //所有任务已结束，书缓存队列已加载完毕 通知排序进程全部排序后可关闭进程
						break;  
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}  
				}
				break;
			}			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
		}
		
	}

	
	
}
	