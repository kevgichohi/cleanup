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
import android.util.Log;
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
import com.redhering.nunuaraha.AnimatedGifImageView.TYPE;

public class ActivityStep04ListBranches extends Activity {
	SQLiteDatabase nunuaRahaDatabase;
	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	Gson gson;

	ArrayList<RequestedResults> SHOPSBRANCHLIST;
	AdapterListWithImage myadapter;
	ListView listView;

	LinearLayout layout_progressbar;
	RelativeLayout headersection;
	RelativeLayout centerwrap;
	RelativeLayout footersection;
	public static String SELECTED_LOCATION_ID;
	public static String SELECTED_LOCATION_TITLE;
	public static String SELECTED_BRAND_ID;
	public static String SELECTED_BRAND_TITLE;
	public static String SELECTED_BRAND_LOGO;
	public static String SELECTED_OUTLET_ID;
	public static String SELECTED_OUTLET_TITLE;
	Typeface EkMukta_Light;
	TextView app_name;
	TextView headerText;
	Button menuIcon;
	Button shoppingButton;
	Button homeButton;
	Button accountButton;
	Button cartButton;
	TextView cartButtonNotification;
	Button backButton;
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
		SHOPSBRANCHLIST = new ArrayList<RequestedResults>();
		myadapter = new AdapterListWithImage(ActivityStep04ListBranches.this,
				SHOPSBRANCHLIST, EkMukta_Light);
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
		sharedPreferences = getSharedPreferences(MY_SESSION,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		gson = new Gson();
		SELECTED_LOCATION_ID = sharedPreferences.getString(
				"selectedLocationId", null);
		SELECTED_LOCATION_TITLE = sharedPreferences.getString(
				"selectedLocation", null);
		SELECTED_OUTLET_ID = sharedPreferences.getString("selectedOutletId",
				null);
		SELECTED_OUTLET_TITLE = sharedPreferences.getString("selectedOutlet",
				null);
		SELECTED_BRAND_ID = sharedPreferences
				.getString("selectedBrandId", null);
		SELECTED_BRAND_TITLE = sharedPreferences.getString("selectedBrand",
				null);
		SELECTED_BRAND_LOGO = sharedPreferences.getString("selectedBrandLogo",
				null);
		EkMukta_Light = Typeface.createFromAsset(getAssets(),
				"fonts/ek_mukta/EkMukta-Light.ttf");
		app_name = (TextView) findViewById(R.id.app_name);
		menuIcon = (Button) findViewById(R.id.menu_icon);
		headerText = (TextView) findViewById(R.id.headerText);
		shoppingButton = (Button) findViewById(R.id.shoppingButton);
		// homeButton = (Button) findViewById(R.id.homeButton);
		// accountButton = (Button) findViewById(R.id.accountButton);
		cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);
		cartButton = (Button) findViewById(R.id.cartButton);
		backButton = (Button) findViewById(R.id.backButton);
		// shopLogoview = (ImageView) findViewById(R.id.shopLogo);

		app_name.setTypeface(EkMukta_Light);
		headerText.setText("Select Branch");

		proceedToCheckout = (Button) findViewById(R.id.proceedToCheckout);
		clearcart = (Button) findViewById(R.id.clearcart);

		ImageView headerTextimage2 = (ImageView) findViewById(R.id.headerTextimage);
		headerTextimage2.setVisibility(View.VISIBLE);
		// headerTextimage2.setImageResource(R.drawable.branchheader);

		if (SELECTED_OUTLET_TITLE == "Wholesalers") {
			headerTextimage2.setImageResource(R.drawable.wholesale);
		} else {
			headerTextimage2.setImageResource(R.drawable.branchheader);
		}
		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityStep04ListBranches.this,
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
					Toast.makeText(ActivityStep04ListBranches.this,
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
					Toast.makeText(ActivityStep04ListBranches.this,
							"click your cart to confirm first",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ActivityStep04ListBranches.this,
							"No items in your cart !", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		footersection.setVisibility(View.VISIBLE);
		backButton.setVisibility(View.VISIBLE);
		// backButton.setText("Back to Neighbourhoods");
		backButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
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

		myadapter = new AdapterListWithImage(ActivityStep04ListBranches.this,
				SHOPSBRANCHLIST, EkMukta_Light);
		listView.setAdapter(myadapter);
		myadapter.notifyDataSetChanged();

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityStep04ListBranches.this,
				sharedPreferences);

	}

	private class retrieveFromDBTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			layout_progressbar.setVisibility(View.VISIBLE);

