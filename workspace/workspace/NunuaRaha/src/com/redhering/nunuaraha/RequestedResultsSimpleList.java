package com.redhering.nunuaraha;

public class RequestedResultsSimpleList {
	String item_id;
	String item_title;
	String delivery_charge;
	String item_thumbnail_url;

	public void setId(String id) {
		this.item_id = item_id;
	}

	public String getId() {
		return item_id;
	}

	public void setTitle(String title) {
		this.item_title = item_title;
	}

	public String getTitle() {
		return item_title;
	}

	public void setDeliveryCharge(String DeliveryCharge) {
		this.delivery_charge = delivery_charge;
	}

	public String getDeliveryCharge() {
		return delivery_charge;
	}

	// Image
	public void setThumbnailUrl(String item_thumbnail_url) {
		this.item_thumbnail_url = item_thumbnail_url;
	}

	public String getThumbnailUrl() {
		return item_thumbnail_url;
	}

	public boolean get(int i) {
		// TODO Auto-generated method stub
		return false;
	}

	public void clear() {
		// TODO Auto-generated method stub

	}

}
