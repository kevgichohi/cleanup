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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.righthere.efam.AnimatedGifImageView.TYPE;

public class ActivityMyAccountPastOrders extends Activity {

	ListView listView;

	LinearLayout layout_progressbar;
	RelativeLayout ratingForm;

	RelativeLayout headersection;
	RelativeLayout centerwrap;
	RelativeLayout footersection;

	Button menuIcon;
	TextView headerText;
	Button shoppingButton;
	Button cartButton;
	TextView app_name;
	Button rateButton;
	Button cancelRatingButton;
	TextView cartButtonNotification;

	Button proceedToCheckout;
	Button clearcart;

	TextView queryOne;
	TextView queryTwo;
	TextView queryThree;
	TextView queryFour;

	RadioGroup query1RadioGroup;
	RadioGroup query2RadioGroup;
	RadioGroup query3RadioGroup;

	RadioButton query1SelectedRadio;
	RadioButton query2SelectedRadio;
	RadioButton query3SelectedRadio;

	public static String RATE_VALUES;

	ArrayList<RequestedSimpleListOrders> MYORDERS;
	AdapterListPastOrders myadapter;

	public static final String MY_SESSION = "mySession";
	public static SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	public static String MY_PHONE_NUMBER;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scroller_simple_list);
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn
																				// animation
		listView = (ListView) findViewById(R.id.simpleListView);
		layout_progressbar = (LinearLayout) findViewById(R.id.progressbar_view);
		ratingForm = (RelativeLayout) findViewById(R.id.ratingForm);
		menuIcon = (Button) findViewById(R.id.menu_icon);
		sharedPreferences = getSharedPreferences(MY_SESSION,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		headerText = (TextView) findViewById(R.id.headerText);
		app_name = (TextView) findViewById(R.id.app_name);
		shoppingButton = (Button) findViewById(R.id.shoppingButton);
		cartButton = (Button) findViewById(R.id.cartButton);
		cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);

		proceedToCheckout = (Button) findViewById(R.id.proceedToCheckout);
		clearcart = (Button) findViewById(R.id.clearcart);

		rateButton = (Button) findViewById(R.id.rateButton);
		cancelRatingButton = (Button) findViewById(R.id.cancelRatingButton);
		queryOne = (TextView) findViewById(R.id.queryOne);
		queryTwo = (TextView) findViewById(R.id.queryTwo);
		queryThree = (TextView) findViewById(R.id.queryThree);

		query1RadioGroup = (RadioGroup) findViewById(R.id.queryOneRadioGroup);
		query2RadioGroup = (RadioGroup) findViewById(R.id.queryTwoRadioGroup);
		query3RadioGroup = (RadioGroup) findViewById(R.id.queryThreeRadioGroup);

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityMyAccountPastOrders.this,
				sharedPreferences);

		ImageView headerTextimage2 = (ImageView) findViewById(R.id.headerTextimage);
		headerTextimage2.setVisibility(View.VISIBLE);
		headerTextimage2.setImageResource(R.mipmap.menuordersactive);

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
					Toast.makeText(ActivityMyAccountPastOrders.this,
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
					Toast.makeText(ActivityMyAccountPastOrders.this,
							"click your cart to confirm first",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ActivityMyAccountPastOrders.this,
							"No items in your cart !", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		// SET TEXT, APPLY FONTS, SETONCLICKLISTENERS
		new ApplyViewParamsTask().execute();
		if (sharedPreferences.contains("customerInfo")) {

		if (sharedPreferences.contains("userPhoneNumber")) {
			MY_PHONE_NUMBER = sharedPreferences.getString("userPhoneNumber",
					null);

			// Call AsynTask to perform network operation on separate thread
			new HttpAsyncTask()
					.execute("http://www.e-fam.com/mobile_trolley_app/getmypastorders.php");

			// LIST VIEW
			MYORDERS = new ArrayList<RequestedSimpleListOrders>();
			myadapter = new AdapterListPastOrders(
					ActivityMyAccountPastOrders.this, MYORDERS,
					layout_progressbar, ratingForm);
			listView.setAdapter(myadapter);
			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					RequestedSimpleListOrders item = (RequestedSimpleListOrders) listView
							.getItemAtPosition(position);

					Log.i("Orders", " --> " + MYORDERS);
					// ADD TO SESSION
					editor.putString("order_date", item.item_one);
					editor.putString("order_item_ref_number",
							item.item_ref_number);
					editor.putString("order_shop", item.item_two);
					editor.putString("order_amount", item.item_three);
					editor.putString("order_deliveryprocess", item.item_four);
					editor.commit();

					Intent intent = new Intent(
							ActivityMyAccountPastOrders.this,
							ActivityMyAccountPastOrders.class);
					intent.putExtra("from", "ActivityMyAccountPastOrders");
					startActivity(intent);
				}
			});
		} else {
			Intent intent = new Intent(ActivityMyAccountPastOrders.this,
					ActivityMyAccountLogin.class);
			intent.putExtra("from", "ActivityMyAccountPastOrders");
			startActivity(intent);
		}
		
		} else {
			Intent intent = new Intent(ActivityMyAccountPastOrders.this,
					ActivityMyAccountLogin.class);
			intent.putExtra("from", "ActivityMyAccountNewOrders");
			startActivity(intent);
		}
		
		

	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first

		myadapter = new AdapterListPastOrders(ActivityMyAccountPastOrders.this,
				MYORDERS, layout_progressbar, ratingForm);
		listView.setAdapter(myadapter);
		myadapter.notifyDataSetChanged();

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityMyAccountPastOrders.this,
				sharedPreferences);
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

			AnimatedGifImageView animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.animatedGifImageView));
			animatedGifImageView.setAnimatedGif(R.mipmap.loading_bar,
					TYPE.FIT_CENTER);
			// animatedGifImageView.setImageResource(R.drawable.loading_bar);
			super.onPreExecute();
			listView.setVisibility(View.GONE);
			super.onPreExecute();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			layout_progressbar.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			myadapter.notifyDataSetChanged();
			super.onPostExecute(result);

			Log.i("Orders", " --> " + result);

			Toast.makeText(getBaseContext(), "Orders Received!",
					Toast.LENGTH_LONG).show();
			Log.i("Orders", " --> " + result);

			if (result.equals("")) {
				RequestedSimpleListOrders d = new RequestedSimpleListOrders();

				d.setItemId("");
				d.setItemRefNumber("");
				d.setItemStatus("");
				d.setItemRate("");
				d.setItemOne("No orders found!");
				d.setItemTwo("");
				d.setItemThree("");
				d.setItemFour("");

				d.item_id = "";
				d.item_ref_number = "";
				d.item_status = "";
				d.item_rate = "";
				d.item_one = "No orders found!";
				d.item_two = "";
				d.item_three = "";
				d.item_four = "";

				MYORDERS.add(d);

			} else {

				Log.i("Orders", " --> " + result);

				try {
					JSONArray jsonArr = new JSONArray(result);
					String[] list_item_ids = new String[jsonArr.length()];
					String[] list_item_titles = new String[jsonArr.length()];

					for (int i = 0; i < jsonArr.length(); i++) {
						JSONObject jsonObj = jsonArr.getJSONObject(i);
						String item_id = jsonObj.getString("id");
						String item_ref_number = jsonObj
								.getString("ref_number");
						String item_status = jsonObj.getString("status");
						String item_rate = jsonObj.getString("rate");
						String date = jsonObj.getString("created");
						String outlet = jsonObj.getString("outlet_id");
						String brand = jsonObj.getString("shop_brand_id1");
						String branch = jsonObj.getString("shop_branch_id1");
						String outlet2 = jsonObj.getString("shop_brand_id");
						String branch2 = jsonObj.getString("shop_branch_id");
						String amount = jsonObj.getString("subtotal");
						String delivery_process = jsonObj
								.getString("delivery_process");
						String item_customer_order = jsonObj
								.getString("user_order");
						String item_customer_name = jsonObj
								.getString("first_name")
								+ " "
								+ jsonObj.getString("last_name");
						String item_customer_email = jsonObj.getString("email");
						String item_customer_address = jsonObj
								.getString("customer_address");

						list_item_ids[i] = item_id;
						list_item_titles[i] = date;

						RequestedSimpleListOrders d = new RequestedSimpleListOrders();

						d.setItemId(item_id);
						d.setItemRefNumber(item_ref_number);
						d.setItemStatus(item_status);
						d.setItemRate(item_rate);
						d.setItemOne(date);
						d.setItemTwo(brand + " " + branch);
						d.setItemOutlet(outlet2);
						d.setItemBranchid(branch2);
						d.setItemBrandId(brand);
						d.setItemThree(amount);
						d.setItemFour(delivery_process);
						d.setCustomerOrder(item_customer_order);
						d.setCustomerName(item_customer_name);
						d.setCustomerEmail(item_customer_email);
						d.setCustomerAddress(item_customer_address);

						d.item_id = item_id;
						d.item_ref_number = item_ref_number;
						d.item_status = item_status;
						d.item_rate = item_rate;
						d.item_one = date;
						d.item_two = brand + " " + branch;
						d.item_outletid = outlet2;
						d.item_branchid = branch2;
						d.item_brandid = brand;
						d.item_three = amount;
						d.item_four = delivery_process;
						d.item_customer_order = item_customer_order;
						d.item_customer_name = item_customer_name;
						d.item_customer_email = item_customer_email;
						d.item_customer_address = item_customer_address;

						Log.i("outlet2", " --> " + outlet2);
						Log.i("branch2", " --> " + branch2);
						Log.i("brand", " --> " + brand);
						Log.i("branch",
								" --> " + jsonObj.getString("shop_branch_id"));

						editor.putString("newbranchid",
								jsonObj.getString("shop_branch_id")).commit();
						editor.putString("newbrandid",
								jsonObj.getString("shop_brand_id")).commit();
						editor.putString("newoutletid",
								jsonObj.getString("outlet_id")).commit();
						editor.putString("newbranch", brand + " " + branch)
								.commit();

						MYORDERS.add(d);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					RequestedSimpleListOrders d = new RequestedSimpleListOrders();

					d.setItemId("");
					d.setItemRefNumber("");
					d.setItemStatus("");
					d.setItemRate("");
					d.setItemOne("No past orders.");
					d.setItemTwo("");
					//d.setItemTwo("Please contact customer care for assistance.");
					d.setItemThree("");
					d.setItemFour("");

					d.item_id = "";
					d.item_ref_number = "";
					d.item_status = "";
					d.item_rate = "";
					d.item_one = "No past orders.";
					d.item_two = "";
					d.item_three = "";
					d.item_four = "";

					MYORDERS.add(d);
				}
			}
		}

		@Override
		protected String doInBackground(String... urls) {

			return GET(urls[0]);
		}
	}

	private class ApplyViewParamsTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return "Done";
		}

		protected void onPostExecute(String params) {
			// MENU ICON
			headerText.setText("My Past Orders");

			rateButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					String queryOneText = queryOne.getText().toString();
					String queryTwoText = queryTwo.getText().toString();
					String queryThreeText = queryThree.getText().toString();

					String radiovalue1 = "0";
					if (query1RadioGroup.getCheckedRadioButtonId() != -1) {
						query1SelectedRadio = (RadioButton) findViewById(query1RadioGroup
								.getCheckedRadioButtonId());
						radiovalue1 = query1SelectedRadio.getText().toString();
					}

					String radiovalue2 = "0";
					if (query2RadioGroup.getCheckedRadioButtonId() != -1) {
						query2SelectedRadio = (RadioButton) findViewById(query3RadioGroup
								.getCheckedRadioButtonId());
						radiovalue2 = query2SelectedRadio.getText().toString();
					}

					String radiovalue3 = "0";
					if (query3RadioGroup.getCheckedRadioButtonId() != -1) {
						query3SelectedRadio = (RadioButton) findViewById(query3RadioGroup
								.getCheckedRadioButtonId());
						radiovalue3 = query3SelectedRadio.getText().toString();
					}

					JSONObject ratingValues = new JSONObject();
					try {
						ratingValues.put(queryOneText, radiovalue1);
						ratingValues.put(queryTwoText, radiovalue2);
						ratingValues.put(queryThreeText, radiovalue3);

						RATE_VALUES = ratingValues.toString();
						new HttpAsyncRatingTask()
								.execute("http://www.e-fam.com/mobile_trolley_app/saverating.php");// Call
																											// AsynTask
																											// to
																											// perform
																											// network
																											// operation
																											// on
																											// separate
																											// thread

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			cancelRatingButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Reload page
					startActivity(new Intent(ActivityMyAccountPastOrders.this,
							ActivityMyAccountPastOrders.class));
				}
			});

		}

	}

	private class HttpAsyncRatingTask extends AsyncTask<String, Void, String> {
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

			ratingForm.setVisibility(View.GONE);
			Toast.makeText(getBaseContext(), "Thank You!", Toast.LENGTH_LONG)
					.show();

			// Reload page
			startActivity(new Intent(ActivityMyAccountPastOrders.this,
					ActivityMyAccountPastOrders.class));
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
		// PARAMS FOR RETRIEVING ORDERS
		nameValuePairs.add(new BasicNameValuePair("phone_number",
				MY_PHONE_NUMBER));

		// PARAMS FOR SAVING USER RATE/REVIEW
		nameValuePairs.add(new BasicNameValuePair("rating", RATE_VALUES));
		nameValuePairs.add(new BasicNameValuePair("customer_fname",
				sharedPreferences.getString("userFirstName", null)));
		nameValuePairs.add(new BasicNameValuePair("customer_lname",
				sharedPreferences.getString("userLastName", null)));
		nameValuePairs.add(new BasicNameValuePair("customer_email",
				sharedPreferences.getString("userEmail", null)));
		nameValuePairs.add(new BasicNameValuePair("customer_phone",
				sharedPreferences.getString("userPhoneNumber", null)));

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
				ActivityMyAccountPastOrders.this);
		builder.setMessage("You will lose all the items that you have selected. "
				+ message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				Intent intent = new Intent(ActivityMyAccountPastOrders.this,
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
