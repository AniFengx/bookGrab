package edu.zxp.bookGrab.utils;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.filechooser.FileSystemView;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.zxp.bookGrab.entity.Book;
import edu.zxp.bookGrab.entity.ProxySource;


/**
* 工具类
* @author Anifengx
*
*/
public class Util {
	/**  获取所需数据的键的常量     */
	public static final String AUTHOR = "作者";
	public static final String PRESS = "出版社";
	public static final String PUBLICATIONDATE = "出版年";
	public static final String PRICE = "定价";
	public static final String MINUTE = "分";
	public static final String HOUR = "小时";
	public static final String DAY = "天";
	/**  请求地址常量     */
	public static String DOUBAN_URL = "https://book.douban.com/tag/编程?type=S&start=";
	
	/**  页数常量     */
	public static AtomicInteger page = new AtomicInteger(0);
	
	/**  代理列索引     */
	public static AtomicInteger index = new AtomicInteger(0);
	
	/**  代理缓存列     */
	public static CopyOnWriteArrayList<ProxySource> proxyList = new CopyOnWriteArrayList<ProxySource>();
	/**
	   * 导出excel方法
	   * @param arr
	   *       
	   */
	public static void exportExcel(Book[] arr){
		try{
		   HSSFWorkbook wb = new HSSFWorkbook();
		   HSSFSheet sheet = wb.createSheet("data");  
		   
		   HSSFRow row = sheet.createRow((int) 0);  
		   HSSFCellStyle style = wb.createCellStyle();  
	       style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		   
	       HSSFCell cell = row.createCell((short) 0);  
	       cell.setCellValue("序号");  
	       cell.setCellStyle(style);  
	       
	       cell = row.createCell((short) 1);  
	       cell.setCellValue("书名");  
	       cell.setCellStyle(style); 
	       
	       cell = row.createCell((short) 2);  
	       cell.setCellValue("评分");  
	       cell.setCellStyle(style); 
	       
	       cell = row.createCell((short) 3);  
	       cell.setCellValue("评价人数");  
	       cell.setCellStyle(style); 
	       
	       cell = row.createCell((short) 4);  
	       cell.setCellValue("作者");  
	       cell.setCellStyle(style); 
	       
	       cell = row.createCell((short) 5);  
	       cell.setCellValue("出版社");  
	       cell.setCellStyle(style); 
	       
	       cell = row.createCell((short) 6);  
	       cell.setCellValue("出版日期");  
	       cell.setCellStyle(style); 
	       
	       cell = row.createCell((short) 7);  
	       cell.setCellValue("价格");  
	       cell.setCellStyle(style); 
	       
	       for(int i = 0 ; i < arr.length ; i++){
	    	   if(null == arr[i])
	    		   return;
	    	   row = sheet.createRow((int) i + 1);
	    	   row.createCell((short) 0).setCellValue(i+1);
	    	   row.createCell((short) 1).setCellValue(arr[i].getName());
	    	   row.createCell((short) 2).setCellValue(arr[i].getScore());
	    	   row.createCell((short) 3).setCellValue(arr[i].getEvaluatorNum());
	    	   row.createCell((short) 4).setCellValue(arr[i].getAuthor());
	    	   row.createCell((short) 5).setCellValue(arr[i].getPress());
	    	   row.createCell((short) 6).setCellValue(arr[i].getPublicationDate());
	    	   row.createCell((short) 7).setCellValue(arr[i].getPrice());
	       }
		    // Write the output to a file
	       sheet.autoSizeColumn(4);
	       sheet.autoSizeColumn(5);
	       sheet.autoSizeColumn(1);
	       sheet.autoSizeColumn(6);
	       sheet.autoSizeColumn(7);
	       
		   FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.dir")+"/src/main/resources/workbook.xls");
		   wb.write(fileOut);
		   fileOut.close();
		}catch(Exception e){
		   e.printStackTrace();
		}

	}
	
