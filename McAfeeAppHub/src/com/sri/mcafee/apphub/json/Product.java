package com.sri.mcafee.apphub.json;

import org.json.JSONException;
import org.json.JSONObject;

public class Product implements Comparable<Product> {

	private String name;
	private String type;
	private String url;
	private String image;
	private String rating;
	private int users;
	private float price;
	private String last_updated;
	private String description;
	private static ProductSort sortMethod = ProductSort.NONE;

	public enum ProductSort {
		NONE, USERS, PRICE, RATING;
	};

	public Product(JSONObject jobj) {
		this.name = jobj.optString(JSONKeys.NAME);
		this.type = jobj.optString(JSONKeys.TYPE);
		this.url = jobj.optString(JSONKeys.URL);
		this.image = jobj.optString(JSONKeys.IMAGE);
		this.rating = jobj.optString(JSONKeys.RATING);
		this.price = jobj.optLong(JSONKeys.PRICE);
		this.users = jobj.optInt(JSONKeys.USERS);
		this.last_updated = jobj.optString(JSONKeys.LAST_UPDATED);
		this.description = jobj.optString(JSONKeys.DESCRIPTION);
	}

	public Product(String jstring) throws JSONException {
		this(new JSONObject(jstring));
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getUrl() {
		return url;
	}

	public String getImage() {
		return image;
	}

	public String getRating() {
		return rating;
	}
	
	public float getPriceFloat() {
		return price;
	}
	
	public String getPrice() {
		return String.valueOf(price);
	}
	
	public int getUsersInt() {
		return users;
	}
	
	public String getUsers() {
		return String.valueOf(users);
	}

	public String getLastUpdated() {
		return last_updated;
	}

	public String getDescription() {
		return description;
	}

	public static void setSort(ProductSort sort) {
		sortMethod = sort;
	}

	@Override
	public int compareTo(Product another) {
		if (sortMethod == ProductSort.RATING) {
			if (Float.parseFloat(this.getRating()) > Float.parseFloat(another.getRating())) {
				return -1;
			}
			if (Float.parseFloat(this.getRating()) < Float.parseFloat(another.getRating())) {
				return 1;
			}

			return 0;
		} else if (sortMethod == ProductSort.PRICE){
			if (this.getPriceFloat() > another.getPriceFloat()) {
				return -1;
			}
			if (this.getPriceFloat() < another.getPriceFloat()) {
				return 1;
			}

			return 0;
		} else {
			if (this.getUsersInt() > another.getUsersInt()) {
				return -1;
			}
			if (this.getUsersInt() < another.getUsersInt()) {
				return 1;
			}

			return 0;
		}
	}

}
