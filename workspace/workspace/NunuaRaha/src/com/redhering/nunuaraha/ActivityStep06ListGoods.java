package com.redhering.nunuaraha;

import java.io.File;
import java.util.ArrayList;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import com.redhering.nunuaraha.AnimatedGifImageView;
import com.redhering.nunuaraha.AnimatedGifImageView.TYPE;

public class ActivityStep06ListGoods extends Activity {
	SQLiteDatabase nunuaRahaDatabase;
	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	Gson gson;

	ArrayList<RequestedResultsSimpleList> PRODUCTLIST;
	AdapterSimpleList myadapter;

	LinearLayout layout_progressbar;
	RelativeLayout headersection;
	RelativeLayout centerwrap;
	RelativeLayout footersection;
	RelativeLayout subtitlesection;
	RelativeLayout listViewCont;
	ListView listView;
	public static String SELECTED_AISLE_ID;
	public static String SELECTED_AISLE_TITLE;
	public static String SELECTED_BRANCH_ID;
	public static String SELECTED_BRANCH_TITLE;
	public static String SELECTED_BRAND_ID;
	public static String SELECTED_BRAND_TITLE;
	public static String SELECTED_BRAND_LOGO;
	public static String SELECTED_LOCATION_ID;
	public static String SELECTED_LOCATION_TITLE;
	public static String SELECTED_OUTLET_ID;
	public static String SELECTED_OUTLET_TITLE;
	public static String BREADCRUMB;
	public static Integer CURRENT_AISLE_ID_POSITION;
	Typeface EkMukta_Light;
	TextView app_name;
	TextView headerText;
	Button menuIcon;
	Button shoppingButton;
	TextView cartButtonNotification;
	Button backToAisles;
	Button backToCategory;
	Button cartButton;
	ImageView shopLogoview;
	String extStorageDirectory;
	Bitmap bm;

