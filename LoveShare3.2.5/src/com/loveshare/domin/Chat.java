package com.loveshare.domin;

/**
 * 聊天类，将聊天内容、发送和接收的信息标志封装到这里
 * @author 温坤哲
 *
 */
public class Chat {

	private String content;
	private int flag;

	public Chat(String content, int flag) {
		super();
		this.content = content;
		this.flag = flag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
