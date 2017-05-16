package com.wjj.weiguan.pojo;


public class User {
	private Long id;
	private String user_id;
	private String user_name;
	private String token;
	private String token_secret;
	private String description;
	private String followers_count;
	private String friends_count;
	private String statuses_count;
	private String user_head;
	
	public String getFollowers_count() {
		return followers_count;
	}

	public void setFollowers_count(String followers_count) {
		this.followers_count = followers_count;
	}

	public String getFriends_count() {
		return friends_count;
	}

	public void setFriends_count(String friends_count) {
		this.friends_count = friends_count;
	}

	public String getStatuses_count() {
		return statuses_count;
	}

	public void setStatuses_count(String statuses_count) {
		this.statuses_count = statuses_count;
	}

	
	

	public User(String user_id, String user_name, String token,
			String token_secret, String description, String user_head) {
		super();
		this.user_id = user_id;
		this.user_name = user_name;
		this.token = token;
		this.token_secret = token_secret;
		this.description = description;
		this.user_head = user_head;
	}

	public User(String user_id, String user_name, String token,
			String token_secret, String description) {
		super();
		this.user_id = user_id;
		this.user_name = user_name;
		this.token = token;
		this.token_secret = token_secret;
		this.description = description;
	}
	
	public User(String user_id, String token, String token_secret) {
		super();
		this.user_id = user_id;
		this.token = token;
		this.token_secret = token_secret;
	}

	public User(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken_secret() {
		return token_secret;
	}

	public void setToken_secret(String token_secret) {
		this.token_secret = token_secret;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUser_head() {
		return user_head;
	}

	public void setUser_head(String user_head) {
		this.user_head = user_head;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", user_id=" + user_id + ", user_name="
				+ user_name + ", token=" + token + ", token_secret="
				+ token_secret + ", description=" + description
				+ ", user_head=" + user_head + "]";
	}

}
