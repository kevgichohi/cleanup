package com.righthere.efam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class ActivityMyAccountOrderView_Copy extends Activity {
	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	Gson gson;
	ArrayList<RequestedSimpleListOrders> MYORDERS;
	AdapterListOrders myadapter;

	ListView listView;
	LinearLayout layout_progressbar;
	RelativeLayout loginForm;
	RelativeLayout ratingForm;
	Button menuIcon;
	TextView headerText;
	Button shoppingButton;
	Button cartButton;
	TextView app_name;
	TextView cartButtonNotification;
	public static String ORDER_ID;
	public static String ORDER_REF_NUMBER;
	public static String ORDER_DATE;
	public static String ORDER_OUTLET;
	public static String ORDER_AMOUNT;
	public static Typeface FONT_EKMUKTA_LIGHT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scroller_simple_list);
		listView = (ListView) findViewById(R.id.simpleListView);
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn
																				// animation
		layout_progressbar = (LinearLayout) findViewById(R.id.progressbar_view);
		loginForm = (RelativeLayout) findViewById(R.id.loginForm);
		ratingForm = (RelativeLayout) findViewById(R.id.ratingForm);
		menuIcon = (Button) findViewById(R.id.menu_icon);
		sharedPreferences = getSharedPreferences(MY_SESSION,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		gson = new Gson();
		FONT_EKMUKTA_LIGHT = Typeface.createFromAsset(getAssets(),
				"fonts/ek_mukta/EkMukta-Light.ttf");
		cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);
		headerText = (TextView) findViewById(R.id.headerText);
		app_name = (TextView) findViewById(R.id.app_name);
		shoppingButton = (Button) findViewById(R.id.shoppingButton);
		cartButton = (Button) findViewById(R.id.cartButton);

		Bundle extras = getIntent().getExtras();
		ORDER_ID = extras.getString("orderId");
		ORDER_REF_NUMBER = extras.getString("orderRefNumber");
		ORDER_DATE = extras.getString("orderDate");
		ORDER_OUTLET = extras.getString("orderOutlet");
		ORDER_AMOUNT = extras.getString("orderAmount");

		headerText.setText("Order #" + ORDER_REF_NUMBER);

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityMyAccountOrderView_Copy.this,
				sharedPreferences);

		// Call AsynTask to perform network operation on separate thread
		new HttpAsyncTask()
				.execute("http://www.e-fam.com/mobile_trolley_app/getorder.php");

		// LIST VIEW
		MYORDERS = new ArrayList<RequestedSimpleListOrders>();
		myadapter = new AdapterListOrders(ActivityMyAccountOrderView_Copy.this,
				MYORDERS, layout_progressbar, ratingForm);
		listView.setDividerHeight(0); // remove the default divider line
		listView.setAdapter(myadapter);
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				RequestedSimpleListOrders item = (RequestedSimpleListOrders) listView
						.getItemAtPosition(position);

				// ADD TO SESSION
				editor.putString("order_date", item.item_one);
				editor.putString("order_shop", item.item_two);
				editor.putString("order_amount", item.item_three);
				editor.commit();

				Intent intent = new Intent(
						ActivityMyAccountOrderView_Copy.this,
						ActivityStep09ListDeliveryOptions.class);
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_test, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
		// layout_progressbar.setVisibility(View.GONE);
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

			Toast.makeText(getBaseContext(), "Order Received!",
					Toast.LENGTH_LONG).show();

			try {

				JSONArray jsonArr = new JSONArray(result);
				JSONObject jsonObj = jsonArr.getJSONObject(0);
				String user_order = jsonObj.getString("user_order");

				List[] jsonOrderList = gson.fromJson(user_order, List[].class);
				List getjsonOrderListCont = Arrays.asList(jsonOrderList);

				Object getOrderObject = getjsonOrderListCont.get(0);
				ArrayList orderList = (ArrayList) getOrderObject;

				String[] list_item_ids = new String[orderList.size()];
				String[] list_item_titles = new String[orderList.size()];

				for (int i = 0; i < orderList.size(); i++) {
					Object cartItem = orderList.get(i);
					ArrayList orderItemInfo = (ArrayList) cartItem;

					String item_id = orderItemInfo.get(0).toString();
					String item_title = orderItemInfo.get(1).toString();
					String item_price = orderItemInfo.get(2).toString();
					String item_size = orderItemInfo.get(3).toString();
					String item_thumbnail_url = orderItemInfo.get(4).toString();

					list_item_ids[i] = item_id;
					list_item_titles[i] = item_title;

					RequestedSimpleListOrders d = new RequestedSimpleListOrders();

					d.setItemId(item_id);
					// d.setItemStatus(item_status);
					d.setItemOne(item_title);
					d.setItemTwo(item_size);
					d.setItemThree(item_price);

					d.item_id = item_id;
					// d.item_status = item_status;
					d.item_one = item_title;
					d.item_two = item_size;
					d.item_three = item_price;

					MYORDERS.add(d);

					Log.i("user_order", " --> " + item_title);
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
		nameValuePairs.add(new BasicNameValuePair("order_id", ORDER_ID));

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

}
