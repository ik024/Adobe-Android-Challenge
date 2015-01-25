package com.example.adobeproducts;

import android.util.Log;

public class Product {

	private final String tag = Product.class.getSimpleName();
	private String name;
	private String in_app;
	private String rating;
	private String description;
	private String imageUrl;
	private String playstoreUrl;
	private String type;
	private String lastUpdated;
	
	public Product(String name, String in_app, String description, String imageUrl, String playstoreUrl, String type, String lastUpdated, String rating) {
		this.name = name;
		this.in_app = in_app;
		this.rating = rating;
		this.description = description;
		this.imageUrl = imageUrl;
		this.playstoreUrl = playstoreUrl;
		this.type = type;
		this.lastUpdated = lastUpdated;
		Log.d(tag, name+" product created");
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIn_app() {
		return this.in_app;
	}
	
	public void setIn_app(String in_app) {
		this.in_app = in_app;
	}
	
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getPlaystoreUrl() {
		return playstoreUrl;
	}
	public void setPlaystoreUrl(String playstoreUrl) {
		this.playstoreUrl = playstoreUrl;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
}
