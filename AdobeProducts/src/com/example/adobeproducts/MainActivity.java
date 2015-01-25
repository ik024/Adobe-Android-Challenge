package com.example.adobeproducts;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private final String tag = MainActivity.class.getSimpleName();
	static  View toolBar;
	static TextView title, subject;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Getting reference to tool bar
		 toolBar = findViewById(R.id.toolbar);
		 subject = (TextView) MainActivity.toolBar.findViewById(R.id.tool_bar_subject);
		 title = (TextView) MainActivity.toolBar.findViewById(R.id.tool_bar_title);

		 //Initail fragment transaction to LList Fragment 
		Fragment listFrag = new ListFragment();
		
		FragmentManager fm = getSupportFragmentManager();
		
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.container, listFrag, "list");
		ft.addToBackStack("list");
		ft.commit();
		
		
	}
	
	@Override
	public void onBackPressed() {
		
	}

	//Back button in ProductFragment
	public void goBack(View view){
		 ListFragment fragment = new ListFragment();

		 FragmentManager fragmentManager = getSupportFragmentManager();
		 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); 
		 fragmentTransaction.setCustomAnimations( R.anim.translate_right_to_left_reverse, R.anim.translate_left_to_right_reverse);
		 fragmentTransaction.replace(R.id.container, fragment);
		 fragmentTransaction.commit(); 
		
		 //Change the ToolBar subject from- product details to home
		 changeToolBarSub();
	}
	
	//share button in ProductFragment
	public void share(View view){
		Product product = ProductFragment.getSelectedProduct();
		String texToShare = "Name: "+product.getName()+"\n";
		texToShare = texToShare+"Ratings: "+product.getRating()+"\n";
		texToShare = texToShare+"Type: "+product.getType()+"\n";
		texToShare = texToShare+"In-App: "+product.getIn_app()+"\n";
		texToShare = texToShare+"Description: "+product.getDescription()+"\n";
		texToShare = texToShare+"PlayStore Url: "+product.getPlaystoreUrl()+".";
		
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
	    sharingIntent.setType("text/plain"); 
	    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, texToShare); 
	    startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}
	
	//PlayStore button in ProductFragment
	public void playStore(View view){
		Product product = ProductFragment.getSelectedProduct();
		String[] psUrl = product.getPlaystoreUrl().split("=");
		final String appPackageName = psUrl[1]; // getPackageName() from Context or Activity object
		Log.d(tag, appPackageName);
		try { 
		    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
		} catch (android.content.ActivityNotFoundException anfe) {
		    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
		} 
	}
	
	//Sms button in ProductFragment
	public void sms(View view){
		Log.d(tag, "sms called");
		Product product = ProductFragment.getSelectedProduct();
		String texToShare = "Name: "+product.getName()+"\n";
		texToShare = texToShare+"Ratings: "+product.getRating()+"\n";
		texToShare = texToShare+"PlayStore Url: "+product.getPlaystoreUrl()+".";
		
		//Show custom dialog to get number from user
		showCustomDialog(product.getName(),texToShare);
		
	}
	
	 private void showCustomDialog(final String name,final String messageToSend) {
			// TODO Auto-generated method stub
			 Context context = MainActivity.this;
			 LayoutInflater li = LayoutInflater.from(context);
				View promptsView = li.inflate(R.layout.custom_dialogbox_prompt, null);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);

				// set prompts.xml to alertdialog builder
				alertDialogBuilder.setView(promptsView);
				final TextView message = (TextView) promptsView
						.findViewById(R.id.dialogMessagTv);
				
				final EditText userInput = (EditText) promptsView
						.findViewById(R.id.editTextDialogUserInput);

				message.setText("Enter your friends mobile number to send "+name+"'s deatil.\n");
				
				alertDialogBuilder.setPositiveButton("Send", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String number = userInput.getText().toString();
						if(number.trim().isEmpty()){
							Toast.makeText(getApplicationContext(), "Enter a valid number", Toast.LENGTH_SHORT).show();
						}else{
							//Send msg to the respective number
							sendSMS(number, messageToSend);
						}
					}
				});
				
				alertDialogBuilder.setNegativeButton("Cancel", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

			}

	 //Sending sms 
	  private void sendSMS(String phoneNumber, String message)
	    {         
	        String SENT = "SMS_SENT";
	        String DELIVERED = "SMS_DELIVERED";
	 
	        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
	            new Intent(SENT), 0);
	 
	        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
	            new Intent(DELIVERED), 0);
	 
	        //---when the SMS has been sent--- 
	        registerReceiver(new BroadcastReceiver(){
	            @Override 
	            public void onReceive(Context arg0, Intent arg1) {
	                switch (getResultCode())
	                { 
	                    case Activity.RESULT_OK:
	                        Toast.makeText(getBaseContext(), "SMS sent", 
	                                Toast.LENGTH_SHORT).show();
	                        break; 
	                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE: 
	                        Toast.makeText(getBaseContext(), "Generic failure", 
	                                Toast.LENGTH_SHORT).show();
	                        break; 
	                    case SmsManager.RESULT_ERROR_NO_SERVICE: 
	                        Toast.makeText(getBaseContext(), "No service", 
	                                Toast.LENGTH_SHORT).show();
	                        break; 
	                    case SmsManager.RESULT_ERROR_NULL_PDU: 
	                        Toast.makeText(getBaseContext(), "Null PDU", 
	                                Toast.LENGTH_SHORT).show();
	                        break; 
	                    case SmsManager.RESULT_ERROR_RADIO_OFF: 
	                        Toast.makeText(getBaseContext(), "Radio off", 
	                                Toast.LENGTH_SHORT).show();
	                        break; 
	                } 
	            } 
	        }, new IntentFilter(SENT));
	 
	        //---when the SMS has been delivered--- 
	        registerReceiver(new BroadcastReceiver(){
	            @Override 
	            public void onReceive(Context arg0, Intent arg1) {
	                switch (getResultCode())
	                { 
	                    case Activity.RESULT_OK:
	                        Toast.makeText(getBaseContext(), "SMS delivered", 
	                                Toast.LENGTH_SHORT).show();
	                        break; 
	                    case Activity.RESULT_CANCELED:
	                        Toast.makeText(getBaseContext(), "SMS not delivered", 
	                                Toast.LENGTH_SHORT).show();
	                        break;                         
	                } 
	            } 
	        }, new IntentFilter(DELIVERED));        
	 
	        SmsManager sms = SmsManager.getDefault(); 
	        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
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
				MainActivity.title.setText("AppHUB");
				MainActivity.subject.setText("home");
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
}
