package com.righthere.efam;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
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

import com.righthere.efam.AnimatedGifImageView;
import com.righthere.efam.AnimatedGifImageView.TYPE;
import com.righthere.efam.interfaces.Constants;

public class ActivityStep05ListAisles extends Activity {

	SQLiteDatabase nunuaRahaDatabase;
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	public static final String MY_SESSION = "mySession";
	Gson gson;

	ArrayList<RequestedResults> AISLELIST;
	AdapterListWithImage myadapter;

	ListView listView;

	LinearLayout layout_progressbar;
	RelativeLayout headersection;
	RelativeLayout centerwrap;
	RelativeLayout footersection;
	public static String SELECTED_BRANCH_ID;
	public static String SELECTED_BRANCH_TITLE;
	public static String SELECTED_BRAND_ID;
	public static String SELECTED_BRAND_TITLE;
	public static String SELECTED_BRAND_LOGO;
	public static String SELECTED_LOCATION_ID;
	public static String SELECTED_LOCATION_TITLE;
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
		AISLELIST = new ArrayList<RequestedResults>();
		myadapter = new AdapterListWithImage(ActivityStep05ListAisles.this,
				AISLELIST, EkMukta_Light);
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
		EkMukta_Light = Typeface.createFromAsset(getAssets(),
				"fonts/ek_mukta/EkMukta-Light.ttf");
		app_name = (TextView) findViewById(R.id.app_name);
		menuIcon = (Button) findViewById(R.id.menu_icon);
		headerText = (TextView) findViewById(R.id.headerText);
		shoppingButton = (Button) findViewById(R.id.shoppingButton);
		// homeButton = (Button) findViewById(R.id.homeButton);
		// accountButton = (Button) findViewById(R.id.accountButton);
		cartButton = (Button) findViewById(R.id.cartButton);
		cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);
		backButton = (Button) findViewById(R.id.backButton);
		// shopLogoview = (ImageView) findViewById(R.id.shopLogo);

		headerText.setText(SELECTED_BRANCH_TITLE);
		app_name.setTypeface(EkMukta_Light);

		proceedToCheckout = (Button) findViewById(R.id.proceedToCheckout);
		clearcart = (Button) findViewById(R.id.clearcart);

		ImageView headerTextimage2 = (ImageView) findViewById(R.id.headerTextimage);
		headerTextimage2.setVisibility(View.VISIBLE);
		headerTextimage2.setImageResource(R.mipmap.superheading1);

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityStep05ListAisles.this,
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

				if (sharedPreferences.contains("myTrolley")) {
					try {
						showDialogEmptyTrolley("Are you sure?");
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					Toast.makeText(ActivityStep05ListAisles.this,
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
					Toast.makeText(ActivityStep05ListAisles.this,
							"click your cart to confirm first",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ActivityStep05ListAisles.this,
							"No items in your cart !", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		footersection.setVisibility(View.VISIBLE);
		backButton.setVisibility(View.VISIBLE);
		// backButton.setText("Back to "+SELECTED_BRAND_TITLE+" branches");
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

		myadapter = new AdapterListWithImage(ActivityStep05ListAisles.this,
				AISLELIST, EkMukta_Light);
		listView.setAdapter(myadapter);
		myadapter.notifyDataSetChanged();

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityStep05ListAisles.this,
				sharedPreferences);
	}

	private class retrieveFromDBTask extends AsyncTask<String, Void, String> {

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
			myadapter.notifyDataSetChanged();
			// super.onPostExecute();

			nunuaRahaDatabase = openOrCreateDatabase("nunuaRahaDatabase",
					MODE_PRIVATE, null);

			// GET AISLES IDS FROM SELECTED BRANCH
			Cursor resultsBranchCursor = nunuaRahaDatabase.rawQuery(
					"SELECT * FROM hdjgf_shops_branches WHERE id ="
							+ SELECTED_BRANCH_ID, null);
			resultsBranchCursor.moveToFirst();
			String aislesIds = resultsBranchCursor.getString(4);
			String aislesIdsString = "";
			JSONArray jsonArr;
			try {
				jsonArr = new JSONArray(aislesIds);
				for (int j = 0; j < jsonArr.length(); j++) {
					String aisle_id = jsonArr.getString(j);
					aislesIdsString = aislesIdsString + aisle_id + ",";
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[] aislesIdsArr = aislesIdsString.split(",");
			String inClause = gson.toJson(aislesIdsArr); // at this point
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

			// GET AISLES BY ID FROM ID LIST ABOVE
			Cursor resultsAislesCursor = nunuaRahaDatabase.rawQuery(
					"SELECT * FROM hdjgf_shops_aisles WHERE id IN " + inClause,
					null);
			resultsAislesCursor.moveToFirst();
			String[] list_item_ids = new String[resultsAislesCursor.getCount()];
			String[] list_item_titles = new String[resultsAislesCursor
					.getCount()];

			ArrayList<ArrayList<ArrayList<String>>> aislesIdListCont = new ArrayList<ArrayList<ArrayList<String>>>(); // Outer
																														// Array
			ArrayList<ArrayList<String>> aislesIdList = new ArrayList<ArrayList<String>>(); // Inner
																							// Array

			int k = 0;
			while (resultsAislesCursor.isAfterLast() == false) {
				String item_id = resultsAislesCursor.getString(0);
				String item_title = resultsAislesCursor.getString(1);
				String item_image_url = resultsAislesCursor.getString(2);

				ArrayList<String> aislesListInfo = new ArrayList<String>(); // Inner
																			// array
				aislesListInfo.add(item_id);
				aislesListInfo.add(item_title);
				aislesListInfo
						.add(Constants.IMAGES_URL
								+ item_image_url);
				// aislesListInfo.add(item_image_url);
				aislesIdList.add(new ArrayList<String>(aislesListInfo));

				list_item_ids[k] = item_id;
				list_item_titles[k] = item_title;

				RequestedResults d = new RequestedResults();

				d.setTitle(item_title);
				d.setThumbnailUrl(item_image_url);

				d.item_id = item_id;
				d.item_title = item_title;
				// d.item_thumbnail_url = item_image_url;
				d.item_thumbnail_url = Constants.IMAGES_URL
						+ item_image_url;

				Log.i("Image url", " --> " + d.item_thumbnail_url);

				AISLELIST.add(d);

				k++;
				resultsAislesCursor.moveToNext();
			}

			// ADD INNER ARRAY TO OUTER ARRAY
			aislesIdListCont
					.add(new ArrayList<ArrayList<String>>(aislesIdList));
			aislesIdList.clear();

			Gson gson = new Gson();
			String jsonAislesIdList = gson.toJson(aislesIdListCont);
			editor.putString("ailsesIdList", jsonAislesIdList);
			editor.commit();

		}

	}

	private class populateListViewTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			layout_progressbar.setVisibility(View.VISIBLE);
			AnimatedGifImageView animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.animatedGifImageView));
			animatedGifImageView.setAnimatedGif(R.mipmap.loading_bar,
					TYPE.FIT_CENTER);

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

					RequestedResults item = (RequestedResults) listView
							.getItemAtPosition(position);

					sharedPreferences = getSharedPreferences(MY_SESSION,
							MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("currentAisleListPosition",
							String.valueOf(position));
					editor.putString("selectedAisle", item.item_title);
					editor.putString("selectedAisleId", item.item_id);
					editor.putString("selectedAisleLogo",
							item.item_thumbnail_url);
					editor.commit();

					Intent intent = new Intent(ActivityStep05ListAisles.this,
							ActivityStep06ListGoods.class);
					startActivity(intent);
				}
			});
		}

	}

	public void showDialogEmptyTrolley(final String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityStep05ListAisles.this);
		builder.setMessage("You will lose all the items that you have selected. "
				+ message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				Intent intent = new Intent(ActivityStep05ListAisles.this,
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
