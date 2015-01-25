package com.example.adobeproducts;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProductFragment extends Fragment {

	private final String tag = ProductFragment.class.getSimpleName();
	TextView product_name, product_rating, product_description;
	TextView  type_title, type_subject, inapp_title, inapp_subject, lastUpdate_title, lastUpdate_subject;
	LinearLayout type_root, inApp_root, lastUpdate_root;
	ImageView product_image;
	static Product selectedProduct;
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.d(tag, "onCreateView Called");
	
		 int position=getArguments().getInt(ListFragment.KEY_POSITION);
		 selectedProduct = ListFragment.productList.get(position);
		 
		if(selectedProduct != null){//proceed only if selectedProduct is not null
			
				View view = inflater.inflate(R.layout.fragment_product, container, false);
				
				product_name = (TextView) view.findViewById(R.id.product_name);
				product_rating = (TextView) view.findViewById(R.id.rating_text);
				product_description = (TextView) view.findViewById(R.id.description);
				product_image = (ImageView) view.findViewById(R.id.product_image);
			
				//Download the image of the product
				new DownloadImageTask(product_image).execute(selectedProduct.getImageUrl()); 
				
				animateRightToLeft_Bounce(product_image, 1000);
				
				product_name.setText(selectedProduct.getName());
				product_rating.setText(selectedProduct.getRating());
				product_description.setText(selectedProduct.getDescription());
				
				View type = view.findViewById(R.id.type);
				View inapp = view.findViewById(R.id.in_app);
				View lastUpdate = view.findViewById(R.id.lastUpdate);
				View navBar = view.findViewById(R.id.nav_bar);
				
				initializeToolBar_Color(type, type_title, type_subject, "Type", selectedProduct.getType(), type_root);
				initializeToolBar_Color(inapp, inapp_title, inapp_subject, "In-App purchases", selectedProduct.getIn_app(), inApp_root);
				initializeToolBar_Color(lastUpdate, lastUpdate_title, lastUpdate_subject, "Last Updated", selectedProduct.getLastUpdated(), lastUpdate_root);
				
				//Do the animation
				animateRightToLeft(type, 1400);
				animateLeftToRight(inapp, 1600);
				animateRightToLeft(lastUpdate, 1400);
				animateBottomToTop(navBar, 1400);
				alphaAnimation(product_description, 2800);
				
				return view;
			}else{
				return null;
			}
	}

	private void initializeToolBar_Color(View toolBar, TextView title, TextView subject, String titleString, String subString, LinearLayout root) {
		
		toolBar.setBackgroundColor(getResources().getColor(R.color.main_color));
		
		subject = (TextView) toolBar.findViewById(R.id.tool_bar_subject);
		title = (TextView) toolBar.findViewById(R.id.tool_bar_title);
		
		title.setText(titleString);
		subject.setText(subString);
		
		title.setTextColor(Color.parseColor("#808080"));
		
	}
	
	private void animateRightToLeft_Bounce(View view, long offSet){
	    Animation translate = new TranslateAnimation(-800, 0, 0, 0);
        translate.setInterpolator(new BounceInterpolator());
        translate.setDuration(1000);
        translate.setStartOffset(offSet);
        view.setAnimation(translate);
	}
	
	private void animateBottomToTop(View view, long offSet){
	    Animation translate = new TranslateAnimation(0, 0, 800, 0);
        translate.setInterpolator(new LinearInterpolator());
        translate.setDuration(1000);
        translate.setStartOffset(offSet);
        view.setAnimation(translate);
	}
	
	private void animateRightToLeft(View view, long offSet){
	    Animation translate = new TranslateAnimation(-800, 0, 0, 0);
        //translate.setInterpolator(new AccelerateInterpolator());
        translate.setDuration(1000);
        translate.setStartOffset(offSet);
        view.setAnimation(translate);
	}
	
	private void animateLeftToRight(View view, long offSet){
	    Animation translate = new TranslateAnimation(800, 0, 0, 0);
	    //translate.setInterpolator(new AccelerateInterpolator());
        translate.setDuration(1000);
        translate.setStartOffset(offSet);
        view.setAnimation(translate);
	}
	
	private void alphaAnimation(View view, long offSet){
	    Animation alpha = new AlphaAnimation(0.0f, 1.0f);
	    alpha.setInterpolator(new LinearInterpolator());
	    alpha.setDuration(2000);
	    alpha.setStartOffset(offSet);
        view.setAnimation(alpha);
	}
	
	public static  Product getSelectedProduct(){
		return selectedProduct;
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;
	 
	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    } 
	 
	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try { 
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        } 
	        return mIcon11;
	    } 
	 
	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	        
	    } 
	}
}
