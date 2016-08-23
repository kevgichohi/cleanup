package com.righthere.efam;

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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;

public class ActivityMyAccountOrders extends Activity {
	SQLiteDatabase nunuaRahaDatabase;
	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
	public static String MY_PHONE_NUMBER;
	SharedPreferences.Editor editor;
	Gson gson;
	Bundle extras;
	ArrayAdapter<String> dataAdapter;

	TextView app_name;

	LinearLayout layout_progressbar;
	RelativeLayout headersection;
	RelativeLayout ratingForm;
	RelativeLayout centerwrap;
	RelativeLayout footersection;
	Button menuIcon;
	TextView headerText;
	Button shoppingButton;
	Button cartButton;
	TextView cartButtonNotification;

	Button proceedToCheckout;
	Button clearcart;

	Typeface EkMukta_Light;

	private Button title1;
	private Button title2;
	private Button title3;
	private TextView text4;
	private ImageView headerTextimage2;
	private LinearLayout pointerwrapper;
	private TextView pointer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn
																				// animation
		setContentView(R.layout.myaccountorders);
		menuIcon = (Button) findViewById(R.id.menu_icon);
		sharedPreferences = getSharedPreferences(MY_SESSION,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		gson = new Gson();
		extras = getIntent().getExtras();
		headerText = (TextView) findViewById(R.id.headerText);
		app_name = (TextView) findViewById(R.id.app_name);
		shoppingButton = (Button) findViewById(R.id.shoppingButton);
		cartButton = (Button) findViewById(R.id.cartButton);

		proceedToCheckout = (Button) findViewById(R.id.proceedToCheckout);
		clearcart = (Button) findViewById(R.id.clearcart);

		title1 = (Button) findViewById(R.id.title1);
		title2 = (Button) findViewById(R.id.title2);
		title3 = (Button) findViewById(R.id.title3);
		text4 = (TextView) findViewById(R.id.text4);
		cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);

		pointer = (TextView) findViewById(R.id.pointer);

		pointerwrapper = (LinearLayout) findViewById(R.id.pointerwrapper);

		headerText.setText("My Orders");
		headerTextimage2 = (ImageView) findViewById(R.id.headerTextimage);
		headerTextimage2.setVisibility(View.VISIBLE);
		headerTextimage2.setImageResource(R.mipmap.menuordersactive);
		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();

		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityMyAccountOrders.this,
				sharedPreferences);

		clearcart.setVisibility(View.VISIBLE);
		clearcart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (sharedPreferences.contains("myTrolley")) {
					try {
						showDialogEmptyTrolley("Are you sure?");
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					Toast.makeText(ActivityMyAccountOrders.this,
							"No items in your cart to clear!",
							Toast.LENGTH_LONG).show();
				}

			}
		});

		proceedToCheckout.setVisibility(View.VISIBLE);
		proceedToCheckout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Intent intent = new Intent(ActivityMyAccountAddresses.this,
				// ActivityStep09ListCart.class);
				// startActivity(intent);
				if (sharedPreferences.contains("myTrolley")) {
					// Intent intent = new
					// Intent(ActivityMyAccountAddresses.this,
					// ActivityStep09ListDeliveryOptions.class);
					// startActivity(intent);
					Toast.makeText(ActivityMyAccountOrders.this,
							"click your cart to confirm first",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ActivityMyAccountOrders.this,
							"No items in your cart !", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		
		
		MY_PHONE_NUMBER = sharedPreferences.getString("userPhoneNumber", null);

		onBtnClick();
		

	}

	public void onBtnClick() {

		title1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ActivityMyAccountOrders.this,
						ActivityMyAccountNewOrders.class);
				startActivity(intent);

				// text4.setVisibility(View.GONE);
				// pointerwrapper.setVisibility(View.GONE);
				// pointer.setVisibility(View.GONE);
			}

		});

		title2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ActivityMyAccountOrders.this,
						ActivityMyAccountPastOrders.class);
				startActivity(intent);

				// text4.setVisibility(View.GONE);
				// pointerwrapper.setVisibility(View.GONE);
				// pointer.setVisibility(View.GONE);
			}

		});

		title3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(ActivityMyAccountOrders.this,
				// ActivityMyAccountOrdersLoyalty.class);
				// startActivity(intent);

				text4.setVisibility(View.VISIBLE);

				pointer.setVisibility(View.VISIBLE);
				pointerwrapper.setVisibility(View.VISIBLE);

				// Log.i("MY_PHONE_NUMBER", MY_PHONE_NUMBER);

				// Preparing post params
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("customer_phone",
						sharedPreferences.getString("userPhoneNumber", null)));
				// nameValuePairs.add(new
				// BasicNameValuePair("customer_phone",sharedPreferences.getString("userPhoneNumber",
				// null)));

				ServiceHandler serviceClient = new ServiceHandler();
				String jsond = serviceClient
						.makeServiceCall(
								"http://www.e-fam.com/mobile_trolley_app/getmyloyalty.php",
								ServiceHandler.POST, params);

				Log.d("SERVER Request: ", "> " + jsond);

				if (jsond != null) {
					try {
						JSONArray jArray = new JSONArray(jsond);
						JSONObject json = jArray.getJSONObject(0);
						// checking for error node in json

						// Log.i("Read from server",
						// json.getString("loyaltypoints"));
						// Log.i("Read from server", "You Have" + " " +
						// json.getString("loyaltypoints")+ " "+ "Points");

						// text4 = (TextView)findViewById(R.id.text4);
						String ponites = "You have "
								+ json.getString("loyaltypoints") + " points";
						text4.setText(ponites);

					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					Log.e("JSON Data", "JSON data error!");
				}

			}

		});

	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn
																				// animation
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn
																				// animation
	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn
		if (sharedPreferences.contains("customerInfo")) {
		} else {
			
			Intent intent = new Intent(ActivityMyAccountOrders.this,
					ActivityMyAccountLogin.class);
			intent.putExtra("from", "ActivityMyAccountOrders");
			//intent.putExtra("from", "ActivityMyAccountNewOrders");
			startActivity(intent);
		// animation
		}
	}

	public void showDialogEmptyTrolley(final String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityMyAccountOrders.this);
		builder.setMessage("You will lose all the items that you have selected. "
				+ message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				Intent intent = new Intent(ActivityMyAccountOrders.this,
						ActivityStep05ListAisles.class);
				startActivity(intent);

				// Go back to previous page
				// finish();
				dialog.dismiss();
			}
		});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.show();
	}
}