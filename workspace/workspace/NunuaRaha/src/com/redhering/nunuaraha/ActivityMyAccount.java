package com.redhering.nunuaraha;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class ActivityMyAccount extends Activity {
	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	Gson gson;

	TextView app_name;
	LinearLayout layout_progressbar;
	ImageView menuIcon;
	TextView headerText;
	Button homeButton;
	Button accountButton;
	Button shoppingButton;
	Button cartButton;
	Button myOrders;
	Button accountInfo;

	Button proceedToCheckout;
	Button clearcart;
	public static Typeface FONT_EKMUKTA_LIGHT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);
		layout_progressbar = (LinearLayout) findViewById(R.id.progressbar_view);
		menuIcon = (ImageView) findViewById(R.id.menu_icon);
		sharedPreferences = getSharedPreferences(MY_SESSION,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		gson = new Gson();
		FONT_EKMUKTA_LIGHT = Typeface.createFromAsset(getAssets(),
				"fonts/ek_mukta/EkMukta-Light.ttf");
		headerText = (TextView) findViewById(R.id.headerText);
		app_name = (TextView) findViewById(R.id.app_name);
		// homeButton = (Button) findViewById(R.id.homeButton);
		// accountButton = (Button) findViewById(R.id.accountButton);
		shoppingButton = (Button) findViewById(R.id.shoppingButton);
		cartButton = (Button) findViewById(R.id.cartButton);
		myOrders = (Button) findViewById(R.id.myOrders);
		accountInfo = (Button) findViewById(R.id.accountInfo);

		proceedToCheckout = (Button) findViewById(R.id.proceedToCheckout);
		clearcart = (Button) findViewById(R.id.clearcart);

		TextView cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);

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
					Toast.makeText(ActivityMyAccount.this,
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
					Toast.makeText(ActivityMyAccount.this,
							"click your cart to confirm first",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ActivityMyAccount.this,
							"No items in your cart !", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		// SET TEXT, APPLY FONTS, SETONCLICKLISTENERS
		new ApplyViewParamsTask().execute();

	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public void onResume() {
		super.onResume(); // Always call the superclass method first

		// SET NOTIFICATION FOR THE NUMBER OF ITEMS CURRENTLY IN THE TROLLEY
		TextView cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);
		if (sharedPreferences.contains("myTrolley")) {
			String jsonMyCartString = sharedPreferences.getString("myTrolley",
					null);
			JSONObject jsonMyCartObject;
			try {
				jsonMyCartObject = new JSONObject(jsonMyCartString);
				Integer numberOfItemsInMyTrolley = 0;
				Iterator<String> loop = jsonMyCartObject.keys();
				while (loop.hasNext()) {
					String key = loop.next();
					try {
						String unitsValue = jsonMyCartObject.getString(key);
						numberOfItemsInMyTrolley = numberOfItemsInMyTrolley
								+ Integer.parseInt(unitsValue);
					} catch (JSONException e) {
						// Something went wrong!
					}
				}
				cartButtonNotification.setText(numberOfItemsInMyTrolley
						.toString());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			cartButtonNotification.setText("0");
			cartButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getBaseContext(), "Your cart is empty!",
							Toast.LENGTH_LONG).show();
				}
			});
		}
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
			layout_progressbar.setVisibility(View.GONE);
			menuIcon.setImageResource(R.drawable.ic_account_grey);
			headerText.setText("My Account");
			app_name.setTypeface(FONT_EKMUKTA_LIGHT);
			accountButton.setTextColor(getResources().getColor(R.color.green));

			// SET ONCLICKLISTENER
			myOrders.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent intent = new Intent(ActivityMyAccount.this,
							ActivityMyAccountOrders.class);
					startActivity(intent);

				}
			});

			accountInfo.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent intent = new Intent(ActivityMyAccount.this,
							ActivityMyAccountProfile.class);
					startActivity(intent);

				}
			});

			homeButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent intent = new Intent(ActivityMyAccount.this,
							ActivityMain.class);
					startActivity(intent);

				}
			});

			shoppingButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent intent = new Intent(ActivityMyAccount.this,
							ActivityStep01ListOutlets.class);
					if (sharedPreferences.contains("myTrolley")) {
						intent = new Intent(ActivityMyAccount.this,
								ActivityStep05ListAisles.class);
					}
					startActivity(intent);
				}
			});

			// SET NOTIFICATION FOR THE NUMBER OF ITEMS CURRENTLY IN THE TROLLEY
			TextView cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);
			if (sharedPreferences.contains("myTrolley")) {
				String jsonMyCartString = sharedPreferences.getString(
						"myTrolley", null);
				JSONObject jsonMyCartObject;
				try {
					jsonMyCartObject = new JSONObject(jsonMyCartString);
					Integer numberOfItemsInMyTrolley = 0;
					Iterator<String> loop = jsonMyCartObject.keys();
					while (loop.hasNext()) {
						String key = loop.next();
						try {
							String unitsValue = jsonMyCartObject.getString(key);
							numberOfItemsInMyTrolley = numberOfItemsInMyTrolley
									+ Integer.parseInt(unitsValue);
						} catch (JSONException e) {
							// Something went wrong!
						}
					}
					cartButtonNotification.setText(numberOfItemsInMyTrolley
							.toString());
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				cartButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(ActivityMyAccount.this,
								ActivityStep08ListCart.class);
						startActivity(intent);

					}
				});
			} else {
				cartButtonNotification.setText("0");
				cartButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(getBaseContext(), "Your cart is empty!",
								Toast.LENGTH_LONG).show();
					}
				});
			}

		}

	}

	public void showDialogEmptyTrolley(final String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityMyAccount.this);
		builder.setMessage("You will lose all the items that you have selected. "
				+ message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				Intent intent = new Intent(ActivityMyAccount.this,
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
