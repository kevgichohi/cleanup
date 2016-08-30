package com.righthere.efam;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.righthere.efam.AnimatedGifImageView.TYPE;
import com.righthere.efam.adapters.DynamicListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class ActivityMyAccountREOrderView extends Activity {
	SQLiteDatabase nunuaRahaDatabase;
	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	Gson gson;

	ArrayList<RequestedResultsReceipt> MYRECEIPT;
	AdapterMyPastREReceipt myadapterreceipt;

	LinearLayout layout_progressbar;
	RelativeLayout headersection;
	RelativeLayout centerwrap;
	RelativeLayout footersection;
	RelativeLayout subtitlesection;
	ListView listView;
	public Integer TOTAL = 0;
	Boolean error;
	Bundle extras;
	Typeface EkMukta_Light;
	TextView app_name;
	TextView headerText;
	Button menuIcon;
	Button shoppingButton;
	Button cartButton;
	Button reOrder;
	TextView shopDetails;
	TextView userName;
	TextView emailAddress;
	TextView phoneNumber;
	TextView delivery;
	Button checkout;
	TextView errorMessage;
	Button backToAisles;
	ImageView shopLogoview;

	Button proceedToCheckout;
	Button clearcart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pastreorderreceipt);
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn
																				// animation
		listView = (ListView) findViewById(R.id.receiptView);
		layout_progressbar = (LinearLayout) findViewById(R.id.progressbar_view);
		headersection = (RelativeLayout) findViewById(R.id.header);
		footersection = (RelativeLayout) findViewById(R.id.footer);
		subtitlesection = (RelativeLayout) findViewById(R.id.subHeader);
		sharedPreferences = getSharedPreferences(MY_SESSION,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		gson = new Gson();
		extras = getIntent().getExtras();
		EkMukta_Light = Typeface.createFromAsset(getAssets(),
				"fonts/ek_mukta/EkMukta-Light.ttf");
		app_name = (TextView) findViewById(R.id.app_name);
		headerText = (TextView) findViewById(R.id.headerText);
		menuIcon = (Button) findViewById(R.id.menu_icon);
		reOrder = (Button) findViewById(R.id.reorder);
		backToAisles = (Button) findViewById(R.id.backToAisles);
		errorMessage = (TextView) findViewById(R.id.errors);
		shopDetails = (TextView) findViewById(R.id.shopDetails);
		userName = (TextView) findViewById(R.id.userName);
		emailAddress = (TextView) findViewById(R.id.email);
		phoneNumber = (TextView) findViewById(R.id.phone);
		delivery = (TextView) findViewById(R.id.delivery);
		shoppingButton = (Button) findViewById(R.id.shoppingButton);
		cartButton = (Button) findViewById(R.id.cartButton);
		shopLogoview = (ImageView) findViewById(R.id.shopLogo);

		cartButton = (Button) findViewById(R.id.cartButton);
		TextView cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);

		proceedToCheckout = (Button) findViewById(R.id.proceedToCheckout);
		clearcart = (Button) findViewById(R.id.clearcart);
		headerText.setText("ReOrder");

		// SET TEXT, APPLY FONTS, SETONCLICKLISTENERS
		new ApplyViewParamsTask().execute();
		// LIST VIEW
		new retrieveFromDBTask().execute();
		MYRECEIPT = new ArrayList<RequestedResultsReceipt>();
		myadapterreceipt = new AdapterMyPastREReceipt(
				ActivityMyAccountREOrderView.this, MYRECEIPT);
		setListViewHeightBasedOnChildren(listView);
		listView.setAdapter(myadapterreceipt);
		boolean resized = DynamicListView.setListViewHeightBasedOnItems(listView);
		Log.d("MAROV_resized", String.valueOf(resized));

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
					Toast.makeText(ActivityMyAccountREOrderView.this,
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
					Toast.makeText(ActivityMyAccountREOrderView.this,
							"click your cart to confirm first",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ActivityMyAccountREOrderView.this,
							"No items in your cart !", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityMyAccountREOrderView.this,
				sharedPreferences);

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

	@TargetApi(Build.VERSION_CODES.KITKAT)
	private class retrieveFromDBTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			layout_progressbar.setVisibility(View.VISIBLE);

			AnimatedGifImageView animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.animatedGifImageView));
			animatedGifImageView.setAnimatedGif(R.mipmap.loading_bar,
					TYPE.FIT_CENTER);
			// animatedGifImageView.setImageResource(R.drawable.loading_bar);
			super.onPreExecute();
		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return "Done";
		}

		protected void onPostExecute(String params) {
			layout_progressbar.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			listView.setDividerHeight(0);
			myadapterreceipt.notifyDataSetChanged();

			String order = extras.getString("order");

			try {
				Integer totalItems = 0;
				JSONObject jsonOrderObject = new JSONObject(order);
				Log.i("Order2", " --> " + jsonOrderObject);
				// Log.i("Order id"," --> "+jsonOrderObject.getString("id"));

				Iterator<String> loop = jsonOrderObject.keys();
				while (loop.hasNext()) {
					String key = loop.next();
					try {
						String orderItems = jsonOrderObject.get(key).toString();
						JSONObject orderItemsObj = new JSONObject(orderItems);

						// Log.i("Order"," --> "+orderItems);
						String item_id = orderItemsObj.getString("id");
						String item_title = orderItemsObj.getString("title");
						String item_size = orderItemsObj.getString("size");
						String item_price = orderItemsObj.getString("price");
						String item_qty = orderItemsObj.getString("qty");

						Integer price = Integer.parseInt(item_price);
						Integer qty = Integer.parseInt(item_qty);
						TOTAL = TOTAL + price * qty;

						totalItems = totalItems + qty;

						RequestedResultsReceipt d = new RequestedResultsReceipt();

						// Cursor resultsImageCursor =
						// nunuaRahaDatabase.rawQuery("SELECT * FROM hdjgf_shops_stocks_brands WHERE id = "
						// + "'" + item_id + "'",null);
						// resultsImageCursor.moveToFirst();

						JSONObject jsonMyCartObject = null;
						if (sharedPreferences.contains("myTrolley")) {
							String jsonMyCartString = sharedPreferences
									.getString("myTrolley", null);
							try {
								jsonMyCartObject = new JSONObject(
										jsonMyCartString);
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

						Log.i("item id", " --> " + item_id);

						try (Cursor resultsImageCursor = nunuaRahaDatabase
								.rawQuery(
										"SELECT * FROM hdjgf_shops_stocks_brands WHERE id = "
												+ item_id, null)) {
							resultsImageCursor.moveToFirst();
							// String[] list_item_ids = new
							// String[resultsImageCursor.getCount()];

							Log.i("resultsImageCursor", " --> "
									+ resultsImageCursor);
							while (resultsImageCursor.isAfterLast() == false) {

								String item_thumbnail_url = resultsImageCursor
										.getString(4);

								Integer item_units_in_cart = 0;

								d.setThumbnailUrl("http://smokesignal.co.ke/mobiletrolley/img/uploads/"
										+ item_thumbnail_url);
								d.setUnits(item_units_in_cart);

								d.item_thumbnail_url = "http://smokesignal.co.ke/mobiletrolley/img/uploads/"
										+ item_thumbnail_url;
								d.item_units_in_cart = item_units_in_cart;

								d.setId(item_id);
								d.setTitle(item_title);
								d.setPrice(item_price);
								d.setSize(item_size);
								d.setUnits(qty);

								d.item_id = item_id;
								d.item_title = item_title;
								d.item_price = item_price;
								d.item_size = item_size;
								d.item_units_in_cart = qty;

								MYRECEIPT.add(d);

								// list_item_ids[k] = item_id;
								Log.i("Order info 1", " --> " + d);
							}
						}

						d.setId(item_id);
						d.setTitle(item_title);
						d.setPrice(item_price);
						d.setSize(item_size);
						d.setUnits(qty);

						d.item_id = item_id;
						d.item_title = item_title;
						d.item_price = item_price;
						d.item_size = item_size;
						d.item_units_in_cart = qty;

						MYRECEIPT.add(d);

						Log.i("Order info 2", " --> " + d);
					} catch (JSONException e) {
						// Something went wrong!
					}

				}

				// TOTAL
				DecimalFormat precision = new DecimalFormat("0.00");
				Double total_double = Double.parseDouble(TOTAL.toString());
				String total_price = precision.format(total_double);

				// VAT AMT
				Integer vat = TOTAL * 16;
				Double vatdouble = Double.parseDouble(vat.toString());
				vatdouble /= 100;

				// VATABLE AMT
				Double vatableAmt = total_double - vatdouble;

				RequestedResultsReceipt d = new RequestedResultsReceipt();
				d.setId("100001");
				d.setTitle("TOTAL");
				d.setPrice(total_price);
				d.setSize("");
				d.setUnits(0);
				d.item_id = "100001";
				d.item_title = "TOTAL";
				d.item_price = total_price;
				d.item_size = "";
				d.item_units_in_cart = 0;
				MYRECEIPT.add(d);

			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.UNSPECIFIED);
		int totalHeight = -10;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new LayoutParams(desiredWidth,
						LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
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
			String customer_name = extras.getString("customer_name");
			String customer_email = extras.getString("customer_email");
			String customer_phone = sharedPreferences.getString(
					"userPhoneNumber", null);
			String customer_address = extras.getString("customer_address");
			String order_ref = extras.getString("orderRefNumber");
			String order_date = extras.getString("orderDate");
			String outlet = extras.getString("orderOutlet");
			String brand = extras.getString("orderBrand");
			String branch = extras.getString("orderBranch");

			// DOWNLOAD OUTLET LOGO
			// ImageDownloader imageDownloader = new ImageDownloader();
			// imageDownloader.download(SELECTED_BRAND_LOGO, shopLogoview);

			shopDetails.setText(outlet);
			userName.setText(customer_name);
			emailAddress.setText(customer_email);
			phoneNumber.setText(" - " + customer_phone);
			delivery.setText(customer_address + "\n\nOrder # " + order_ref
					+ "\n" + order_date);

		}

	}

	public void showDialogEmptyTrolley(final String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityMyAccountREOrderView.this);
		builder.setMessage("You will lose all the items that you have selected. "
				+ message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				Intent intent = new Intent(ActivityMyAccountREOrderView.this,
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
