package com.sri.mcafee.apphub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.actionbarsherlock.view.MenuItem;
import com.sri.mcafee.apphub.json.Product;
import com.sri.mcafee.apphub.views.ColoredRatingBar;
import com.sri.mcafee.apphub.views.TextAwesome;

public class ProductActivity extends BaseActivity {

	private TextAwesome productName;
	private ColoredRatingBar productRating;
	private TextAwesome productRatingText;
	private TextAwesome productPrice;
	private TextAwesome productDescription;
	private TextAwesome productType;
	private TextAwesome productLastUpdated;
	private ImageView productImage;
	private TextAwesome productUsers;

	private Product selectedProduct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product);
		initUi();
		initComponents();
	}

	private void initUi() {
		productName = (TextAwesome) findViewById(R.id.product_details_name);
		productRating = (ColoredRatingBar) findViewById(R.id.product_details_rating);
		productRatingText = (TextAwesome) findViewById(R.id.product_details_rating_text);
		productPrice = (TextAwesome) findViewById(R.id.product_details_price);
		productUsers = (TextAwesome) findViewById(R.id.product_details_users);
		productDescription = (TextAwesome) findViewById(R.id.product_description);
		productType = (TextAwesome) findViewById(R.id.product_details_type);
		productLastUpdated = (TextAwesome) findViewById(R.id.product_details_lastupdated);
		productImage = (ImageView) findViewById(R.id.product_image);
	}

	private void initComponents() {
		int index = getIntent().getIntExtra(McAfeeApphub.PRODUCT_POSITION, 0);
		selectedProduct = McAfeeApphub.getProductList().get(index);
		setTitle(selectedProduct.getName());
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		productName.setText(selectedProduct.getName());
		productRating.setRating(Float.parseFloat(selectedProduct.getRating()));
		productRatingText.setText(selectedProduct.getRating());
		if (selectedProduct.getPriceFloat() > 0) {
			productPrice.setText(String.format(getString(R.string.product_price), selectedProduct.getPrice()));
		} else {
			productPrice.setText(R.string.product_price_free);
		}
		productDescription.setText(selectedProduct.getDescription());
		productUsers.setText(selectedProduct.getUsers());
		productType.setText(selectedProduct.getType());
		productLastUpdated.setText(selectedProduct.getLastUpdated());
		// remove last three chars from the image url. otherwise its not getting download
		String imageUrl = selectedProduct.getImage().substring(0, selectedProduct.getImage().length() - 3);
		McAfeeApphub.getImageLoader(getApplicationContext()).DisplayImage(imageUrl, productImage, true);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void playStoreClicked(View v) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(selectedProduct.getUrl()));
		startActivity(i);
	}

	public void shareAppClicked(View v) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
	    sharingIntent.setType("text/plain");
	    String shareBody = "Hey Buddy, I have shared " + selectedProduct.getName() + "app to you. You can click this link to get this app. " + selectedProduct.getUrl();
	    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, selectedProduct.getName());
	    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
	    startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}

	public void shareSmsClicked(View v) {
		Intent sendIntent = new Intent(Intent.ACTION_VIEW);
		sendIntent.setData(Uri.parse("sms:"));
		sendIntent.putExtra("sms_body", selectedProduct.getUrl());
		startActivity(sendIntent);
	}

}
