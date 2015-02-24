package com.example.adobeproducts;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter{

	Activity activity;
	ArrayList<Product> products;
	LayoutInflater inflater;

    //this is a comment and this is useless
	public CustomAdapter(Activity activity) {
		this.activity = activity;
		products = ListFragment.productList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return products.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	ViewHolder holder;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(inflater == null){
			inflater = activity.getLayoutInflater();
		}
		
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_row, parent , false);
			holder = new ViewHolder(convertView);
		}
		
		Product product = products.get(position);
		holder.name.setText(product.getName());
		holder.inApp.setText("In-App: "+product.getIn_app());
		return convertView;
	}

	class ViewHolder{
		TextView name, inApp;
		
		public ViewHolder(View view) {
			name = (TextView) view.findViewById(R.id.product_name);
			inApp = (TextView) view.findViewById(R.id.product_in_app);
		}
	}
}
