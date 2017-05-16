package com.wjj.weiguan.pojo;

public class ToolItem {
	private int reId;
	private String text;

	public ToolItem(int reId, String text) {
		super();
		this.reId = reId;
		this.text = text;
	}

	public int getReId() {
		return reId;
	}

	public void setReId(int reId) {
		this.reId = reId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
