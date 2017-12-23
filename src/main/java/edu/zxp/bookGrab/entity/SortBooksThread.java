package edu.zxp.bookGrab.entity;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

import edu.zxp.bookGrab.main.BookGrab;
import edu.zxp.bookGrab.utils.HeapSort;
import edu.zxp.bookGrab.utils.Util;

/**
* 排序线程类
* @author Anifengx
*
*/
public  class SortBooksThread implements Runnable{

	BlockingQueue<Book> bookList ;
	
	/**
	   * 构造方法
	   * @param bookList
	   *       
	   */
	public SortBooksThread(BlockingQueue<Book> bookList ) {
		super();
		this.bookList = bookList;
	}

	@Override
	public void run() {
		Book[] bestBooks = new Book[40];
		int index = 0 ;
		System.out.println("排序线程开启");
		// TODO Auto-generated method stub
		while(BookGrab.sortFlag){  //根据排序标志循环判断是否该停止线程
			System.out.println("bestBooks " + Arrays.toString(bestBooks));
			while(!bookList.isEmpty()){  //判断是否取完队列
				try {    
					Book bookTemp = bookList.take();
					if(index <= 39){     //book数组小于40直接添加 多于40进行判断添加					
						bestBooks[index] = bookTemp;
						index++;
					}else if(bestBooks[index-1].getScore() < bookTemp.getScore()){
						bestBooks[index-1] = bookTemp;
					}
					new HeapSort(bestBooks).sort(); 	
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  				
		}
		Util.exportExcel(bestBooks);   //调用工具类导出
		BookGrab.end = System.currentTimeMillis();
		System.out.println("\n用时"+"  "+(BookGrab.end-BookGrab.beg)+"毫秒");
	}
	
}

