package com.righthere.efam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class ActivityTalkToUs extends Activity {
	SQLiteDatabase nunuaRahaDatabase;
	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	Gson gson;
	Bundle extras;

	HashMap<String, String> spinnerMapShop;
	List<String> list = new ArrayList<String>();

	TextView app_name;

	LinearLayout layout_progressbar;
	RelativeLayout headersection;
	RelativeLayout centerwrap;
	RelativeLayout footersection;
	Button menuIcon;
	TextView headerText;
	Button shoppingButton;
	Button cartButton;

	Button proceedToCheckout;
	Button clearcart;

	Typeface EkMukta_Light;
	Spinner shop;
	TextView talkToUs;
	TextView call;
	TextView msg;
	TextView email;
	TextView youremail;
	TextView infoqwrap;
	TextView infoq;
	TextView callnot;
	TextView callnot2;
	TextView callnot3;
	TextView pointer;
	Button submitFeedback;
	TextView cartButtonNotification;
	TextView errorMessage;
	Boolean error;

	public static String CUSTOMER_PHONE_NUMBER;
	public static String CUSTOMER_FIRST_NAME;
	public static String CUSTOMER_LAST_NAME;
	public static String CUSTOMER_EMAIL;
	public static String FEEDBACK_SHOP;
	public static String FEEDBACK;
	public static String YOURMAIL;
	private ImageView headerTextimage2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_talk_to_us);
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn
		// animation
		layout_progressbar = (LinearLayout) findViewById(R.id.progressbar_view);
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
		shop = (Spinner) findViewById(R.id.shop);
		call = (TextView) findViewById(R.id.call);
		msg = (TextView) findViewById(R.id.msg);
		infoq = (TextView) findViewById(R.id.infoq);
		callnot = (TextView) findViewById(R.id.callnot);
		callnot2 = (TextView) findViewById(R.id.callnot2);
		callnot3 = (TextView) findViewById(R.id.callnot3);

		infoq = (TextView) findViewById(R.id.infoq);
		email = (TextView) findViewById(R.id.email);
		talkToUs = (TextView) findViewById(R.id.talkToUs);
		youremail = (TextView) findViewById(R.id.youremail);
		pointer = (TextView) findViewById(R.id.pointer);
		submitFeedback = (Button) findViewById(R.id.submitFeedback);
		cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);
		EkMukta_Light = Typeface.createFromAsset(getAssets(),
				"fonts/ek_mukta/EkMukta-Light.ttf");
		errorMessage = (TextView) findViewById(R.id.systemMessage);

		proceedToCheckout = (Button) findViewById(R.id.proceedToCheckout);
		clearcart = (Button) findViewById(R.id.clearcart);

		headerTextimage2 = (ImageView) findViewById(R.id.headerTextimage);
		headerTextimage2.setVisibility(View.VISIBLE);
		headerTextimage2.setImageResource(R.mipmap.menutalktousactive);

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityTalkToUs.this,
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
					Toast.makeText(ActivityTalkToUs.this,
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
					Toast.makeText(ActivityTalkToUs.this,
							"click your cart to confirm first",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ActivityTalkToUs.this,
							"No items in your cart !", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		msg.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent smsIntent = new Intent(Intent.ACTION_VIEW);
				smsIntent.setType("vnd.android-dir/mms-sms");
				smsIntent.putExtra("address", "+254700665180");
				smsIntent.putExtra("sms_body",
						"Body of Message, test MSG from E-Fam app.");
				startActivity(smsIntent);
			}
		});

		call.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:+254700665180"));
				if (ActivityCompat.checkSelfPermission(ActivityTalkToUs.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
					// TODO: Consider calling
					//    ActivityCompat#requestPermissions
					// here to request the missing permissions, and then overriding
					//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
					//                                          int[] grantResults)
					// to handle the case where the user grants the permission. See the documentation
					// for ActivityCompat#requestPermissions for more details.
					return;
				}
				startActivity(callIntent);
			}
		});

		email.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				youremail.setVisibility(View.VISIBLE);
				talkToUs.setVisibility(View.VISIBLE);
				pointer.setVisibility(View.VISIBLE);
				submitFeedback.setVisibility(View.VISIBLE);

			}
		});

		submitFeedback.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				FEEDBACK = talkToUs.getText().toString();
				YOURMAIL = youremail.getText().toString();
				errorMessage.setVisibility(View.GONE);// Hide all errors before
														// displaying new ones
				Boolean error = false;

				if (YOURMAIL == null || YOURMAIL.isEmpty()) {
					errorMessage.setVisibility(View.VISIBLE);
					errorMessage.setText("Please give us your email");
					youremail.requestFocus();
					error = true;
				} else if (FEEDBACK == null || FEEDBACK.isEmpty()) {
					errorMessage.setVisibility(View.VISIBLE);
					errorMessage.setText("Please give us your feedback");
					talkToUs.requestFocus();
					error = true;
				} else if (error == false) {
					sendEmail();
					// FEEDBACK_SHOP = ""
					// editor.putString("customerInfo","Yes").commit();
					// new
					// HttpAsyncTask().execute("http://www.e-fam.com/mobile_trolley_app/savefeedback.php");
				}

			}
		});

		if (sharedPreferences.contains("loggedIn")) {
			CUSTOMER_PHONE_NUMBER = sharedPreferences.getString(
					"userPhoneNumber", null);

			if (sharedPreferences.contains("customerInfo")) {

				if (getIntent().hasExtra("from") == false) {

				}

				new retrieveShopsFromDBTask().execute();

				spinnerMapShop = new HashMap<String, String>();
				spinnerMapShop.put("Select Shop", "0");

				list = new ArrayList<String>();
				list.add("Select Shop");

				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
						this, android.R.layout.simple_spinner_item, list);
				dataAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				shop.setAdapter(dataAdapter);

				new ApplyViewParamsTask().execute();

			} else {
				Intent intent = new Intent(ActivityTalkToUs.this,
						ActivityMyAccountProfile.class);
				intent.putExtra("from", "ActivityTalkToUs");
				startActivity(intent);
			}

		} else {
			Intent intent = new Intent(ActivityTalkToUs.this,
					ActivityMyAccountLogin.class);
			intent.putExtra("from", "ActivityTalkToUs");
			startActivity(intent);
		}

	}

	protected void sendEmail() {
		Log.i("Send email", "");
		String[] TO = { "support@e-fam.com" };
		String[] CC = { "carolynembulwa@gmail.com" };
		Intent emailIntent = new Intent(Intent.ACTION_SEND);

		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.setType("text/plain");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
		emailIntent.putExtra(Intent.EXTRA_CC, CC);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "E-Fam App Contacts Us");
		emailIntent.putExtra(Intent.EXTRA_TEXT, FEEDBACK);

		try {
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			finish();
			//Log.i("Finished sending email...", "");
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(ActivityTalkToUs.this,
					"There is no email client installed.", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		Toast.makeText(
				parent.getContext(),
				"OnItemSelectedListener : "
						+ parent.getItemAtPosition(pos).toString(),
				Toast.LENGTH_SHORT).show();
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

	private class retrieveShopsFromDBTask extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			layout_progressbar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return "Done";
		}

		protected void onPostExecute(String params) {
			layout_progressbar.setVisibility(View.GONE);
			// super.onPostExecute();

			nunuaRahaDatabase = openOrCreateDatabase("nunuaRahaDatabase",
					MODE_PRIVATE, null);

			// GET SHOPS
			Cursor resultsShopsCursor;
			resultsShopsCursor = nunuaRahaDatabase.rawQuery(
					"SELECT * FROM hdjgf_shops_brands WHERE status = 1", null);
			resultsShopsCursor.moveToFirst();
			while (resultsShopsCursor.isAfterLast() == false) {

				String item_id = resultsShopsCursor.getString(0);
				String item_title = resultsShopsCursor.getString(1);

				spinnerMapShop.put(item_title, item_id);
				list.add(item_title);

				resultsShopsCursor.moveToNext();
			}

		}

	}

	private class ApplyViewParamsTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			layout_progressbar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return "Done";
		}

		protected void onPostExecute(String params) {
			layout_progressbar.setVisibility(View.GONE);
			headerText.setText("Talk to Us");

			submitFeedback.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					String selectedShop = shop.getSelectedItem().toString();

					FEEDBACK_SHOP = spinnerMapShop.get(selectedShop);
					FEEDBACK = talkToUs.getText().toString();

					errorMessage.setVisibility(View.GONE);// Hide all errors
															// before displaying
															// new ones
					Boolean error = false;
					/*
					 * if(FEEDBACK_SHOP.equals("0")){
					 * errorMessage.setVisibility(View.VISIBLE);
					 * errorMessage.setText("Select shop"); shop.requestFocus();
					 * error = true; }
					 */
					// else
					if (FEEDBACK == null || FEEDBACK.isEmpty()) {
						errorMessage.setVisibility(View.VISIBLE);
						errorMessage.setText("Please give us your feedback");
						talkToUs.requestFocus();
						error = true;
					} else if (error == false) {
						// FEEDBACK_SHOP = ""
						// editor.putString("customerInfo","Yes").commit();
						new HttpAsyncTask()
								.execute("http://www.e-fam.com/mobile_trolley_app/savefeedback.php");
					}

				}
			});
		}

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

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			layout_progressbar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			layout_progressbar.setVisibility(View.GONE);
			super.onPostExecute(result);

			// Toast.makeText(getBaseContext(), "Pick up options!",
			// Toast.LENGTH_LONG).show();
			try {
				Log.i("Results", " --> " + result);

				JSONArray jsonArr = new JSONArray(result);

				for (int i = 0; i < jsonArr.length(); i++) {
					JSONObject jsonObj = jsonArr.getJSONObject(i);
					// String querytype = jsonObj.getString("querytype");
					// Intent intent = new Intent(ActivityTalkToUs.this,
					// callerClass);
					// startActivity(intent);

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected String doInBackground(String... urls) {
			return GET(urls[0]);
		}

	}

	public static String GET(String url) {
		InputStream inputStream = null;
		String result = "";

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("customer_phone",
				CUSTOMER_PHONE_NUMBER));
		nameValuePairs.add(new BasicNameValuePair("customer_name",
				CUSTOMER_FIRST_NAME + " " + CUSTOMER_LAST_NAME));
		nameValuePairs.add(new BasicNameValuePair("customer_email",
				CUSTOMER_EMAIL));
		nameValuePairs.add(new BasicNameValuePair("shop", FEEDBACK_SHOP));
		nameValuePairs.add(new BasicNameValuePair("feedback", FEEDBACK));

		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// make GET request to the given URL
			HttpResponse response = httpclient.execute(httppost);

			// receive response as inputStream
			inputStream = response.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
			} else {
				result = "Did not work!";
			}

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	public void showDialogEmptyTrolley(final String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityTalkToUs.this);
		builder.setMessage("You will lose all the items that you have selected. "
				+ message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				Intent intent = new Intent(ActivityTalkToUs.this,
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
