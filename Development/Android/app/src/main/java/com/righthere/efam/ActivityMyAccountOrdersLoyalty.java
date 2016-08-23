package com.righthere.efam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
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
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class ActivityMyAccountOrdersLoyalty extends Activity {

	SQLiteDatabase nunuaRahaDatabase;
	public static final String MY_SESSION = "mySession";
	private static final int TIMEOUT_MILLISEC = 0;
	public static String MY_PHONE_NUMBER;
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	Gson gson;
	Bundle extras;
	ArrayAdapter<String> dataAdapter;

	LinearLayout layout_progressbar;
	RelativeLayout headersection;
	RelativeLayout centerwrap;
	RelativeLayout footersection;
	TextView app_name;
	Button menuIcon;
	TextView headerText;
	Button shoppingButton;
	Button cartButton;
	TextView cartButtonNotification;

	Button proceedToCheckout;
	Button clearcart;

	Typeface EkMukta_Light;

	private TextView text1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn
																				// animation
		setContentView(R.layout.loyaltypoints);
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

		cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);

		ImageView headerTextimage2 = (ImageView) findViewById(R.id.headerTextimage);
		headerTextimage2.setVisibility(View.VISIBLE);
		headerTextimage2.setImageResource(R.mipmap.loyaltypointsactive);
		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();

		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityMyAccountOrdersLoyalty.this,
				sharedPreferences);

		clearcart.setVisibility(View.VISIBLE);
		clearcart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (sharedPreferences.contains("myTrolley")) {
					try {
						showDialogEmptyTrolley("Are you sure?");
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					Toast.makeText(ActivityMyAccountOrdersLoyalty.this,
							"No items in your cart to clear!",
							Toast.LENGTH_LONG).show();
				}

			}
		});

		proceedToCheckout.setVisibility(View.VISIBLE);
		proceedToCheckout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Intent intent = new Intent(ActivityMyAccountAddresses.this,
				// ActivityStep09ListCart.class);
				// startActivity(intent);
				if (sharedPreferences.contains("myTrolley")) {
					// Intent intent = new
					// Intent(ActivityMyAccountAddresses.this,
					// ActivityStep09ListDeliveryOptions.class);
					// startActivity(intent);
					Toast.makeText(ActivityMyAccountOrdersLoyalty.this,
							"click your cart to confirm first",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ActivityMyAccountOrdersLoyalty.this,
							"No items in your cart !", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		MY_PHONE_NUMBER = sharedPreferences.getString("userPhoneNumber", null);

		headerText.setText("Loyalty Points");

		// Preparing post params
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("customer_phone", sharedPreferences
				.getString("userPhoneNumber", null)));
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

				// Log.i("Read from server", json.getString("loyaltypoints"));
				// Log.i("Read from server", "You Have" + " " +
				// json.getString("loyaltypoints")+ " "+ "Points");

				text1 = (TextView) findViewById(R.id.name);
				String ponites = "You Have " + json.getString("loyaltypoints")
						+ " Points";
				text1.setText(ponites);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else {
			Log.e("JSON Data", "JSON data error!");
		}

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
																				// animation
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	public void showDialogEmptyTrolley(final String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityMyAccountOrdersLoyalty.this);
		builder.setMessage("You will lose all the items that you have selected. "
				+ message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				Intent intent = new Intent(ActivityMyAccountOrdersLoyalty.this,
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
