package com.righthere.efam;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMyAccountLogin extends Activity {

	LinearLayout layout_progressbar;
	TextView errorMessage;
	Boolean error;
	EditText phoneNumber;
	Button getCode;
	EditText verifyCode;
	Button login;
	String prevActivity;
	String totalPrice;

	ArrayList<RequestedSimpleListOrders> MYORDERS;
	AdapterListOrders myadapter;

	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	public static String MY_PHONE_NUMBER;
	public static Typeface FONT_EKMUKTA_LIGHT;
	Bundle extras;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		layout_progressbar = (LinearLayout) findViewById(R.id.progressbar_view);
		sharedPreferences = getSharedPreferences(MY_SESSION,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		FONT_EKMUKTA_LIGHT = Typeface.createFromAsset(getAssets(),
				"fonts/ek_mukta/EkMukta-Light.ttf");
		extras = getIntent().getExtras();
		errorMessage = (TextView) findViewById(R.id.systemMessage);
		phoneNumber = (EditText) findViewById(R.id.phoneNumber);
		getCode = (Button) findViewById(R.id.getCode);
		verifyCode = (EditText) findViewById(R.id.verifyCode);
		login = (Button) findViewById(R.id.login);
		prevActivity = extras.getString("from");

		if (sharedPreferences.contains("loggedIn")) {
			if (sharedPreferences.contains("customerInfo")) {
				finish();
			} else {
				Intent intent = new Intent(ActivityMyAccountLogin.this,
						ActivityMyAccountProfile.class);
				intent.putExtra("from", prevActivity);
				startActivity(intent);
			}
		} else {
			// SET TEXT, APPLY FONTS, SETONCLICKLISTENERS
			new ApplyViewParamsTask().execute();
		}

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

		if (sharedPreferences.contains("loggedIn")) {
			finish();
		}
	}

	private class ApplyViewParamsTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			//layout_progressbar.setVisibility(View.VISIBLE);

			//AnimatedGifImageView animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.animatedGifImageView));
			//animatedGifImageView.setAnimatedGif(R.drawable.loading_bar,
					//TYPE.FIT_CENTER);
			// animatedGifImageView.setImageResource(R.drawable.loading_bar);
			super.onPreExecute();
		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return "Done";
		}

		protected void onPostExecute(String params) {
			layout_progressbar.setVisibility(View.GONE);

			// SET ONCLICKLISTENER
			getCode.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					String myPhoneNumber = phoneNumber.getText().toString();

					if (myPhoneNumber == null || myPhoneNumber.isEmpty()) {
						errorMessage.setVisibility(View.VISIBLE);
						errorMessage.setText("Enter your mobile number");
						phoneNumber.requestFocus();
					} else {
						// GENERATE RANDOM NUMBER BTWN 10000 AND 100000
						Random r = new Random();
						int randomNumber = r.nextInt(100000 - 10000) + 10000;
						String verificationCode = String.valueOf(randomNumber);

						SharedPreferences.Editor editor = sharedPreferences
								.edit();
						editor.putString("userVerificationCode",
								verificationCode);
						editor.putString("userPhoneNumber", myPhoneNumber);
						editor.commit();

						SmsManager sms = SmsManager.getDefault();
						try {
							sms.sendTextMessage(myPhoneNumber, null,
									verificationCode, null, null);
						} catch (IllegalArgumentException e) {

						}
						Toast.makeText(getBaseContext(),
								"Verification Code Sent!", Toast.LENGTH_LONG)
								.show();
					}

				}
			});

			login.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					errorMessage.setVisibility(View.GONE);// Hide all errors
															// before displaying
															// new ones
					error = false;
					String vCode = verifyCode.getText().toString();
					String phone = phoneNumber.getText().toString();
					String savedVerifyCode = sharedPreferences.getString(
							"userVerificationCode", null);

					// VALIDATE PHONE
					if (phone == null || phone.isEmpty()) {
						errorMessage.setVisibility(View.VISIBLE);
						errorMessage.setText("Enter your phone number");
						phoneNumber.requestFocus();
						error = true;
					}

					// VALIDATE CODE
					else if (!sharedPreferences
							.contains("userVerificationCode")) {
						errorMessage.setVisibility(View.VISIBLE);
						errorMessage
								.setText("Invalid verification code. Click on the button below to get code");
						verifyCode.requestFocus();
						error = true;
					} else if (vCode.compareTo(savedVerifyCode) != 0) {
						errorMessage.setVisibility(View.VISIBLE);
						errorMessage
								.setText("Invalid verification code. Check your sms for the valid code");
						verifyCode.requestFocus();
						error = true;
					}

					else if (error == false) {

						sharedPreferences.edit().remove("userVerificationCode")
								.commit();
						editor.putString("loggedIn", "Yes").commit();

						if (extras != null) {
							if (sharedPreferences.contains("customerInfo")) {
								Class callerClass;
								try {
									callerClass = Class
											.forName("com.redhering.nunuaraha."
													+ prevActivity);
									Intent intent = new Intent(
											ActivityMyAccountLogin.this,
											callerClass);
									startActivity(intent);
								} catch (ClassNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								Intent intent = new Intent(
										ActivityMyAccountLogin.this,
										ActivityMyAccountProfile.class);
								intent.putExtra("from", prevActivity);
								startActivity(intent);
							}
						} else {
							finish();
						}
					}

					Log.i("Error", " --> " + prevActivity);
				}
			});

		}

	}
}