			AnimatedGifImageView animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.animatedGifImageView));
			animatedGifImageView.setAnimatedGif(R.drawable.loading_bar,
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
			myadapter.notifyDataSetChanged();
			// super.onPostExecute();

			Log.i("location id", " --> " + SELECTED_LOCATION_ID);

			nunuaRahaDatabase = openOrCreateDatabase("nunuaRahaDatabase",
					MODE_PRIVATE, null);

			Cursor resultsBranchesCursor = nunuaRahaDatabase.rawQuery(
					"SELECT * FROM hdjgf_shops_branches WHERE delivery_areas like '%\""
							+ SELECTED_LOCATION_ID
							+ "\"%' AND shop_brand_id = " + SELECTED_BRAND_ID
							+ " AND status = 1", null);
			resultsBranchesCursor.moveToFirst();
			String[] list_item_ids = new String[resultsBranchesCursor
					.getCount()];
			String[] list_item_titles = new String[resultsBranchesCursor
					.getCount()];
			// String[] list_delivery_charges = new
			// String[resultsBranchesCursor.getCount()];

			ArrayList<ArrayList<ArrayList<String>>> branchesIdListCont = new ArrayList<ArrayList<ArrayList<String>>>(); // Outer
																														// Array
			ArrayList<ArrayList<String>> branchesIdList = new ArrayList<ArrayList<String>>(); // Inner
																								// Array

			int k = 0;
			while (resultsBranchesCursor.isAfterLast() == false) {
				String item_id = resultsBranchesCursor.getString(0);
				String item_title = resultsBranchesCursor.getString(1);
				// String item_image_url = resultsBranchesCursor.getString(2);
				// String delivery_charge = resultsBranchesCursor.getString(1);
				String item_image_url = "ic_image.png";

				ArrayList<String> branchesListInfo = new ArrayList<String>(); // Inner
																				// array
				branchesListInfo.add(item_id);
				branchesListInfo.add(item_title);
				branchesListInfo
						.add("http://smokesignal.co.ke/mobiletrolley/img/uploads/"
								+ item_image_url);

				branchesIdList.add(new ArrayList<String>(branchesListInfo));

				list_item_ids[k] = item_id;
				list_item_titles[k] = item_title;

				// list_delivery_charges[i] = delivery_charge;

				RequestedResults d = new RequestedResults();

				d.setTitle(item_title);

				d.item_id = item_id;
				d.item_title = SELECTED_BRAND_TITLE + " - " + item_title;
				d.setThumbnailUrl("http://smokesignal.co.ke/mobiletrolley/img/uploads/"
						+ item_image_url);
				// d.delivery_charge = delivery_charge;
				SHOPSBRANCHLIST.add(d);

				k++;
				resultsBranchesCursor.moveToNext();
			}

			// ADD INNER ARRAY TO OUTER ARRAY
			branchesIdListCont.add(new ArrayList<ArrayList<String>>(
					branchesIdList));
			branchesIdList.clear();
			String jsonBranchesIdList = gson.toJson(branchesIdListCont);
			editor.putString("branchesIdList", jsonBranchesIdList);
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
			listView.setDividerHeight(0);
			listView.setTextFilterEnabled(true);
			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					RequestedResults item = (RequestedResults) listView
							.getItemAtPosition(position);
					try {
						String currentBrand = sharedPreferences.getString(
								"selectedBrand", null);
						String currentBranch = sharedPreferences.getString(
								"selectedBranch", null);
						String currentBranchId = sharedPreferences.getString(
								"selectedBranchId", null);

						editor.putString("selectedBranch", item.item_title);
						editor.putString("selectedBranchId", item.item_id);
						// editor.putString("selectedBranchDeliveryCharges",item.delivery_charge);
						editor.commit();

						if (sharedPreferences.contains("selectedBranchId")) {
							if (sharedPreferences.contains("myTrolley")
									&& !currentBranchId.equals(item.item_id)) {
								showDialogSelectBranch(item, position,
										currentBrand + " " + currentBranch
												+ "?");
							} else {
								Intent intent = new Intent(
										ActivityStep04ListBranches.this,
										ActivityStep05ListAisles.class);
								startActivity(intent);
							}
						} else {

							Intent intent = new Intent(
									ActivityStep04ListBranches.this,
									ActivityStep05ListAisles.class);
							startActivity(intent);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

	}

	public void showDialogSelectBranch(final RequestedResults item,
			final int position, final String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityStep04ListBranches.this);
		builder.setMessage("You will lose all the items in your trolley at "
				+ message + " Are you sure?");

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				layout_progressbar.setVisibility(View.VISIBLE);

				AnimatedGifImageView animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.animatedGifImageView));
				animatedGifImageView.setAnimatedGif(R.drawable.loading_bar,
						TYPE.FIT_CENTER);

				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				editor.putString("selectedBranch", item.item_title);
				editor.putString("selectedBranchId", item.item_id);
				// editor.putString("selectedBranchDeliveryCharges",item.delivery_charge);
				editor.commit();

				Intent intent = new Intent(ActivityStep04ListBranches.this,
						ActivityStep05ListAisles.class);
				startActivity(intent);

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

	public void showDialogEmptyTrolley(final String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityStep04ListBranches.this);
		builder.setMessage("You will lose all the items that you have selected. "
				+ message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				Intent intent = new Intent(ActivityStep04ListBranches.this,
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
