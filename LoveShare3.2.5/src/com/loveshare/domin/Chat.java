package com.loveshare.domin;

/**
 * �����࣬���������ݡ����ͺͽ��յ���Ϣ��־��װ������
 * @author ������
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
