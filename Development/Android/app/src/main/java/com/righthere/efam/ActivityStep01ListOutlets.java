package com.righthere.efam;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
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

import com.google.gson.Gson;

import com.righthere.efam.AnimatedGifImageView.TYPE;
import com.righthere.efam.interfaces.Constants;

public class ActivityStep01ListOutlets extends Activity {
	SQLiteDatabase nunuaRahaDatabase;
	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	public static ArrayList<RequestedResults> OUTLETS;
	AdapterListWithImage myadapter;
	ListView listView;
	LinearLayout layout_progressbar;
	RelativeLayout headersection;
	RelativeLayout centerwrap;
	RelativeLayout footersection;
	LinearLayout listViewCont;
	Typeface EkMukta_Light;
	TextView app_name;
	TextView headerText;
	Button menuIcon;
	Button shoppingButton;
	Button cartButton;
	TextView cartButtonNotification;

	Button proceedToCheckout;
	Button clearcart;

	private ImageView headerTextimage2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scroller_simple_list);
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn
																				// animation
		sharedPreferences = getSharedPreferences(MY_SESSION, MODE_PRIVATE);
		editor = sharedPreferences.edit();
		listView = (ListView) findViewById(R.id.simpleListView);
		layout_progressbar = (LinearLayout) findViewById(R.id.progressbar_view);
		EkMukta_Light = Typeface.createFromAsset(getAssets(),
				"fonts/ek_mukta/EkMukta-Light.ttf");
		app_name = (TextView) findViewById(R.id.app_name);
		menuIcon = (Button) findViewById(R.id.menu_icon);
		headerText = (TextView) findViewById(R.id.headerText);
		shoppingButton = (Button) findViewById(R.id.shoppingButton);
		cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);
		cartButton = (Button) findViewById(R.id.cartButton);

		proceedToCheckout = (Button) findViewById(R.id.proceedToCheckout);
		clearcart = (Button) findViewById(R.id.clearcart);

		layout_progressbar.setVisibility(View.VISIBLE);
		headerText.setText("Retailers");
		headerTextimage2 = (ImageView) findViewById(R.id.headerTextimage);
		headerTextimage2.setVisibility(View.VISIBLE);
		headerTextimage2.setImageResource(R.mipmap.retailers);
		app_name.setTypeface(EkMukta_Light);

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityStep01ListOutlets.this,
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
					Toast.makeText(ActivityStep01ListOutlets.this,
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
					Toast.makeText(ActivityStep01ListOutlets.this,
							"click your cart to confirm first",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ActivityStep01ListOutlets.this,
							"No items in your cart !", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		// LOAD LIST ITEMS
		new retrieveFromDBTask().execute();
		OUTLETS = new ArrayList<RequestedResults>();
		myadapter = new AdapterListWithImage(ActivityStep01ListOutlets.this,
				OUTLETS, EkMukta_Light);
		listView.setAdapter(myadapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				RequestedResults item = (RequestedResults) listView
						.getItemAtPosition(position);
				try {
					String currentOutlet = sharedPreferences.getString(
							"selectedOutlet", null);
					String currentOutletId = sharedPreferences.getString(
							"selectedOutletId", null);
					String currentBrand = sharedPreferences.getString(
							"selectedBrand", null);
					String currentBranch = sharedPreferences.getString(
							"selectedBranch", null);

					if (sharedPreferences.contains("selectedOutletId")) {
						if (sharedPreferences.contains("myTrolley")
								&& !currentOutletId.equals(item.item_id)) {
							showDialogSelectBranch(item, position,
									currentOutlet + " - " + currentBrand + " "
											+ currentBranch + ".");
						} else {
							Log.i("Order title", " --> " + item.item_title);
							editor.putString("selectedOutlet", item.item_title);
							editor.putString("selectedOutletId", item.item_id);
							editor.commit();
							Intent intent = new Intent(
									ActivityStep01ListOutlets.this,
									ActivityStep02ListBrands.class);
							startActivity(intent);
						}
					} else {

						editor.putString("selectedOutlet", item.item_title);
						editor.putString("selectedOutletId", item.item_id);
						editor.commit();
						Intent intent = new Intent(
								ActivityStep01ListOutlets.this,
								ActivityStep02ListBrands.class);
						startActivity(intent);

						Log.i("Order title", " --> " + item.item_title);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		listView.setAdapter(myadapter);
		listView.setTextFilterEnabled(true);

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

		myadapter = new AdapterListWithImage(ActivityStep01ListOutlets.this,
				OUTLETS, EkMukta_Light);
		listView.setAdapter(myadapter);
		myadapter.notifyDataSetChanged();

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityStep01ListOutlets.this,
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
			// menuListView.setVisibility(View.VISIBLE);
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
					"SELECT * FROM hdjgf_shops", null);
			resultsCursor.moveToFirst();

			String[] list_item_ids = new String[resultsCursor.getCount()];
			String[] list_item_titles = new String[resultsCursor.getCount()];
			int i = 0;
			ArrayList<ArrayList<ArrayList<String>>> outletsIdListCont = new ArrayList<ArrayList<ArrayList<String>>>(); // Outer
																														// Array
			ArrayList<ArrayList<String>> outletsIdList = new ArrayList<ArrayList<String>>(); // Inner
																								// Array

			Log.i("creating database",
					" --> retrieve funtion" + resultsCursor.getCount());

			while (resultsCursor.isAfterLast() == false) {

				Log.i("creating database", " --> loop funtion");

				String item_id = resultsCursor.getString(0);
				String item_title = resultsCursor.getString(1);
				String item_image_url = "ic_image.png";

				ArrayList<String> outlestListInfo = new ArrayList<String>(); // Inner
																				// array
				outlestListInfo.add(item_id);
				outlestListInfo.add(item_title);

				outlestListInfo
						.add(Constants.IMAGES_URL
								+ item_image_url);

				outletsIdList.add(new ArrayList<String>(outlestListInfo));

				list_item_ids[i] = item_id;
				list_item_titles[i] = item_title;

				RequestedResults d = new RequestedResults();

				d.setTitle(item_title);

				d.setThumbnailUrl(Constants.IMAGES_URL + item_image_url);

				d.item_id = item_id;
				d.item_title = item_title;
				OUTLETS.add(d);

				i++;
				resultsCursor.moveToNext();

				// Log.i("Order title"," --> "+item_title);
				// Log.i("Order id"," --> "+item_id);
			}

			// ADD INNER ARRAY TO OUTER ARRAY
			outletsIdListCont.add(new ArrayList<ArrayList<String>>(
					outletsIdList));
			outletsIdList.clear();
			Gson gson = new Gson();
			String jsonOutletsIdList = gson.toJson(outletsIdListCont);
			Log.i("outletlist", " --> " + jsonOutletsIdList);
			editor.putString("outlestIdList", jsonOutletsIdList);
			editor.commit();

			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					RequestedResults item = (RequestedResults) listView
							.getItemAtPosition(position);
					try {
						String currentOutlet = sharedPreferences.getString(
								"selectedOutlet", null);
						String currentOutletId = sharedPreferences.getString(
								"selectedOutletId", null);
						String currentBrand = sharedPreferences.getString(
								"selectedBrand", null);
						String currentBranch = sharedPreferences.getString(
								"selectedBranch", null);

						if (sharedPreferences.contains("selectedOutletId")) {
							if (sharedPreferences.contains("myTrolley")
									&& !currentOutletId.equals(item.item_id)) {
								showDialogSelectBranch(item, position,
										currentOutlet + " - " + currentBrand
												+ " " + currentBranch + ".");
							} else {
								Log.i("Order title", " --> " + item.item_title);
								editor.putString("selectedOutlet",
										item.item_title);
								editor.putString("selectedOutletId",
										item.item_id);
								editor.commit();
								Intent intent = new Intent(
										ActivityStep01ListOutlets.this,
										ActivityStep02ListBrands.class);
								startActivity(intent);
							}
						} else {

							editor.putString("selectedOutlet", item.item_title);
							editor.putString("selectedOutletId", item.item_id);
							editor.commit();
							Intent intent = new Intent(
									ActivityStep01ListOutlets.this,
									ActivityStep02ListBrands.class);
							startActivity(intent);

							Log.i("Order title", " --> " + item.item_title);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});

			listView.setAdapter(myadapter);
			listView.setTextFilterEnabled(true);

		}

	}

	public void showDialogSelectBranch(final RequestedResults item,
			final int position, final String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityStep01ListOutlets.this);
		builder.setMessage("You will lose all the items in your trolley at "
				+ message + " Are you sure?");

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				layout_progressbar.setVisibility(View.VISIBLE);

				AnimatedGifImageView animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.animatedGifImageView));
				animatedGifImageView.setAnimatedGif(R.mipmap.loading_bar,
						TYPE.FIT_CENTER);
				animatedGifImageView.setImageResource(R.mipmap.loading_bar);

				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				editor.putString("selectedOutlet", item.item_title);
				editor.putString("selectedOutletId", item.item_id);
				editor.commit();

				Log.i("Order title", " --> " + item.item_title);
				Log.i("Order id", " --> " + item.item_id);

				Intent intent = new Intent(ActivityStep01ListOutlets.this,
						ActivityStep02ListBrands.class);
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
				ActivityStep01ListOutlets.this);
		builder.setMessage("You will lose all the items that you have selected. "
				+ message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				Intent intent = new Intent(ActivityStep01ListOutlets.this,
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
