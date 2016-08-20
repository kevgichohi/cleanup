package com.redhering.nunuaraha;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.redhering.nunuaraha.R;

public class ActivityMyAccount extends Activity {
	public static final String MY_SESSION = "mySession";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
	
	TextView app_name;
	LinearLayout layout_progressbar;
	ImageView menuIcon;
	TextView headerText;
	Button homeButton;
	Button accountButton;
	Button shoppingButton;
	Button cartButton;
	Button myOrders;
	Button accountInfo;
    public static Typeface FONT_EKMUKTA_LIGHT;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);
		layout_progressbar = (LinearLayout) findViewById(R.id.progressbar_view);
		menuIcon  = (ImageView) findViewById(R.id.menu_icon);
		sharedPreferences = getSharedPreferences(MY_SESSION,Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		gson = new Gson();
		FONT_EKMUKTA_LIGHT = Typeface.createFromAsset(getAssets(),"fonts/ek_mukta/EkMukta-Light.ttf");
  		headerText = (TextView) findViewById(R.id.headerText);
  		app_name = (TextView) findViewById(R.id.app_name);
  		homeButton = (Button) findViewById(R.id.homeButton);
  		accountButton = (Button) findViewById(R.id.accountButton);
  		shoppingButton = (Button) findViewById(R.id.shoppingButton);
  		cartButton = (Button) findViewById(R.id.cartButton);
  		myOrders = (Button) findViewById(R.id.myOrders);
  		accountInfo = (Button) findViewById(R.id.accountInfo);
  		
	    
	    //SET TEXT, APPLY FONTS, SETONCLICKLISTENERS
  		new ApplyViewParamsTask().execute();
        
	}
	
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	@Override
	public void onResume() {
		super.onResume();  // Always call the superclass method first
		
		//SET NOTIFICATION FOR THE NUMBER OF ITEMS CURRENTLY IN THE TROLLEY
		TextView cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);
		if(sharedPreferences.contains("myTrolley")) {
			String jsonMyCartString = sharedPreferences.getString("myTrolley", null);
			JSONObject jsonMyCartObject;
			try {
				jsonMyCartObject = new JSONObject(jsonMyCartString);
				Integer numberOfItemsInMyTrolley = 0;
				Iterator<String> loop = jsonMyCartObject.keys();
			    while (loop.hasNext()) {
			        String key = loop.next();
			        try {
			            String unitsValue = jsonMyCartObject.getString(key);
			            numberOfItemsInMyTrolley = numberOfItemsInMyTrolley + Integer.parseInt(unitsValue);
			        } catch (JSONException e) {
			            // Something went wrong!
			        }
			    }
			    cartButtonNotification.setText(numberOfItemsInMyTrolley.toString());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else{
			cartButtonNotification.setText("0");
	  		cartButton.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	Toast.makeText(getBaseContext(), "Your cart is empty!", Toast.LENGTH_LONG).show();
	            }
	        });
		}
	}
	
	private class ApplyViewParamsTask extends AsyncTask<String, Void, String> {
		
		@Override
	    protected void onPreExecute() {
	        layout_progressbar.setVisibility(View.VISIBLE);
	        super.onPreExecute();
	    }
		
		protected String doInBackground(String...params) {
			// TODO Auto-generated method stub
			return "Done";
		}
		
	    protected void onPostExecute(String params) {
			layout_progressbar.setVisibility(View.GONE);
  			menuIcon.setImageResource(R.drawable.ic_account_grey);
	  		headerText.setText("My Account");
			app_name.setTypeface(FONT_EKMUKTA_LIGHT);
	  		accountButton.setTextColor(getResources().getColor(R.color.green));
	  		
	  		//SET ONCLICKLISTENER
	  		myOrders.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	Intent intent = new Intent(ActivityMyAccount.this, ActivityMyAccountOrders.class);
		    	    startActivity(intent);
	                
	            }
	        }); 
	  		
	  		accountInfo.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	Intent intent = new Intent(ActivityMyAccount.this, ActivityMyAccountProfile.class);
		    	    startActivity(intent);
	                
	            }
	        }); 
	  		

	  		homeButton.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	Intent intent = new Intent(ActivityMyAccount.this, ActivityMain.class);
		    	    startActivity(intent);
	                
	            }
	        }); 
	  		
	  		shoppingButton.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	Intent intent = new Intent(ActivityMyAccount.this, ActivityStep01ListOutlets.class);
	            	if(sharedPreferences.contains("myTrolley")) {
	            		intent = new Intent(ActivityMyAccount.this, ActivityStep05ListAisles.class);
	            	}
		    	    startActivity(intent);
	            }
	        }); 
	  		
	  		
	  		//SET NOTIFICATION FOR THE NUMBER OF ITEMS CURRENTLY IN THE TROLLEY
			TextView cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);
			if(sharedPreferences.contains("myTrolley")) {
				String jsonMyCartString = sharedPreferences.getString("myTrolley", null);
				JSONObject jsonMyCartObject;
				try {
					jsonMyCartObject = new JSONObject(jsonMyCartString);
					Integer numberOfItemsInMyTrolley = 0;
					Iterator<String> loop = jsonMyCartObject.keys();
				    while (loop.hasNext()) {
				        String key = loop.next();
				        try {
				            String unitsValue = jsonMyCartObject.getString(key);
				            numberOfItemsInMyTrolley = numberOfItemsInMyTrolley + Integer.parseInt(unitsValue);
				        } catch (JSONException e) {
				            // Something went wrong!
				        }
				    }
				    cartButtonNotification.setText(numberOfItemsInMyTrolley.toString());
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				cartButton.setOnClickListener(new View.OnClickListener() {
    	            @Override
    	            public void onClick(View v) {
    	            	Intent intent = new Intent(ActivityMyAccount.this, ActivityStep08ListCart.class);
    		    	    startActivity(intent);
    	                
    	            }
    	        });
			}else{
				cartButtonNotification.setText("0");
    	  		cartButton.setOnClickListener(new View.OnClickListener() {
    	            @Override
    	            public void onClick(View v) {
    	            	Toast.makeText(getBaseContext(), "Your cart is empty!", Toast.LENGTH_LONG).show();
    	            }
    	        });
			}

	    }
	    
	 }
}
