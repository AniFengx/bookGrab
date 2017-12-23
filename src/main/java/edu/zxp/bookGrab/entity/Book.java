package edu.zxp.bookGrab.entity;


/**
* 书类
* @author Anifengx
*
*/
public class Book {
	private String name; //书名
	private float score; //得分
	private String evaluatorNum;  //评价人数
	private String author; //作者
	private String press;  //出版社
	private String publicationDate;  //出版日期
	private String price;  //定价

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public String getEvaluatorNum() {
		return evaluatorNum;
	}

	public void setEvaluatorNum(String evaluatorNum) {
		this.evaluatorNum = evaluatorNum;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPress() {
		return press;
	}

	public void setPress(String press) {
		this.press = press;
	}

	public String getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Book() {
		super();
	}

	@Override
	public String toString() {
		return "Book [name=" + name + ", score=" + score + ", evaluatorNum=" + evaluatorNum + ", author=" + author + ", press="
				+ press + ", publicationDate=" + publicationDate + ", price=" + price + "]";
	}

}
