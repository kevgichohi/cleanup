package com.righthere.efam;

import java.io.File;
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
import com.righthere.efam.AnimatedGifImageView.TYPE;

public class ActivityStep03ListNeighbourhood extends Activity {
	SQLiteDatabase nunuaRahaDatabase;
	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	Gson gson;

	ArrayList<RequestedResultsSimpleList> LOCATIONLIST;
	AdapterSimpleList myadapter;
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
	public static String SELECTED_OUTLET_ID;
	public static String SELECTED_OUTLET_TITLE;
	Typeface EkMukta_Light;
	TextView app_name;
	TextView headerText;
	Button menuIcon;
	Button shoppingButton;
	Button cartButton;
	TextView cartButtonNotification;
	ImageView shopLogoview;
	ImageView itemImage;
	Button backButton;
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
		LOCATIONLIST = new ArrayList<RequestedResultsSimpleList>();
		myadapter = new AdapterSimpleList(ActivityStep03ListNeighbourhood.this,
				LOCATIONLIST, EkMukta_Light);
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
		cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);
		cartButton = (Button) findViewById(R.id.cartButton);
		// shopLogoview = (ImageView) findViewById(R.id.shopLogo);
		backButton = (Button) findViewById(R.id.backButton);

		proceedToCheckout = (Button) findViewById(R.id.proceedToCheckout);
		clearcart = (Button) findViewById(R.id.clearcart);
		app_name.setTypeface(EkMukta_Light);
		headerText.setText("Select Neighbourhood");

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityStep03ListNeighbourhood.this,
				sharedPreferences);

		// DISPLAY OUTLET LOGO
		File file = new File(extStorageDirectory + "/NunuaRaha/",
				SELECTED_BRAND_LOGO);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
				options);
		// shopLogoview.setImageBitmap(bitmap);

		ImageView headerTextimage2 = (ImageView) findViewById(R.id.headerTextimage);
		headerTextimage2.setVisibility(View.VISIBLE);
		headerTextimage2.setImageResource(R.mipmap.neighbourhood2);

		/*
		 * ImageView itemImage2 = (ImageView) findViewById(R.id.itemImage);
		 * itemImage2.setVisibility(View.VISIBLE);
		 * itemImage2.setImageResource(R.drawable.neighbourhood);
		 */

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
					Toast.makeText(ActivityStep03ListNeighbourhood.this,
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
					Toast.makeText(ActivityStep03ListNeighbourhood.this,
							"click your cart to confirm first",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ActivityStep03ListNeighbourhood.this,
							"No items in your cart !", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		footersection.setVisibility(View.VISIBLE);
		backButton.setVisibility(View.VISIBLE);
		// backButton.setText("Back to "+SELECTED_OUTLET_TITLE);
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

		myadapter = new AdapterSimpleList(ActivityStep03ListNeighbourhood.this,
				LOCATIONLIST, EkMukta_Light);
		listView.setAdapter(myadapter);
		myadapter.notifyDataSetChanged();

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityStep03ListNeighbourhood.this,
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

			// GET BRANCHES
			Cursor resultsBranchesCursor = nunuaRahaDatabase.rawQuery(
					"SELECT * FROM hdjgf_shops_branches WHERE shop_brand_id = "
							+ SELECTED_BRAND_ID + " AND status = 1", null);
			resultsBranchesCursor.moveToFirst();
			int i = 0;
			String deliveryAreasString = "";
			while (resultsBranchesCursor.isAfterLast() == false) {
				String item_delivery_areas = resultsBranchesCursor.getString(5);
				JSONArray jsonArr;
				try {
					jsonArr = new JSONArray(item_delivery_areas);
					for (int j = 0; j < jsonArr.length(); j++) {
						String location_id = jsonArr.getString(j);
						deliveryAreasString = deliveryAreasString + location_id
								+ ",";
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i++;
				resultsBranchesCursor.moveToNext();
			}
			String[] deliveryAreasArr = deliveryAreasString.split(",");
			String inClause = gson.toJson(deliveryAreasArr); // at this point
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

			// GET LOCATIONS
			Cursor resultsLocationsCursor = nunuaRahaDatabase.rawQuery(
					"SELECT * FROM hdjgf_locations WHERE id IN " + inClause,
					null);
			resultsLocationsCursor.moveToFirst();
			String[] list_item_ids = new String[resultsLocationsCursor
					.getCount()];
			String[] list_item_titles = new String[resultsLocationsCursor
					.getCount()];
			int k = 0;
			while (resultsLocationsCursor.isAfterLast() == false) {
				String item_id = resultsLocationsCursor.getString(0);
				String item_title = resultsLocationsCursor.getString(1);

				list_item_ids[k] = item_id;
				list_item_titles[k] = item_title;

				RequestedResultsSimpleList d = new RequestedResultsSimpleList();

				d.setTitle(item_title);

				d.item_id = item_id;
				d.item_title = item_title;
				LOCATIONLIST.add(d);

				k++;
				resultsLocationsCursor.moveToNext();
			}

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
			listView.setDividerHeight(0); // remove the default divider line
			listView.setTextFilterEnabled(true);
			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					RequestedResultsSimpleList item = (RequestedResultsSimpleList) listView
							.getItemAtPosition(position);

					sharedPreferences = getSharedPreferences(MY_SESSION,
							MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("selectedLocation", item.item_title);
					editor.putString("selectedLocationId", item.item_id);
					editor.commit();

					Intent intent = new Intent(
							ActivityStep03ListNeighbourhood.this,
							ActivityStep04ListBranches.class);
					startActivity(intent);
				}
			});
		}

	}

	public void showDialogEmptyTrolley(final String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityStep03ListNeighbourhood.this);
		builder.setMessage("You will lose all the items that you have selected. "
				+ message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				Intent intent = new Intent(
						ActivityStep03ListNeighbourhood.this,
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
