package com.righthere.efam;

public class RequestedResults {
	String item_id;
	String item_title;
	String item_price;
	String item_size;
	String item_thumbnail_url;
	Integer item_units_in_cart;

	// Id
	public void setId(String id) {
		this.item_id = item_id;
	}

	public String getId() {
		return item_id;
	}

	// Title
	public void setTitle(String title) {
		this.item_title = item_title;
	}

	public String getTitle() {
		return item_title;
	}

	// Price
	public void setPrice(String price) {
		this.item_price = item_price;
	}

	public String getPrice() {
		return item_price;
	}

	// Size
	public void setSize(String size) {
		this.item_size = item_size;
	}

	public String getSize() {
		return item_size;
	}

	// Image
	public void setThumbnailUrl(String item_thumbnail_url) {
		this.item_thumbnail_url = item_thumbnail_url;
	}

	public String getThumbnailUrl() {
		return item_thumbnail_url;
	}

	// Total item units in cart
	public void setUnits(Integer units) {
		this.item_units_in_cart = item_units_in_cart;
	}

	public Integer getUnits() {
		return item_units_in_cart;
	}

}