	/**
	   * 通过document解析出book类
	   * @param doc
	   *       
	   */
	public static Book parseBook(Document doc){
		Elements bookOriginal= doc.select("div#info");
		Book book = new Book();
		//通过br标签对内容进行分割处理
		String[] bookAttrs = Jsoup.parse(bookOriginal.toString().replaceAll("(?i)<br[^>]*>", "brAn")).text().split("brAn");
		Map<String,String> bookMap = new HashMap<String,String>();
		for(int i = 0 ;i < bookAttrs.length ; i ++){  //将解析出的string数组转入map中
			String[] keyValue = bookAttrs[i].split(":");
			if( keyValue.length == 2){
				bookMap.put(keyValue[0].trim(), keyValue[1]);
			}
		}
		book.setAuthor(bookMap.get(Util.AUTHOR));
		book.setPress(bookMap.get(Util.PRESS));
		book.setPrice(bookMap.get(Util.PRICE));
		book.setPublicationDate(bookMap.get(Util.PUBLICATIONDATE));
		book.setName(doc.select("h1").text());
		book.setEvaluatorNum(doc.select("div.rating_sum span a span[property]").text());
		book.setScore(Float.parseFloat(doc.select("strong.ll.rating_num").text()));
		return book;
	}
	
	/**
	   * 代理IP方法
	   * @param strUrl
	   *       
	   */
	public static Document proxyIP(String strUrl) throws Exception{
		Document doc = null ;
		String ip = proxyList.get(index.get()).getIp();
		int port = proxyList.get(index.get()).getPort();
		
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));			 
			URL url = new URL(strUrl);  
			HttpsURLConnection urlcon = (HttpsURLConnection)url.openConnection(proxy);  
			urlcon.connect();         //获取连接  
			InputStream is = urlcon.getInputStream();  
			BufferedReader buffer = new BufferedReader(new InputStreamReader(is));  
			StringBuffer bs = new StringBuffer();  
			String l = null;  
			while((l=buffer.readLine())!=null){  
			bs.append(l);  
			}   
			doc = Jsoup.parse(bs.toString());		
		return doc;		
	}
	public static List getProxyList() {
		List<ProxySource> list = null;
		Document doc = null;
		try {
			doc = Jsoup.connect("http://www.xicidaili.com/nn").header("Accept-Encoding", "gzip, deflate")  
				    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")  
				    .maxBodySize(0)  
				    .timeout(600000).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		list = parseProxy(doc);
		proxyList.addAll(list);
		return list;
	}
	
	/**
	   * 通过document解析出代理资源类方法
	   * @param doc
	   *       
	   */
	public static List<ProxySource> parseProxy(Document doc){
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
		List<ProxySource> proxySourceList = new ArrayList<ProxySource>();
		Elements books = doc.select("tr:has(td)");
		for(Element e : books){
			ProxySource proxySource = new ProxySource();
			Elements proxyE = e.children();
			for(int i = 0 ; i < proxyE.size() ; i++){
				proxySource.setIp(proxyE.get(1).text().trim());
				proxySource.setPort(Integer.valueOf(proxyE.get(2).text().trim()));
				proxySource.setAddr(proxyE.get(3).text().trim());
				proxySource.setType(proxyE.get(5).text().trim());
				Long tim = null;
				Long validTim = null;
				try {
					tim = sdf.parse(proxyE.get(9).text()).getTime();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(proxyE.get(8).text().trim().indexOf(Util.MINUTE) != -1){
					String str = proxyE.get(8).text().trim().substring(0, proxyE.get(8).text().trim().indexOf(Util.MINUTE));
					validTim = tim + (Long.valueOf(str) * 60000);
				}else if(proxyE.get(8).text().trim().indexOf(Util.HOUR) != -1){
					String str = proxyE.get(8).text().trim().substring(0, proxyE.get(8).text().trim().indexOf(Util.HOUR));
					validTim = tim + (Long.valueOf(str) * 60000 * 60);
				}
				else if(proxyE.get(8).text().trim().indexOf(Util.DAY) != -1){
					String str = proxyE.get(8).text().trim().substring(0, proxyE.get(8).text().trim().indexOf(Util.DAY));
					validTim = tim + (Long.valueOf(str) * 60000 * 24);
				}
				proxySource.setValidTim(new Date(validTim));
				proxySourceList.add(proxySource);
			}
		}
		return proxySourceList;
	}
	
}
