package com.paidtocode.stargang.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vihanga on 7/21/18 in stargang.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wall implements Serializable {

	private String iduser;
	private String userFullName;
	private String postID;
	private String postText;
	private String agoTime;
	private int likes;
	private int comments;
	private List<WallImage> images;

	public Wall() {
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

	public String getIduser() {
		return iduser;
	}

	public void setIduser(String iduser) {
		this.iduser = iduser;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getPostID() {
		return postID;
	}

	public void setPostID(String postID) {
		this.postID = postID;
	}

	public String getPostText() {
		return postText;
	}

	public void setPostText(String postText) {
		this.postText = postText;
	}

	public List<WallImage> getImages() {
		return images;
	}

	public void setImages(List<WallImage> images) {
		this.images = images;
	}
}
