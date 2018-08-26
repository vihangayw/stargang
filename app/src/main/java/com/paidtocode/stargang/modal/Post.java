package com.paidtocode.stargang.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vihanga on 7/21/18 in stargang.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post implements Serializable {

	private String pID;
	private String uID;
	private String pText;
	private String agoTime;
	private int likes;
	private int comments;
	private boolean likeByMe;
	private boolean commentByMe;
	private List<Image> images;

	public Post() {
	}


	public boolean isLikeByMe() {
		return likeByMe;
	}

	public void setLikeByMe(boolean likeByMe) {
		this.likeByMe = likeByMe;
	}

	public boolean isCommentByMe() {
		return commentByMe;
	}

	public void setCommentByMe(boolean commentByMe) {
		this.commentByMe = commentByMe;
	}

	public String getpID() {
		return pID;
	}

	public void setpID(String pID) {
		this.pID = pID;
	}

	public String getuID() {
		return uID;
	}

	public void setuID(String uID) {
		this.uID = uID;
	}

	public String getpText() {
		return pText;
	}

	public void setpText(String pText) {
		this.pText = pText;
	}

	public String getAgoTime() {
		return agoTime;
	}

	public void setAgoTime(String agoTime) {
		this.agoTime = agoTime;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}
}
