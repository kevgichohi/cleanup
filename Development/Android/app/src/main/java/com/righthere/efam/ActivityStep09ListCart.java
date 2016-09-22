package com.righthere.efam;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.righthere.efam.AnimatedGifImageView.TYPE;
import com.righthere.efam.interfaces.Constants;

public class ActivityStep09ListCart extends Activity {

	SQLiteDatabase nunuaRahaDatabase;
	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	Gson gson;

	ArrayList<RequestedResults> MYCART;
	AdapterMyCart9 myadapter;

	LinearLayout layout_progressbar;
	RelativeLayout headersection;
	RelativeLayout centerwrap;
	RelativeLayout footersection;
	ListView listView;
	public Integer TOTAL = 0;
	public static String SELECTED_BRANCH_ID;
	public static String SELECTED_BRANCH_TITLE;
	public static String SELECTED_BRAND_ID;
	public static String SELECTED_BRAND_TITLE;
	public static String SELECTED_BRAND_LOGO;
	public static String reorderif;
	public static String SELECTED_OUTLET_ID;
	Typeface EkMukta_Light;
	TextView app_name;
	TextView headerText;
	Button menuIcon;
	TextView cartButtonNotification;
	TextView subtitle;
	Button shoppingButton;
	Button cartButton;
	Button proceedToCheckout;
	Button clearcart;
	Button backToAisles;
	ImageView shopLogoview;
	String extStorageDirectory;
	Bitmap bm;
	TextView itemtotalprice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scroller_shoppingcart);
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn
																				// animation
		initViews();

		// LIST VIEW
		new populateListViewTask().execute();
		MYCART = new ArrayList<RequestedResults>();
		myadapter = new AdapterMyCart9(ActivityStep09ListCart.this, MYCART,
				cartButtonNotification, subtitle);
		listView.setAdapter(myadapter);
		myadapter.notifyDataSetChanged();

	}

	private void initViews() {
		extStorageDirectory = Environment.getExternalStorageState().toString();
		extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();
		listView = (ListView) findViewById(R.id.productsListView);
		layout_progressbar = (LinearLayout) findViewById(R.id.progressbar_view);
		centerwrap = (RelativeLayout) findViewById(R.id.centerwrap);
		headersection = (RelativeLayout) findViewById(R.id.header);
		footersection = (RelativeLayout) findViewById(R.id.footer);
		sharedPreferences = getSharedPreferences(MY_SESSION,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		gson = new Gson();
		// SELECTED_BRAND_ID = sharedPreferences.getString("selectedBrandId",
		// null);
		SELECTED_BRAND_TITLE = sharedPreferences.getString("selectedBrand",
				null);
		SELECTED_BRAND_LOGO = sharedPreferences.getString("selectedBrandLogo",
				null);
		// SELECTED_BRANCH_ID = sharedPreferences.getString("selectedBranchId",
		// null);
		// SELECTED_BRANCH_TITLE = sharedPreferences.getString("selectedBranch",
		// null);

		// reorderif = sharedPreferences.getString("reorderif", null);

		// Log.i("reorderif", reorderif);

		if (sharedPreferences.contains("reorderif")) {

			SELECTED_BRANCH_ID = sharedPreferences.getString("newbranchid",
					null);
			SELECTED_BRAND_ID = sharedPreferences.getString("newbrandid", null);
			SELECTED_OUTLET_ID = sharedPreferences.getString("newoutletid",
					null);
			SELECTED_BRANCH_TITLE = sharedPreferences.getString("newbranch",
					null);

		} else {
			SELECTED_BRANCH_ID = sharedPreferences.getString(
					"selectedBranchId", null);
			SELECTED_BRAND_ID = sharedPreferences.getString("selectedBrandId",
					null);
			SELECTED_OUTLET_ID = sharedPreferences.getString(
					"selectedOutletId", null);
			SELECTED_BRANCH_TITLE = sharedPreferences.getString(
					"selectedBranch", null);
		}

		EkMukta_Light = Typeface.createFromAsset(getAssets(),
				"fonts/ek_mukta/EkMukta-Light.ttf");
		app_name = (TextView) findViewById(R.id.app_name);
		headerText = (TextView) findViewById(R.id.headerText);
		menuIcon = (Button) findViewById(R.id.menu_icon);
		shoppingButton = (Button) findViewById(R.id.shoppingButton);
		cartButton = (Button) findViewById(R.id.cartButton);
		cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);
		subtitle = (TextView) findViewById(R.id.subtitle);
		backToAisles = (Button) findViewById(R.id.backToAisles);
		proceedToCheckout = (Button) findViewById(R.id.proceedToCheckout);
		clearcart = (Button) findViewById(R.id.clearcart);
		// shopLogoview = (ImageView) findViewById(R.id.shopLogo);

		headerText.setText(SELECTED_BRANCH_TITLE);
		app_name.setTypeface(EkMukta_Light);
		cartButton.setTextColor(getResources().getColor(R.color.green));
		footersection.setVisibility(View.VISIBLE);

		ImageView headerTextimage2 = (ImageView) findViewById(R.id.headerTextimage);
		headerTextimage2.setVisibility(View.VISIBLE);
		headerTextimage2.setImageResource(R.mipmap.ic_shop);

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityStep09ListCart.this,
				sharedPreferences);

		// DISPLAY OUTLET LOGO
		// File file = new File(extStorageDirectory+"/NunuaRaha/",
		// SELECTED_BRAND_LOGO);
		// BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
		// options);
		// shopLogoview.setImageBitmap(bitmap);

		clearcart.setVisibility(View.VISIBLE);
		clearcart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					showDialogEmptyTrolley("Are you sure?");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		backToAisles.setVisibility(View.VISIBLE);
		backToAisles.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ActivityStep09ListCart.this,
						ActivityStep05ListAisles.class);
				startActivity(intent);
			}
		});

		proceedToCheckout.setVisibility(View.VISIBLE);
		proceedToCheckout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String Tro = sharedPreferences
						.getString("myTrolleyTotal", null);
				// int resulttro = Integer.parseInt(Tro);
				double d = Double.parseDouble(Tro);
				int resulttro = (int) d;

				Log.d("resulttro ", String.valueOf(resulttro));
				if (resulttro >= 10) {
					Intent intent = new Intent(ActivityStep09ListCart.this,
							ActivityStep09ListDeliveryOptions.class);
					startActivity(intent);
				} else {
					Toast.makeText(ActivityStep09ListCart.this,
							"Minimum amount ksh.1000", Toast.LENGTH_LONG)
							.show();
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
																				// animation

		myadapter = new AdapterMyCart9(ActivityStep09ListCart.this, MYCART,
				cartButtonNotification, subtitle);
		listView.setAdapter(myadapter);
		myadapter.notifyDataSetChanged();

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityStep09ListCart.this,
				sharedPreferences);
	}

	private class populateListViewTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			layout_progressbar.setVisibility(View.VISIBLE);

			AnimatedGifImageView animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.animatedGifImageView));
			animatedGifImageView.setAnimatedGif(R.mipmap.loading_bar,
					TYPE.FIT_CENTER);
			listView.setVisibility(View.GONE);
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
			myadapter.notifyDataSetChanged();

			JSONObject jsonMyCartObject = null;
			String stockString = "";
			if (sharedPreferences.contains("myTrolley")) {
				String jsonMyCartString = sharedPreferences.getString(
						"myTrolley", null);
				try {
					jsonMyCartObject = new JSONObject(jsonMyCartString);
					Iterator<String> loop = jsonMyCartObject.keys();
					while (loop.hasNext()) {
						String key = loop.next();
						stockString = stockString + key + ",";
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			Log.i("objecte", " -->" + jsonMyCartObject);
			Log.i("stockstring", " -->" + stockString);
			String[] stockArr = stockString.split(",");
			String inClause = gson.toJson(stockArr); // at this point inClause
														// will look like
														// ["23","343","33","55","43"]
			inClause = inClause.replace("[", "(");// at this point inClause will
													// look like
													// ("23","343","33","55","43"]
			inClause = inClause.replace("]", ")");// at this point inClause will
													// look like
													// ("23","343","33","55","43")
			inClause = inClause.replace("\"", "");// now at this point inClause
													// will look like
													// (23,343,33,55,43) so use
													// it to construct your
													// SELECT
			Log.i("in clause", " -->" + inClause);

			nunuaRahaDatabase = openOrCreateDatabase("nunuaRahaDatabase",
					MODE_PRIVATE, null);
			Cursor resultsProductsCursor = nunuaRahaDatabase.rawQuery(
					"SELECT * FROM hdjgf_shops_stocks_brands WHERE id IN "
							+ inClause, null);
			resultsProductsCursor.moveToFirst();
			String[] list_item_ids = new String[resultsProductsCursor
					.getCount()];
			String[] list_item_titles = new String[resultsProductsCursor
					.getCount()];
			int k = 0;
			while (resultsProductsCursor.isAfterLast() == false) {
				String item_id = resultsProductsCursor.getString(0);
				String item_title = resultsProductsCursor.getString(1);
				String item_size = resultsProductsCursor.getString(2);
				String item_price = resultsProductsCursor.getString(3);
				String item_thumbnail_url = Constants.IMAGES_URL
						+ resultsProductsCursor.getString(4);
				Integer item_units_in_cart = 0;

				Integer item_total = 0;
				try {
					item_units_in_cart = Integer.parseInt(jsonMyCartObject
							.getString(item_id));

				} catch (NumberFormatException | JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				TOTAL = TOTAL
						+ (Integer.parseInt(item_price) * item_units_in_cart);

				list_item_ids[k] = item_id;
				list_item_titles[k] = item_title;

				RequestedResults d = new RequestedResults();

				d.setId(item_id);
				d.setTitle(item_title);
				d.setPrice(item_price);
				d.setSize(item_size);
				d.setThumbnailUrl(item_thumbnail_url);
				d.setUnits(item_units_in_cart);

				d.item_id = item_id;
				d.item_title = item_title;
				d.item_price = item_price;
				d.item_size = item_size;
				d.item_thumbnail_url = item_thumbnail_url;
				d.item_units_in_cart = item_units_in_cart;

				MYCART.add(d);

				k++;
				resultsProductsCursor.moveToNext();
			}
			// FORMAT TOTAL
			DecimalFormat precision = new DecimalFormat("0.00");
			Double total_double = Double.parseDouble(TOTAL.toString());
			String total_price = precision.format(total_double);
			subtitle.setText("My Cart (KES." + total_price + ")");
			editor.putString("myTrolleyTotal", total_price).commit();

			String Ovaraltatol = sharedPreferences.getString("myTrolleyTotal",
					null);

			Log.i("Results", " --> " + Ovaraltatol);

		}

	}

	public void showDialogEmptyTrolley(final String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityStep09ListCart.this);
		builder.setMessage("You will lose all the items that you have selected. "
				+ message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				Intent intent = new Intent(ActivityStep09ListCart.this,
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