	Button proceedToCheckout;
	Button clearcart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scroller_simple_list);
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn
																				// animation
		initViews();

		// LIST VIEW
		new retrieveFromDBTask().execute();
		PRODUCTLIST = new ArrayList<RequestedResultsSimpleList>();
		myadapter = new AdapterSimpleList(ActivityStep06ListGoods.this,
				PRODUCTLIST, EkMukta_Light);
		listView.setAdapter(myadapter);
		new populateListViewTask().execute();

	}

	private void initViews() {
		extStorageDirectory = Environment.getExternalStorageState().toString();
		extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();

		listView = (ListView) findViewById(R.id.simpleListView);
		layout_progressbar = (LinearLayout) findViewById(R.id.progressbar_view);
		headersection = (RelativeLayout) findViewById(R.id.header);
		footersection = (RelativeLayout) findViewById(R.id.footer);
		subtitlesection = (RelativeLayout) findViewById(R.id.subHeader);
		listViewCont = (RelativeLayout) findViewById(R.id.listViewCont);
		sharedPreferences = getSharedPreferences(MY_SESSION,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		gson = new Gson();
		SELECTED_OUTLET_ID = sharedPreferences.getString("selectedOutletId",
				null);
		SELECTED_OUTLET_TITLE = sharedPreferences.getString("selectedOutlet",
				null);
		SELECTED_LOCATION_ID = sharedPreferences.getString(
				"selectedLocationId", null);
		SELECTED_LOCATION_TITLE = sharedPreferences.getString(
				"selectedLocation", null);
		SELECTED_BRAND_ID = sharedPreferences
				.getString("selectedBrandId", null);
		SELECTED_BRAND_TITLE = sharedPreferences.getString("selectedBrand",
				null);
		SELECTED_BRAND_LOGO = sharedPreferences.getString("selectedBrandLogo",
				null);
		SELECTED_BRANCH_ID = sharedPreferences.getString("selectedBranchId",
				null);
		SELECTED_BRANCH_TITLE = sharedPreferences.getString("selectedBranch",
				null);
		SELECTED_AISLE_ID = sharedPreferences
				.getString("selectedAisleId", null);
		SELECTED_AISLE_TITLE = sharedPreferences.getString("selectedAisle",
				null);
		EkMukta_Light = Typeface.createFromAsset(getAssets(),
				"fonts/ek_mukta/EkMukta-Light.ttf");
		app_name = (TextView) findViewById(R.id.app_name);
		menuIcon = (Button) findViewById(R.id.menu_icon);
		headerText = (TextView) findViewById(R.id.headerText);
		shoppingButton = (Button) findViewById(R.id.shoppingButton);
		cartButton = (Button) findViewById(R.id.cartButton);
		cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);
		backToAisles = (Button) findViewById(R.id.backToAisles);
		backToCategory = (Button) findViewById(R.id.backButton);
		proceedToCheckout = (Button) findViewById(R.id.proceedToCheckout);
		// shopLogoview = (ImageView) findViewById(R.id.shopLogo);

		proceedToCheckout = (Button) findViewById(R.id.proceedToCheckout);
		clearcart = (Button) findViewById(R.id.clearcart);

		app_name.setTypeface(EkMukta_Light);
		headerText.setText(SELECTED_BRANCH_TITLE);
		shoppingButton.setTextColor(getResources().getColor(R.color.green));
		subtitlesection.setVisibility(View.VISIBLE);
		TextView subtitle = (TextView) findViewById(R.id.aislesHeaderText);
		subtitle.setText(SELECTED_AISLE_TITLE);
		RelativeLayout.LayoutParams listviewParams = (RelativeLayout.LayoutParams) listViewCont
				.getLayoutParams();
		listviewParams.addRule(RelativeLayout.BELOW, subtitlesection.getId());
		listViewCont.setLayoutParams(listviewParams);

		ImageView headerTextimage2 = (ImageView) findViewById(R.id.headerTextimage);
		headerTextimage2.setVisibility(View.VISIBLE);
		headerTextimage2.setImageResource(R.drawable.superheading1);

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityStep06ListGoods.this,
				sharedPreferences);

		// DISPLAY OUTLET LOGO
		// File file = new File(extStorageDirectory+"/NunuaRaha/",
		// SELECTED_BRAND_LOGO);
		// BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
		// options);
		// shopLogoview.setImageBitmap(bitmap);

		footersection.setVisibility(View.VISIBLE);
		backToAisles.setVisibility(View.VISIBLE);
		backToAisles.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

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
					Toast.makeText(ActivityStep06ListGoods.this,
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
					Toast.makeText(ActivityStep06ListGoods.this,
							"click your cart to confirm first",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ActivityStep06ListGoods.this,
							"No items in your cart !", Toast.LENGTH_LONG)
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

		myadapter = new AdapterSimpleList(ActivityStep06ListGoods.this,
				PRODUCTLIST, EkMukta_Light);
		listView.setAdapter(myadapter);
		myadapter.notifyDataSetChanged();

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityStep06ListGoods.this,
				sharedPreferences);
	}

	private class retrieveFromDBTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			layout_progressbar.setVisibility(View.VISIBLE);

			AnimatedGifImageView animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.animatedGifImageView));
			animatedGifImageView.setAnimatedGif(R.drawable.loading_bar,
					TYPE.FIT_CENTER);
			// animatedGifImageView.setImageResource(R.drawable.loading_bar);

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
			myadapter.notifyDataSetChanged();
			// super.onPostExecute();

			nunuaRahaDatabase = openOrCreateDatabase("nunuaRahaDatabase",
					MODE_PRIVATE, null);

			Cursor resultsStocksCursor = nunuaRahaDatabase.rawQuery(
					"SELECT * FROM hdjgf_shops_stocks WHERE branch_id = "
							+ SELECTED_BRANCH_ID + " AND aisle_id = "
							+ SELECTED_AISLE_ID, null);
			resultsStocksCursor.moveToFirst();
			String productsIdsString = "";
			while (resultsStocksCursor.isAfterLast() == false) {
				String prod_id = resultsStocksCursor.getString(3);
				productsIdsString = productsIdsString + prod_id + ",";
				resultsStocksCursor.moveToNext();
			}
			String[] roductsIdsArr = productsIdsString.split(",");
			String inClause = gson.toJson(roductsIdsArr); // at this point
															// inClause will
															// look like
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

			Cursor resultsProductsCursor = nunuaRahaDatabase.rawQuery(
					"SELECT * FROM hdjgf_products WHERE id IN " + inClause,
					null);
			resultsProductsCursor.moveToFirst();
			String[] list_item_ids = new String[resultsProductsCursor
					.getCount()];
			String[] list_item_titles = new String[resultsProductsCursor
					.getCount()];

			ArrayList<ArrayList<ArrayList<String>>> goodsIdListCont = new ArrayList<ArrayList<ArrayList<String>>>(); // Outer
																														// Array
			ArrayList<ArrayList<String>> goodsIdList = new ArrayList<ArrayList<String>>(); // Inner
																							// Array

			int k = 0;
			while (resultsProductsCursor.isAfterLast() == false) {
				String item_id = resultsProductsCursor.getString(0);
				String item_title = resultsProductsCursor.getString(1);

				ArrayList<String> goodsListInfo = new ArrayList<String>(); // Inner
																			// array
				goodsListInfo.add(item_id);
				goodsListInfo.add(item_title);
				goodsIdList.add(new ArrayList<String>(goodsListInfo));

				list_item_ids[k] = item_id;
				list_item_titles[k] = item_title;

				RequestedResultsSimpleList d = new RequestedResultsSimpleList();

				d.setTitle(item_title);

				d.item_id = item_id;
				d.item_title = item_title;
				PRODUCTLIST.add(d);

				k++;
				resultsProductsCursor.moveToNext();
			}

			// ADD INNER ARRAY TO OUTER ARRAY
			goodsIdListCont.add(new ArrayList<ArrayList<String>>(goodsIdList));
			String jsonGoodsIdList = gson.toJson(goodsIdListCont);
			editor.putString("goodsIdList", jsonGoodsIdList);
			editor.commit();

		}

	}

	private class populateListViewTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return "Done";
		}

		protected void onPostExecute(String params) {
			listView.setTextFilterEnabled(true);
			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					RequestedResultsSimpleList item = (RequestedResultsSimpleList) listView
							.getItemAtPosition(position);

					sharedPreferences = getSharedPreferences(MY_SESSION,
							MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("currentGoodsListPosition",
							String.valueOf(position));
					editor.commit();

					Intent intent = new Intent(ActivityStep06ListGoods.this,
							ActivityStep07ListProducts.class);
					intent.putExtra("selectedProductId", item.item_id);
					intent.putExtra("selectedProductTitle", item.item_title);
					startActivity(intent);
				}
			});
		}

	}

	public void showDialogEmptyTrolley(final String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityStep06ListGoods.this);
		builder.setMessage("You will lose all the items that you have selected. "
				+ message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				Intent intent = new Intent(ActivityStep06ListGoods.this,
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
