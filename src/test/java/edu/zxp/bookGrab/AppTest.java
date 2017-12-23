package edu.zxp.bookGrab;

import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Document;

import edu.zxp.bookGrab.entity.Book;
import edu.zxp.bookGrab.utils.HeapSort;
import edu.zxp.bookGrab.utils.Util;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    
    
    /**
     * 测试导出excel方法
     */
    public void testHeapSort(){
    	Book b1 = new   Book();
        b1.setScore(9.1f);
        Book b2 = new   Book();
        b2.setScore(9.3f);
        Book b3 = new   Book();
        Book b4 = new   Book();
        Book b5 = new   Book();
        b3.setScore(8.0f);
        b4.setScore(9.6f);
        b5.setScore(9.9f);
        Book b6 = new   Book();
        b6.setScore(6.7f);
        Book[] arr = {b1,b2,b3,b4,b5,null};
        Book b7 = new   Book();
        b7.setScore(9.4f);       
        new HeapSort(arr).sort(); 
        System.out.println(Arrays.toString(arr));
    }
    
    /**
     * 测试导出excel方法
     */
    public void testExportExcel(){
    	Book book = new Book();
    	book.setAuthor("测试");
    	book.setEvaluatorNum("1300");
    	book.setName("Java");
    	book.setPress("Java出版");
    	book.setPrice("13元");
    	book.setPublicationDate("2017-4");
    	book.setScore(9.1f);
    	Book[] arr = new Book[1];
    	arr[0] = book;
    	Util.exportExcel(arr);
    }
    
    /**
     * 测试获取代理资源方法
     */
    public void testGetProxyIP(){
			List list = Util.getProxyList();
			System.out.println(list);
    }
    /**
     * 测试动态代理方法
     */
    public void testProxyIp(){
    	List list =Util.getProxyList();
    	System.out.println(list);
    	Document doc = null;
    	for(int i = 1 ; i > 0 ;i++ ){
			try {
				doc = Util.proxyIP("https://book.douban.com/tag/编程?type=S&start=0");
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
    	System.out.println(doc.toString());
    }
}
