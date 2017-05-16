package com.wjj.weiguan.pojo;

public class RemindItem {
	private int reId;
	private String text;
	private String tv_num;
	private int reId_arrow;

	public int getReId_arrow() {
		return reId_arrow;
	}

	public void setReId_arrow(int reId_arrow) {
		this.reId_arrow = reId_arrow;
	}

	public RemindItem(int reId, String text, String tv_num, int reId_arrow) {
		super();
		this.reId = reId;
		this.text = text;
		this.tv_num = tv_num;
		this.reId_arrow = reId_arrow;
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

	public String getTv_num() {
		return tv_num;
	}

	public void setTv_num(String tv_num) {
		this.tv_num = tv_num;
	}
}
