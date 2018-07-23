package com.paidtocode.stargang.modal;

import android.text.TextUtils;

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
	private boolean likeByMe;
	private boolean commentByMe;
	private List<WallImage> images;

	public Wall() {
	}

	public Wall(String postID) {
		this.postID = postID;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Wall wall = (Wall) o;
		return TextUtils.equals(postID, wall.postID);
	}

}
