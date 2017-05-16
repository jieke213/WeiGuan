package com.wjj.weiguan.pojo;

public class WeiboInfo {
	private String id;
	private String user_id;
	private String text;
	private String user_name;
	private String user_head;
	private String time;
	private boolean isImage = false;
	private String Image_url;
	private String forward;
	private String comments;
	private String thumb_up;

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getThumb_up() {
		return thumb_up;
	}

	public void setThumb_up(String thumb_up) {
		this.thumb_up = thumb_up;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_head() {
		return user_head;
	}

	public void setUser_head(String user_head) {
		this.user_head = user_head;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean isImage() {
		return isImage;
	}

	public void setImage(boolean isImage) {
		this.isImage = isImage;
	}

	public String getImage_url() {
		return Image_url;
	}

	public void setImage_url(String image_url) {
		Image_url = image_url;
	}

	@Override
	public String toString() {
		return "WeiboInfo [id=" + id + ", user_id=" + user_id + ", text="
				+ text + ", user_name=" + user_name + ", user_head="
				+ user_head + ", time=" + time + ", isImage=" + isImage
				+ ", Image_url=" + Image_url + "]";
	}

}
