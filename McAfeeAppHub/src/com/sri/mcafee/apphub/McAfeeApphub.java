package com.sri.mcafee.apphub;

import java.util.ArrayList;

import android.app.Application;
import android.content.Context;

import com.sri.mcafee.apphub.imageloader.SriImageLoader;
import com.sri.mcafee.apphub.json.Product;

public class McAfeeApphub extends Application {

	private static ArrayList<Product> productList;
	public static final String PRODUCT_POSITION = "product_position";
	private static SriImageLoader imageLoader;

	public static ArrayList<Product> getProductList() {
		return productList;
	}

	public static void setProductList(ArrayList<Product> list) {
		McAfeeApphub.productList = list;
	}

	public static SriImageLoader getImageLoader(Context context) {
		if (imageLoader == null) {
			imageLoader = new SriImageLoader(context);
		}

		return imageLoader;
	}
}
