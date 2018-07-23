package com.paidtocode.stargang.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by vihanga on 7/21/18 in stargang.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Subscribe implements Serializable {

	private int productId;
	private String customerNo;

	public Subscribe() {
	}

	public Subscribe(int productId, String customerNo) {
		this.productId = productId;
		this.customerNo = customerNo;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
}
