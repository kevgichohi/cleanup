package com.righthere.efam;

import java.util.ArrayList;

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
import android.os.Bundle;
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
import com.righthere.efam.AnimatedGifImageView.TYPE;

import com.google.gson.Gson;

public class ActivityStep02ListBrands extends Activity {

	SQLiteDatabase nunuaRahaDatabase;
	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	public static ArrayList<RequestedResults> SHOPSBRANDSLIST;
	AdapterListWithImage myadapter;

	public static String SELECTED_SHOP_ID;
	public static String SELECTED_SHOP_TITLE;
	ListView listView;

	LinearLayout layout_progressbar;
	RelativeLayout headersection;
	RelativeLayout centerwrap;
	RelativeLayout footersection;
	Typeface EkMukta_Light;
	TextView app_name;
	LinearLayout rlMenu;
	TextView headerText;
	Button menuIcon;
	Button shoppingButton;
	Button homeButton;
	Button accountButton;
	Button cartButton;
	TextView cartButtonNotification;
	Button backButton;

	Button proceedToCheckout;
	Button clearcart;

	String starimageurl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scroller_simple_list);
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn
																				// animation
		footersection = (RelativeLayout) findViewById(R.id.footer);
		listView = (ListView) findViewById(R.id.simpleListView);
		layout_progressbar = (LinearLayout) findViewById(R.id.progressbar_view);
		rlMenu = (LinearLayout) findViewById(R.id.menuListViewCont);
		sharedPreferences = getSharedPreferences(MY_SESSION,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		SELECTED_SHOP_ID = sharedPreferences
				.getString("selectedOutletId", null);
		SELECTED_SHOP_TITLE = sharedPreferences.getString("selectedOutlet",
				null);
		EkMukta_Light = Typeface.createFromAsset(getAssets(),
				"fonts/ek_mukta/EkMukta-Light.ttf");
		app_name = (TextView) findViewById(R.id.app_name);
		headerText = (TextView) findViewById(R.id.headerText);
		menuIcon = (Button) findViewById(R.id.menu_icon);
		shoppingButton = (Button) findViewById(R.id.shoppingButton);
		// homeButton = (Button) findViewById(R.id.homeButton);
		// accountButton = (Button) findViewById(R.id.accountButton);
		cartButton = (Button) findViewById(R.id.cartButton);
		cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);
		backButton = (Button) findViewById(R.id.backButton);

		proceedToCheckout = (Button) findViewById(R.id.proceedToCheckout);
		clearcart = (Button) findViewById(R.id.clearcart);

		layout_progressbar.setVisibility(View.VISIBLE);
		headerText.setText(SELECTED_SHOP_TITLE);
		app_name.setTypeface(EkMukta_Light);

		ImageView headerTextimage2 = (ImageView) findViewById(R.id.headerTextimage);
		headerTextimage2.setVisibility(View.VISIBLE);

		Log.i("Branch Title ", SELECTED_SHOP_TITLE);

		if (SELECTED_SHOP_TITLE.equals("Wholesalers")) {
			headerTextimage2.setImageResource(R.mipmap.wholesale);
			Log.i("Branch Title3 ", SELECTED_SHOP_TITLE);
		} else {
			headerTextimage2.setImageResource(R.mipmap.superheading);
			Log.i("Branch Title2 ", SELECTED_SHOP_TITLE);
		}

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityStep02ListBrands.this,
				sharedPreferences);

		// LISTVIEW
		new retrieveFromDBTask().execute();

		SHOPSBRANDSLIST = new ArrayList<RequestedResults>();

		myadapter = new AdapterListWithImage(ActivityStep02ListBrands.this,
				SHOPSBRANDSLIST, EkMukta_Light);

		listView.setAdapter(myadapter);
		new populateListViewTask().execute();

		footersection.setVisibility(View.VISIBLE);
		backButton.setVisibility(View.VISIBLE);
		backButton.setText("");
		backButton.setOnClickListener(new View.OnClickListener() {
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
					Toast.makeText(ActivityStep02ListBrands.this,
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
					Toast.makeText(ActivityStep02ListBrands.this,
							"click your cart to confirm first",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ActivityStep02ListBrands.this,
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

		myadapter = new AdapterListWithImage(ActivityStep02ListBrands.this,
				SHOPSBRANDSLIST, EkMukta_Light);
		listView.setAdapter(myadapter);
		myadapter.notifyDataSetChanged();

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityStep02ListBrands.this,
				sharedPreferences);
	}

	private class retrieveFromDBTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			layout_progressbar.setVisibility(View.VISIBLE);

			AnimatedGifImageView animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.animatedGifImageView));
			animatedGifImageView.setAnimatedGif(R.mipmap.loading_bar,
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
			Cursor resultsCursor = nunuaRahaDatabase.rawQuery(
					"SELECT * FROM hdjgf_shops_brands WHERE shop_id = "
							+ SELECTED_SHOP_ID + " AND status = 1", null);
			resultsCursor.moveToFirst();

			String[] list_item_ids = new String[resultsCursor.getCount()];
			String[] list_item_titles = new String[resultsCursor.getCount()];
			int i = 0;
			ArrayList<ArrayList<ArrayList<String>>> outletBrandsIdListCont = new ArrayList<ArrayList<ArrayList<String>>>(); // Outer
																															// Array
			ArrayList<ArrayList<String>> outletBrandsIdList = new ArrayList<ArrayList<String>>(); // Inner
																									// Array

			while (resultsCursor.isAfterLast() == false) {
				String item_id = resultsCursor.getString(0);
				String item_title = resultsCursor.getString(1);
				String item_image_url = resultsCursor.getString(2);

				ArrayList<String> outletBrandsListInfo = new ArrayList<String>(); // Inner
																					// array
				outletBrandsListInfo.add(item_id);
				outletBrandsListInfo.add(item_title);
				outletBrandsListInfo
						.add("http://smokesignal.co.ke/mobiletrolley/img/uploads/"
								+ item_image_url);
				outletBrandsIdList.add(new ArrayList<String>(
						outletBrandsListInfo));

				list_item_ids[i] = item_id;
				list_item_titles[i] = item_title;

				RequestedResults d = new RequestedResults();

				d.setTitle(item_title);
				d.setThumbnailUrl("http://smokesignal.co.ke/mobiletrolley/img/uploads/"
						+ item_image_url);

				d.item_id = item_id;
				d.item_title = item_title;
				d.item_thumbnail_url = "http://smokesignal.co.ke/mobiletrolley/img/uploads/"
						+ item_image_url;

				SHOPSBRANDSLIST.add(d);

				i++;
				resultsCursor.moveToNext();
			}

			// ADD INNER ARRAY TO OUTER ARRAY
			outletBrandsIdListCont.add(new ArrayList<ArrayList<String>>(
					outletBrandsIdList));
			outletBrandsIdList.clear();
			Gson gson = new Gson();
			String jsonOutletBrandsIdList = gson.toJson(outletBrandsIdListCont);
			editor.putString("outletBrandsIdList", jsonOutletBrandsIdList);
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
						String currentBrandId = sharedPreferences.getString(
								"selectedBrandId", null);
						String currentBranch = sharedPreferences.getString(
								"selectedBranch", null);

						if (sharedPreferences.contains("selectedBrandId")) {
							if (sharedPreferences.contains("myTrolley")
									&& !currentBrandId.equals(item.item_id)) {
								showDialogSelectBrand(item, position,
										currentBrand + " " + currentBranch
												+ "?");
							} else {
								// ADD TO SESSION
								editor.putString("selectedBrand",
										item.item_title);
								editor.putString("selectedBrandId",
										item.item_id);
								editor.putString("selectedBrandLogo",
										item.item_thumbnail_url);
								editor.commit();

								Intent intent = new Intent(
										ActivityStep02ListBrands.this,
										ActivityStep03ListNeighbourhood.class);
								startActivity(intent);
							}
						} else {
							// ADD TO SESSION
							editor.putString("selectedBrand", item.item_title);
							editor.putString("selectedBrandId", item.item_id);
							editor.putString("selectedBrandLogo",
									item.item_thumbnail_url);
							editor.commit();
							Intent intent = new Intent(
									ActivityStep02ListBrands.this,
									ActivityStep03ListNeighbourhood.class);
							startActivity(intent);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
		}

	}

	public void showDialogSelectBrand(final RequestedResults item,
			final int position, final String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityStep02ListBrands.this);
		builder.setMessage("You will lose all the items in your cart at "
				+ message + " Are you sure?");

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				layout_progressbar.setVisibility(View.VISIBLE);

				AnimatedGifImageView animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.animatedGifImageView));
				animatedGifImageView.setAnimatedGif(R.mipmap.loading_bar,
						TYPE.FIT_CENTER);

				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				// ADD TO SESSION
				editor.putString("selectedBrand", item.item_title);
				editor.putString("selectedBrandId", item.item_id);
				editor.putString("selectedBrandLogo", item.item_thumbnail_url);
				editor.commit();

				Intent intent = new Intent(ActivityStep02ListBrands.this,
						ActivityStep03ListNeighbourhood.class);
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
				ActivityStep02ListBrands.this);
		builder.setMessage("You will lose all the items that you have selected. "
				+ message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				Intent intent = new Intent(ActivityStep02ListBrands.this,
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
