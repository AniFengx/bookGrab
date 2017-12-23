package edu.zxp.bookGrab.entity;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.zxp.bookGrab.main.BookGrab;
import edu.zxp.bookGrab.utils.Util;

/**
* 页面加载线程类
* @author Anifengx
*
*/
public class LoadPageThread implements Runnable{
	
	public static boolean isProxy  = false;

	private String url;
	
	ExecutorService executorService ;
	
	BlockingQueue<Book> bookList ;
	/**
	   * 构造方法
	   * @param url
	   * @param executorService
	   * @param bookList
	   *       
	   */
	public LoadPageThread(String url , ExecutorService executorService, BlockingQueue<Book> bookList) {
		super();
		this.url = url;
		this.executorService = executorService ;
		this.bookList = bookList ;
	}



	public void run() {
		Document doc = null;
		if(isProxy){
			for(int i = 1 ; i > 0 ;i++ ){
			try {
				doc = Util.proxyIP(url);
				break;
			}catch (Exception e1) {
				// TODO Auto-generated catch block
				if(i > 5){
					if(Util.index.addAndGet(1) > Util.proxyList.size()-1){
						Util.index.set(0);
					}
				}		
				System.out.println("第"+i+"次失败，重新尝试");
			}
		}
		}else{
			try {
				doc = Jsoup.connect(url).header("Accept-Encoding", "gzip, deflate")  
					    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")  
					    .maxBodySize(0)  
					    .timeout(600000).get();
			} catch(HttpStatusException e){
				if(e.getStatusCode() == 403){
					System.out.println("开启代理");
					isProxy = true ;
					executorService.execute(new LoadPageThread(url,executorService,bookList));
					return ;
				}	
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//System.out.println(doc.text());
		
		//根据url参数判断是book列表或者book详细页面
		if(url.indexOf("subject") == -1){
			Elements books = doc.select("li.subject-item:has(div.info div.star span.pl:matches([0-9]{4,}))");
			for(Element e : books){
				String href = e.select("div.pic a.nbg").attr("href");
				href = href.startsWith("/") ? Util.DOUBAN_URL+href : href;
				executorService.execute(new LoadPageThread(href,executorService,bookList));
			}
			if(doc.text().indexOf("没有找到符合条件的图书") == -1){
				Util.page.addAndGet(20);
				executorService.execute(new LoadPageThread(Util.DOUBAN_URL+String.valueOf(Util.page),executorService,bookList));
			}else{
				BookGrab.loadFlag = false ;
			}
		}else{				
			Book book = Util.parseBook(doc);
			try {
				bookList.put(book);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(bookList.toString());			
	}
	
}
