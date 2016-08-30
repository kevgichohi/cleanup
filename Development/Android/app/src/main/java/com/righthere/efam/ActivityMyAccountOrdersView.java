package com.righthere.efam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
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
import com.righthere.efam.adapters.DynamicListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class ActivityMyAccountOrdersView extends Activity {
	SQLiteDatabase nunuaRahaDatabase;
	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	Gson gson;

	ArrayList<RequestedResultsReceipt> MYRECEIPT;
	AdapterMyReceipt myadapterreceipt;

	LinearLayout layout_progressbar;
	RelativeLayout headersection;
	RelativeLayout centerwrap;
	RelativeLayout footersection;
	RelativeLayout subtitlesection;
	ListView listView;

	// public Integer TOTAL = 0;
	public static String Ovaraltatol;
	public static String totalitem2;
	public static String vatrate2;
	public static String vatable2;
	public static String vatamt2;

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
	Button checkout;
	TextView errorMessage;
	Button backToAisles;
	ImageView shopLogoview;

	Button proceedToCheckout;
	Button clearcart;

	TextView totalovaraltext;
	TextView totalitem;
	TextView vatrate;
	TextView vatable;
	TextView vatamt;
	TextView delivery;
	TextView deliveryinfo;
	TextView service;
	TextView reciepttotal;
	TextView reciepttotaltext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pastreceipt);
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
		shoppingButton = (Button) findViewById(R.id.shoppingButton);
		cartButton = (Button) findViewById(R.id.cartButton);
		shopLogoview = (ImageView) findViewById(R.id.shopLogo);

		proceedToCheckout = (Button) findViewById(R.id.proceedToCheckout);
		clearcart = (Button) findViewById(R.id.clearcart);

		TextView cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);

		totalovaraltext = (TextView) findViewById(R.id.totalovaraltext);
		totalitem = (TextView) findViewById(R.id.totalitem);
		vatrate = (TextView) findViewById(R.id.vatrate);
		vatable = (TextView) findViewById(R.id.vatable);
		vatamt = (TextView) findViewById(R.id.vatamt);
		delivery = (TextView) findViewById(R.id.delivery);
		service = (TextView) findViewById(R.id.service);
		deliveryinfo = (TextView) findViewById(R.id.deliveryinfo);
		reciepttotal = (TextView) findViewById(R.id.reciepttotal);
		reciepttotaltext = (TextView) findViewById(R.id.reciepttotaltext);

		// totalitem2 = sharedPreferences.getString("totalitem", null);
		// vatrate2 = sharedPreferences.getString("vatrate", null);
		// vatable2 = sharedPreferences.getString("vatable", null);
		// vatamt2 = sharedPreferences.getString("vatamt", null);

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityMyAccountOrdersView.this,
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
					Toast.makeText(ActivityMyAccountOrdersView.this,
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
					Toast.makeText(ActivityMyAccountOrdersView.this,
							"click your cart to confirm first",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ActivityMyAccountOrdersView.this,
							"No items in your cart !", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		ImageView headerTextimage2 = (ImageView) findViewById(R.id.headerTextimage);
		headerTextimage2.setVisibility(View.VISIBLE);
		headerTextimage2.setImageResource(R.mipmap.superheading);

		String branch = extras.getString("orderbranch");

		headerText.setText("Past Reciept");

		// SET TEXT, APPLY FONTS, SETONCLICKLISTENERS
		new ApplyViewParamsTask().execute();
		// LIST VIEW
		new populateListViewTask().execute();
		MYRECEIPT = new ArrayList<RequestedResultsReceipt>();
		myadapterreceipt = new AdapterMyReceipt(
				ActivityMyAccountOrdersView.this, MYRECEIPT);
		// setListViewHeightBasedOnChildren(listView);
		listView.setAdapter(myadapterreceipt);
		boolean resized = DynamicListView.setListViewHeightBasedOnItems(listView);
        Log.d("MAOVS_resized", String.valueOf(resized));
		new populateListViewTask().execute();

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

	private class populateListViewTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			layout_progressbar.setVisibility(View.VISIBLE);
		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return "Done";
		}

		protected void onPostExecute(String params) {
			layout_progressbar.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			listView.setDividerHeight(0);

			myadapterreceipt = new AdapterMyReceipt(
					ActivityMyAccountOrdersView.this, MYRECEIPT);
			setListViewHeightBasedOnChildren(listView);
			listView.setAdapter(myadapterreceipt);
			myadapterreceipt.notifyDataSetChanged();

			String customer_address = extras.getString("customer_address");

			try {
				String order = extras.getString("order");
				Integer totalItems = 0;
				Integer TOTAL2 = 0;
				JSONObject jsonOrderObject = new JSONObject(order);
				Iterator<String> loop = jsonOrderObject.keys();
				while (loop.hasNext()) {
					// Scanner scanner = new Scanner(System.in);

					String key = loop.next();
					Log.i("key", " --> " + key);
					try {
						String orderItems = jsonOrderObject.get(key).toString();
						JSONObject orderItemsObj = new JSONObject(orderItems);
						String item_id = orderItemsObj.getString("id");
						String item_title = orderItemsObj.getString("title");
						String item_size = orderItemsObj.getString("size");
						String item_price = orderItemsObj.getString("price");
						String item_qty = orderItemsObj.getString("qty");

						Integer price = Integer.parseInt(item_price);
						Integer qty = Integer.parseInt(item_qty);

						Integer sumitems = price * qty;

						TOTAL2 = TOTAL2 + sumitems;

						totalItems = totalItems + qty;

						RequestedResultsReceipt d = new RequestedResultsReceipt();

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

						Log.i("Order", " --> " + item_title);
						Log.i("Total", " --> " + TOTAL2);

					} catch (JSONException e) {
						// Something went wrong!
					}

				}

				// TOTAL
				DecimalFormat precision = new DecimalFormat("0.00");
				Double total_double = Double.parseDouble(TOTAL2.toString());
				String total_price = precision.format(total_double);

				Log.i("Ovaraltatol", " --> " + total_price);
				Log.i("Ovaraltatol", " --> " + TOTAL2.toString());

				// VAT AMT
				Integer vat = TOTAL2 * 16;
				Double vatdouble = Double.parseDouble(vat.toString());
				vatdouble /= 100;

				// VATABLE AMT
				Double vatableAmt = total_double - vatdouble;

				totalitem.setText(" " + totalItems);
				vatrate.setText(" 16%");
				vatable.setText(" " + vatableAmt.toString());
				vatamt.setText(" " + vatdouble.toString());

				totalovaraltext.setText(total_price);
				// editor.putString("newtotaldata", total_price).commit();

				int sw = Double.valueOf(total_price).intValue();
				Integer vat1 = sw * 10;
				Double vatdouble1 = Double.parseDouble(vat1.toString());
				vatdouble1 /= 100;

				service.setText(" " + vatdouble1.toString());

				int i = Double.valueOf(total_price).intValue();
				int ii = Double.valueOf(vatdouble1.toString()).intValue();

				if (customer_address.equals("Pick Up")) {
					delivery.setText("Pick Up");
					deliveryinfo.setText(" " + 0.00);

					int recieptdata = i + ii + 0;

					String strI = Integer.toString(recieptdata);

					DecimalFormat precisionTotal = new DecimalFormat("0.00");
					Double item_total_double = Double.parseDouble(strI);
					String item_total1 = precisionTotal
							.format(item_total_double);

					reciepttotaltext.setText(item_total1);

					editor.putString("reciepttotaltextdata", item_total1)
							.commit();

				} else {
					delivery.setText(customer_address);

					deliveryinfo.setText(" " + 250.00);

					int recieptdata = i + ii + 250;

					String strI = Integer.toString(recieptdata);

					DecimalFormat precisionTotal = new DecimalFormat("0.00");
					Double item_total_double = Double.parseDouble(strI);
					String item_total1 = precisionTotal
							.format(item_total_double);

					reciepttotaltext.setText(item_total1);

					editor.putString("reciepttotaltextdata", item_total1)
							.commit();

				}

			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.AT_MOST);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
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
			// String customer_address = extras.getString("customer_address");
			String customer_order = extras.getString("customer_order");
			String order_ref = extras.getString("orderRefNumber");
			String order_date = extras.getString("orderDate");
			String outlet = extras.getString("orderOutlet");
			String branch = extras.getString("newbranch");

			// Ovaraltatol = extras.getString("newtotaldata");

			shopDetails.setText(branch);
			userName.setText(customer_name);
			emailAddress.setText(customer_email);
			phoneNumber.setText(" - " + customer_phone);
			// delivery.setText(customer_address+"\n\nOrder # "+order_ref+"\n"+order_date);
			// Ovaraltatol = "10.00";
			// totalovaraltext.setText(Ovaraltatol);

			// totalitem.setText(" "+totalitem2);
			// vatrate.setText(" "+vatrate2);
			// vatable.setText(" "+vatable2);
			// vatamt.setText(" "+vatamt2);

			// Log.i("Ovaraltatol"," --> "+Ovaraltatol);

			reOrder.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					Log.i("Order", " --> " + extras.getString("orderOutlet"));
					Log.i("orderoutlet",
							" --> " + extras.getString("orderOutlet2"));
					Log.i("orderbrandid",
							" --> " + extras.getString("orderbrandid"));
					Log.i("orderbranchid",
							" --> " + extras.getString("orderbranchid"));

					Intent intent = new Intent(
							ActivityMyAccountOrdersView.this,
							ActivityStep10ListCart.class);
					intent.putExtra("selectedoutlet",
							extras.getString("orderOutlet2"));
					intent.putExtra("selectedbrandid",
							extras.getString("orderbrandid"));
					intent.putExtra("selectedbranchid",
							extras.getString("orderbranchid"));
					intent.putExtra("ordersss", extras.getString("order"));
					editor.putString("reorderif", "reorderif").commit();

					startActivity(intent);

				}
			});

		}

	}

	public void showDialogEmptyTrolley(final String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityMyAccountOrdersView.this);
		builder.setMessage("You will lose all the items that you have selected. "
				+ message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				Intent intent = new Intent(ActivityMyAccountOrdersView.this,
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
