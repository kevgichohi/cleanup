package com.righthere.efam;

import java.io.File;
import java.util.ArrayList;

import com.righthere.efam.AnimatedGifImageView.TYPE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityStep09ListDeliveryOptions extends Activity {
	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	public static ArrayList<RequestedResultsSimpleList> DELIVERYOPTIONS;
	AdapterSimpleList myadapter;

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
	public static String USER_PHONE_NUMBER;
	Typeface EkMukta_Light;
	TextView app_name;
	Button menuIcon;
	Button shoppingButton;
	Button cartButton;
	TextView cartButtonNotification;
	TextView headerText;
	Button optionPickup;
	Button optionDelivery;
	Button optionDeliveryFoot;
	ImageView shopLogoview;
	String extStorageDirectory;
	Bitmap bm;

	Button proceedToCheckout;
	Button clearcart;

	private Button title1;
	private TextView text1;

	private Button title2;
	private TextView text2;

	private ImageView headerTextimage2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delivery_options);
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn
																				// animation
		initViews();

		if (sharedPreferences.contains("loggedIn")
				&& sharedPreferences.contains("customerInfo")) {
			headerText.setText("Collection Preference");

			headerTextimage2 = (ImageView) findViewById(R.id.headerTextimage);
			headerTextimage2.setVisibility(View.VISIBLE);
			headerTextimage2.setImageResource(R.mipmap.collection);

			// LOAD QUICKLINKS
			HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
			helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
					cartButtonNotification,
					ActivityStep09ListDeliveryOptions.this, sharedPreferences);

			// DISPLAY OUTLET LOGO
			// File file = new File(extStorageDirectory+"/NunuaRaha/",
			// SELECTED_BRAND_LOGO);
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			// Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
			// options);
			// shopLogoview.setImageBitmap(bitmap);

			new ApplyViewParamsTask().execute();

		} else {
			Intent intent = new Intent(ActivityStep09ListDeliveryOptions.this,
					ActivityMyAccountLogin.class);
			intent.putExtra("from", "ActivityStep09ListDeliveryOptions");
			startActivity(intent);
		}
	}

	private void initViews() {
		extStorageDirectory = Environment.getExternalStorageState().toString();
		extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();

		layout_progressbar = (LinearLayout) findViewById(R.id.progressbar_view);
		headersection = (RelativeLayout) findViewById(R.id.header);
		sharedPreferences = getSharedPreferences(MY_SESSION,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
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
		USER_PHONE_NUMBER = sharedPreferences
				.getString("userPhoneNumber", null);
		EkMukta_Light = Typeface.createFromAsset(getAssets(),
				"fonts/ek_mukta/EkMukta-Light.ttf");
		app_name = (TextView) findViewById(R.id.app_name);
		menuIcon = (Button) findViewById(R.id.menu_icon);
		headerText = (TextView) findViewById(R.id.headerText);
		shoppingButton = (Button) findViewById(R.id.shoppingButton);
		cartButton = (Button) findViewById(R.id.cartButton);
		cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);
		optionPickup = (Button) findViewById(R.id.optionPickup);
		optionDelivery = (Button) findViewById(R.id.optionDelivery);
		optionDeliveryFoot = (Button) findViewById(R.id.optionDeliveryFoot);
		title1 = (Button) findViewById(R.id.title1);
		title2 = (Button) findViewById(R.id.title2);
		text1 = (TextView) findViewById(R.id.text1);
		text2 = (TextView) findViewById(R.id.text2);
		// shopLogoview = (ImageView) findViewById(R.id.shopLogo);

		proceedToCheckout = (Button) findViewById(R.id.proceedToCheckout);
		clearcart = (Button) findViewById(R.id.clearcart);

		clearcart.setVisibility(View.VISIBLE);
		clearcart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (sharedPreferences.contains("myTrolley")) {
					try {
						showDialogEmptyTrolley("Are you sure?");
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					Toast.makeText(ActivityStep09ListDeliveryOptions.this,
							"No items in your cart to clear!",
							Toast.LENGTH_LONG).show();
				}

			}
		});

		proceedToCheckout.setVisibility(View.VISIBLE);
		proceedToCheckout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Intent intent = new Intent(ActivityMyAccountAddresses.this,
				// ActivityStep09ListCart.class);
				// startActivity(intent);
				if (sharedPreferences.contains("myTrolley")) {
					// Intent intent = new
					// Intent(ActivityMyAccountAddresses.this,
					// ActivityStep09ListDeliveryOptions.class);
					// startActivity(intent);
					Toast.makeText(ActivityStep09ListDeliveryOptions.this,
							"click your cart to confirm first",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ActivityStep09ListDeliveryOptions.this,
							"No items in your cart !", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		onBtnClick();
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

	private class ApplyViewParamsTask extends AsyncTask<String, Void, String> {

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
			layout_progressbar.setVisibility(View.GONE);

			optionPickup.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					editor.putString("deliverShopping", "No").commit();
					Intent intent = new Intent(
							ActivityStep09ListDeliveryOptions.this,
							ActivityStep10ListPaymentOptions.class);
					startActivity(intent);
				}
			});

			optionDelivery.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					editor.putString("deliverShopping", "Yes").commit();
					editor.putString("deliveryMethod", "Home").commit();
					Intent intent = new Intent(
							ActivityStep09ListDeliveryOptions.this,
							ActivityStep09ListDeliveryOptionsAddresses.class);
					startActivity(intent);
				}
			});

			optionDeliveryFoot.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					editor.putString("deliverShopping", "Yes").commit();
					editor.putString("deliveryMethod", "Foot").commit();
					Intent intent = new Intent(
							ActivityStep09ListDeliveryOptions.this,
							ActivityStep09ListDeliveryOptionsAddresses.class);
					startActivity(intent);
				}
			});

		}

	}

	public void onBtnClick() {

		title1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				text1.setVisibility((text1.getVisibility() == View.VISIBLE) ? View.GONE
						: View.VISIBLE);
				text2.setVisibility(View.GONE);

			}
		});

		title2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				text2.setVisibility((text1.getVisibility() == View.VISIBLE) ? View.GONE
						: View.VISIBLE);
				text1.setVisibility(View.GONE);

			}
		});
	}

	public void showDialogEmptyTrolley(final String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityStep09ListDeliveryOptions.this);
		builder.setMessage("You will lose all the items that you have selected. "
				+ message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				Intent intent = new Intent(
						ActivityStep09ListDeliveryOptions.this,
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
