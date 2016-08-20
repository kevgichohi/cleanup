package com.redhering.nunuaraha;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.StringEntity;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract.Document;
import android.renderscript.Element;

import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.redhering.nunuaraha.R;

public class ActivityStep10ListPaymentOptions<NameValuePair> extends Activity {
	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
	public static ArrayList<RequestedResultsSimpleList> DELIVERYOPTIONS;
	private static ArrayList<RequestedResultsReceipt> myReceipt;
	AdapterSimpleList myadapter;
	
	LinearLayout layout_progressbar;
	RelativeLayout headersection;
	public static String SELECTED_BRANCH_ID;
	public static String SELECTED_BRANCH_TITLE;
	public static String SELECTED_BRAND_ID;
	public static String SELECTED_BRAND_TITLE;
	public static String SELECTED_BRAND_LOGO;
	public static String SELECTED_OUTLET_ID;
	public static String SELECTED_OUTLET_TITLE;
	public static String USER_PHONE_NUMBER;
	Typeface EkMukta_Light;
	TextView app_name;
	Button menuIcon;
	TextView optionPayCash;
	TextView optionPayOnline;
	TextView cartButtonNotification;
	TextView headerText;
	Button shoppingButton;
	Button cartButton;
	ImageView shopLogoview;
    String extStorageDirectory;
    Bitmap bm;

	private	ImageView headerTextimage2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_options);
		overridePendingTransition(R.anim.slide_page_in,R.anim.slide_page_out);//SlideIn animation
		initViews();
  		
  		if(sharedPreferences.contains("loggedIn") && sharedPreferences.contains("customerInfo")) {
  			headerText.setText("Payment Options");
  			
			headerTextimage2 = (ImageView) findViewById(R.id.headerTextimage);
			headerTextimage2.setVisibility(View.VISIBLE);
			headerTextimage2.setImageResource(R.drawable.paymentmode);
			//LOAD QUICKLINKS
	  		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
	  		helperQuickLinks.create(menuIcon,shoppingButton,cartButton,cartButtonNotification,ActivityStep10ListPaymentOptions.this,sharedPreferences);
	  		new ApplyViewParamsTask().execute();

			//DISPLAY OUTLET LOGO
	        File file = new File(extStorageDirectory+"/NunuaRaha/", SELECTED_BRAND_LOGO);
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
	        //shopLogoview.setImageBitmap(bitmap);
  			
  		}else{
  			Intent intent = new Intent(ActivityStep10ListPaymentOptions.this, ActivityMyAccountLogin.class);
  			intent.putExtra("from" , "ActivityStep09ListDeliveryOptions");
    	    startActivity(intent);
  		}
	}
	
	private void initViews() {
		extStorageDirectory = Environment.getExternalStorageState().toString();
  	    extStorageDirectory = Environment.getExternalStorageDirectory().toString();
  	    
  	    layout_progressbar = (LinearLayout) findViewById(R.id.progressbar_view);
	    headersection = (RelativeLayout) findViewById(R.id.header);
	    sharedPreferences = getSharedPreferences(MY_SESSION,Context.MODE_PRIVATE);
	    editor = sharedPreferences.edit();
	    SELECTED_OUTLET_ID = sharedPreferences.getString("selectedOutletId", null);
	    SELECTED_OUTLET_TITLE = sharedPreferences.getString("selectedOutlet", null);
	    SELECTED_BRAND_ID = sharedPreferences.getString("selectedBrandId", null);
		SELECTED_BRAND_TITLE = sharedPreferences.getString("selectedBrand", null);
		SELECTED_BRAND_LOGO = sharedPreferences.getString("selectedBrandLogo", null);
		SELECTED_BRANCH_ID = sharedPreferences.getString("selectedBranchId", null);
		SELECTED_BRANCH_TITLE = sharedPreferences.getString("selectedBranch", null);
		USER_PHONE_NUMBER = sharedPreferences.getString("userPhoneNumber", null);
		EkMukta_Light = Typeface.createFromAsset(getAssets(),"fonts/ek_mukta/EkMukta-Light.ttf");
		app_name = (TextView) findViewById(R.id.app_name);
		menuIcon  = (Button) findViewById(R.id.menu_icon);
		headerText = (TextView) findViewById(R.id.headerText);
		shoppingButton = (Button) findViewById(R.id.shoppingButton);
		cartButton = (Button) findViewById(R.id.cartButton);
		cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);
		optionPayCash = (TextView) findViewById(R.id.optionPayCash);
		optionPayOnline = (TextView) findViewById(R.id.optionPayOnline);
		shopLogoview = (ImageView) findViewById(R.id.shopLogo);
	}
	
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_page_in,R.anim.slide_page_out);//SlideIn animation
	}
	
	@Override
	public void finish() {
	    super.finish();
		overridePendingTransition(R.anim.slide_page_in,R.anim.slide_page_out);//SlideIn animation
	}
	
	@Override
	public void onResume() {
		super.onResume();  // Always call the superclass method first
		overridePendingTransition(R.anim.slide_page_in,R.anim.slide_page_out);//SlideIn animation
		
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
	  		
			optionPayCash.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
					editor.putString("paymentOption","Cash").commit();
                	Intent intent = new Intent(ActivityStep10ListPaymentOptions.this, ActivityStep11MyReceipt.class);
		    	    startActivity(intent);
                }
            });
	  		
			optionPayOnline.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	editor.putString("paymentOption","Online").commit();
                	Intent intent = new Intent(ActivityStep10ListPaymentOptions.this, ActivityStep11MyReceipt.class);
					startActivity(intent);
				}
			});
	    
	 
	    }
	}
}
