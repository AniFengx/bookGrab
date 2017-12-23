package edu.zxp.bookGrab.entity;

import java.util.Date;

/**
* 代理资源类
* @author Anifengx
*
*/
public class ProxySource {
	private String ip;  		//代理服务器IP地址
	private int port; 			//代理服务器端口
	private String addr;		//代理服务器地址
	private String type;		//地理类型
	private Date validTim;		//代理有效日期

	public ProxySource() {
		super();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getValidTim() {
		return validTim;
	}

	public void setValidTim(Date validTim) {
		this.validTim = validTim;
	}

	@Override
	public String toString() {
		return "ProxySource [ip=" + ip + ", port=" + port + ", addr=" + addr + ", type=" + type + ", validTim="
				+ validTim + "]";
	}

}
