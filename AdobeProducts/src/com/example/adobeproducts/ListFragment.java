package com.example.adobeproducts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ListFragment extends Fragment {

	private final String tag = ListFragment.class.getSimpleName();
	public static final String KEY_POSITION = "position";
	private final String URL = "http://adobe.0x10.info/api/products?type=json";
	public static ArrayList<Product> productList = new ArrayList<Product>();
	public static ListView listView;
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.d(tag, "onCreateView Called");
		
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		
		productList.clear();
		
		//GEt product data from the url and set the list view adapter in onPost execute
		new GetProductsData(getActivity()).execute(URL);
		
		listView = (ListView) view.findViewById(R.id.listView1);
		
		//Animate the list items
		animateListItems(listView);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,	int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putInt(KEY_POSITION, position);
				
				//Animate the fragment transaction
				 ProductFragment fragment2 = new ProductFragment();
				 fragment2.setArguments(bundle);
				 FragmentManager fragmentManager = getFragmentManager();
				 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); 
				 fragmentTransaction.setCustomAnimations( R.anim.translate_right_to_left, R.anim.translate_left_to_right);
				 fragmentTransaction.replace(R.id.container, fragment2);
				 fragmentTransaction.commit(); 
				
				 //Change the tOoolBar subject from - home to product details
				 changeToolBarSub();
			}
		});
		return view;
		
	}

	
	private void animateListItems(ListView listView) {
			AnimationSet set = new AnimationSet(true);
		 	Animation animation  = new TranslateAnimation(0, 0, 800, 0);
		    animation.setDuration(300); 
		    set.addAnimation(animation);    
		    LayoutAnimationController controller = new LayoutAnimationController(set, 0.2f);
		 
		    listView.setLayoutAnimation(controller);
		
	}

	private void changeToolBarSub() {
		final ObjectAnimator animator = ObjectAnimator.ofInt(MainActivity.subject, "textColor",getResources().getColor(android.R.color.darker_gray), getResources().getColor(R.color.view_background_color));
		animator.setDuration(1000);
		animator.setEvaluator(new ArgbEvaluator());
		animator.setInterpolator(new LinearInterpolator());
		animator.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				MainActivity.title.setText("AppHUB");
				MainActivity.subject.setText("product details");
				final ObjectAnimator animator1 = ObjectAnimator.ofInt(MainActivity.subject, "textColor",  getResources().getColor(R.color.view_background_color), getResources().getColor(android.R.color.darker_gray));
				animator1.setDuration(1000);
				animator1.setStartDelay(500);
				animator1.setEvaluator(new ArgbEvaluator());
				animator1.setInterpolator(new LinearInterpolator());
				animator1.start();
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
		animator.start();
		
		
	}
	
	static class GetProductsData extends AsyncTask<String, Void, String>{
		private final String tag = GetProductsData.class.getSimpleName();
		
		InputStream is = null;
		 String json = null;
		private Context context;
		 
		GetProductsData(Context context){
			this.context = context;
		}
		 
		@Override
		protected String doInBackground(String... params) {
			 try {
				
			      // defaultHttpClient
			      DefaultHttpClient httpClient = new DefaultHttpClient();
			      HttpPost httpPost = new HttpPost(params[0]);
			      HttpResponse httpResponse = httpClient.execute(httpPost);
			      HttpEntity httpEntity = httpResponse.getEntity();
			      is = httpEntity.getContent();
			    } catch (UnsupportedEncodingException e) {
			      e.printStackTrace();
			    } catch (ClientProtocolException e) {
			      e.printStackTrace();
			    } catch (IOException e) {
			      e.printStackTrace();
			    }
			    try {
			      BufferedReader reader = new BufferedReader(new InputStreamReader(
			          is, "iso-8859-1"), 8);
			      StringBuilder sb = new StringBuilder();
			      String line = null;
			      while ((line = reader.readLine()) != null) {
			        sb.append(line + "n");
			      }
			      is.close();
			      json = sb.toString();
			     // Log.d(tag, json);
			    } catch (Exception e) {
			      Log.e("Buffer Error", "Error converting result " + e.toString());
			    }
			 
			return json;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result != null){
				// parse the string 
				getJsonData(json);
			}else{
				Toast.makeText(this.context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
			}
		}
		private void getJsonData(String json) {
			try {
				
				JSONArray jsonArray = new JSONArray(json);
				
				for(int i = 0; i< jsonArray.length(); i++){
					
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String name = jsonObject.getString("name");
					String rating = jsonObject.getString("rating");
					String in_app = jsonObject.getString("inapp-purchase");
					String type =  jsonObject.getString("type");
					String lastUpdated =  jsonObject.getString("last updated");
					String description =  jsonObject.getString("description");
					String imageUrl =  jsonObject.getString("image");
					String playstoreUrl =  jsonObject.getString("url");
				
					if(!name.isEmpty()){ //Proceed only if Product name is valid i.e not empty
						//Add the products in an ArrayList
						productList.add(new Product(name, in_app, description, imageUrl, playstoreUrl, type, lastUpdated, rating));
					}else{
						Log.e(tag, "Empty product");
					}
				}
				
				Log.d(tag, "Number of products: "+productList.size());
				
				//set the listview adapter
				listView.setAdapter(new CustomAdapter((Activity) context));
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
	}
}
