package com.redhering.nunuaraha;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class CopyOfActivityMyAccountProfile extends Activity {
	SQLiteDatabase nunuaRahaDatabase;
	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	Gson gson;
	Bundle extras;
	ArrayAdapter<String> dataAdapter;
	TextView app_name;
	LinearLayout layout_progressbar;
	Button menuIcon;
	TextView headerText;
	Button shoppingButton;
	Button cartButton;

	Typeface EkMukta_Light;

	TextView customerFname;
	TextView customerLname;
	TextView customerEmail;
	TextView customerPhone;
	Spinner customerGender;
	private TextView customerDOB;
	Button saveCustomerInfo;
	ImageButton editCustomerInfo;
	TextView errorMessage;
	Boolean error;
	TextView cartButtonNotification;

	private DatePicker date_picker;
	Button selectGenderBtn;

	private int year;
	private int month;
	private int day;

	static final int DATE_DIALOG_ID = 100;

	public static String CUSTOMER_PHONE_NUMBER;
	public static String CUSTOMER_FIRST_NAME;
	public static String CUSTOMER_LAST_NAME;
	public static String CUSTOMER_EMAIL;
	public static String CUSTOMER_GENDER;
	public static String CUSTOMER_DOB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_profile);
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn
																				// animation
		layout_progressbar = (LinearLayout) findViewById(R.id.progressbar_view);
		menuIcon = (Button) findViewById(R.id.menu_icon);
		sharedPreferences = getSharedPreferences(MY_SESSION,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		gson = new Gson();
		extras = getIntent().getExtras();
		headerText = (TextView) findViewById(R.id.headerText);
		app_name = (TextView) findViewById(R.id.app_name);
		shoppingButton = (Button) findViewById(R.id.shoppingButton);
		cartButton = (Button) findViewById(R.id.cartButton);
		customerFname = (TextView) findViewById(R.id.customerFname);
		customerLname = (TextView) findViewById(R.id.customerLname);
		customerEmail = (TextView) findViewById(R.id.customerEmail);
		customerGender = (Spinner) findViewById(R.id.customerGender);
		saveCustomerInfo = (Button) findViewById(R.id.saveCustomerInfo);
		editCustomerInfo = (ImageButton) findViewById(R.id.editCustomerInfo);
		customerPhone = (TextView) findViewById(R.id.customerPhone);
		cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);
		EkMukta_Light = Typeface.createFromAsset(getAssets(),
				"fonts/ek_mukta/EkMukta-Light.ttf");
		errorMessage = (TextView) findViewById(R.id.systemMessage);
		customerDOB = (TextView) findViewById(R.id.customerDOB);

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, CopyOfActivityMyAccountProfile.this,
				sharedPreferences);

		if (sharedPreferences.contains("loggedIn")) {
			CUSTOMER_PHONE_NUMBER = sharedPreferences.getString(
					"userPhoneNumber", null);

			if (!sharedPreferences.contains("customerInfo")) {
				CUSTOMER_FIRST_NAME = sharedPreferences.getString(
						"customerFirstName", null);
				CUSTOMER_LAST_NAME = sharedPreferences.getString(
						"customerLastName", null);
				CUSTOMER_EMAIL = sharedPreferences.getString("customerEmail",
						null);
			}

			new HttpAsyncTask()
					.execute("http://www.e-fam.com/mobile_trolley_app/getcustomerinfo.php");

			List<String> list = new ArrayList<String>();
			list.add("Select Gender");
			list.add("Female");
			list.add("Male");

			dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, list);
			dataAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			customerGender.setAdapter(dataAdapter);

			setCurrentDate();
			addButtonListener();

			new ApplyViewParamsTask().execute();

		} else {
			Intent intent = new Intent(CopyOfActivityMyAccountProfile.this,
					ActivityMyAccountLogin.class);
			intent.putExtra("from", "ActivityMyAccountProfile");
			startActivity(intent);
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
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn
																				// animation

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
			headerText.setText("My Profile");
			customerPhone.setText(CUSTOMER_PHONE_NUMBER);

			saveCustomerInfo.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					errorMessage.setVisibility(View.GONE);// Hide all errors
															// before displaying
															// new ones
					error = false;

					CUSTOMER_FIRST_NAME = customerFname.getText().toString();
					CUSTOMER_LAST_NAME = customerLname.getText().toString();
					CUSTOMER_EMAIL = customerEmail.getText().toString();
					CUSTOMER_GENDER = customerGender.getSelectedItem()
							.toString();
					CUSTOMER_DOB = customerDOB.getText().toString();

					if (CUSTOMER_FIRST_NAME == null
							|| CUSTOMER_FIRST_NAME.isEmpty()) {
						errorMessage.setVisibility(View.VISIBLE);
						errorMessage.setText("Fill your first name");
						customerFname.requestFocus();
						error = true;
					} else if (CUSTOMER_LAST_NAME == null
							|| CUSTOMER_LAST_NAME.isEmpty()) {
						errorMessage.setVisibility(View.VISIBLE);
						errorMessage.setText("Fill in your last name");
						customerLname.requestFocus();
						error = true;
					} else if (CUSTOMER_EMAIL == null
							|| CUSTOMER_EMAIL.isEmpty()) {
						errorMessage.setVisibility(View.VISIBLE);
						errorMessage.setText("Fill in your email address");
						customerEmail.requestFocus();
						error = true;
					} else if (CUSTOMER_GENDER.equals("Select Gender")) {
						errorMessage.setVisibility(View.VISIBLE);
						errorMessage.setText("Select your gender");
						customerGender.requestFocus();
						error = true;
					} else if (CUSTOMER_DOB == null || CUSTOMER_DOB.isEmpty()) {
						errorMessage.setVisibility(View.VISIBLE);
						errorMessage.setText("Select you date of birth");
						customerEmail.requestFocus();
						error = true;
					} else if (error == false) {
						// editor.putString("customerInfo","Yes").commit();
						new HttpAsyncTask()
								.execute("http://www.e-fam.com/mobile_trolley_app/savecustomerbasicinfo.php");
					}

				}
			});

			editCustomerInfo.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					customerFname.setEnabled(true);
					customerLname.setEnabled(true);
					customerEmail.setEnabled(true);

					customerGender.setEnabled(true);
					customerGender.setClickable(true);

					selectGenderBtn.setVisibility(View.VISIBLE);
					saveCustomerInfo.setVisibility(View.VISIBLE);
					editCustomerInfo.setVisibility(View.GONE);
				}
			});

		}

	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			layout_progressbar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			layout_progressbar.setVisibility(View.GONE);
			super.onPostExecute(result);

			// Toast.makeText(getBaseContext(), "Pick up options!",
			// Toast.LENGTH_LONG).show();
			try {
				JSONArray jsonArr = new JSONArray(result);
				for (int i = 0; i < jsonArr.length(); i++) {

					JSONObject jsonObj = jsonArr.getJSONObject(i);
					String querytype = jsonObj.getString("querytype");

					if (querytype.equals("insert info")) {
						String message = jsonObj.getString("message");
						if (message.equals("success")) {
							finish();
							startActivity(getIntent());
						}

						if (message.equals("fail")) {
							Toast.makeText(
									getBaseContext(),
									"Error saving your info. Kindly contact our support team.",
									Toast.LENGTH_LONG).show();
						}

					} else {

						String customerInfo = jsonObj.getString("info");
						if (customerInfo.equals("0")) {
							customerFname.setEnabled(true);
							customerLname.setEnabled(true);
							customerEmail.setEnabled(true);
							saveCustomerInfo.setVisibility(View.VISIBLE);

						} else {

							JSONObject customerInfoObj = new JSONObject(
									customerInfo);
							String customer_fname = customerInfoObj
									.getString("fname");
							String customer_lname = customerInfoObj
									.getString("lname");
							String customer_email = customerInfoObj
									.getString("email");
							String customer_gender = customerInfoObj
									.getString("gender");
							String customer_dob = customerInfoObj
									.getString("dob");

							// Add shared preferences
							editor.putString("customerInfo", "Yes");
							editor.putString("customerFirstName",
									customer_fname);
							editor.putString("customerLastName", customer_lname);
							editor.putString("customerEmail", customer_email);
							editor.putString("customerGender", customer_gender);
							editor.putString("customerDob", customer_dob);
							editor.commit();

							customerFname.setText(customer_fname);
							customerLname.setText(customer_lname);
							customerEmail.setText(customer_email);
							customerDOB.setText(customer_dob);
							customerGender.setSelection(dataAdapter
									.getPosition(customer_gender));
							editCustomerInfo.setVisibility(View.VISIBLE);

							if (getIntent().hasExtra("from")) {
								String prevActivity = extras.getString("from");
								// Log.i("prev activity"," --> "+prevActivity);
								// this activity was requested from an activity
								// in the shopping process and thus after
								// filling in the customer info
								// it needs to be redirected back to the
								// shopping process that requested for it
								Class callerClass;
								try {
									callerClass = Class
											.forName("com.redhering.nunuaraha."
													+ prevActivity);
									Intent intent = new Intent(
											CopyOfActivityMyAccountProfile.this,
											callerClass);
									startActivity(intent);
								} catch (ClassNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						}
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected String doInBackground(String... urls) {
			return GET(urls[0]);
		}

	}

	public static String GET(String url) {
		InputStream inputStream = null;
		String result = "";

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("phone",
				CUSTOMER_PHONE_NUMBER));
		nameValuePairs
				.add(new BasicNameValuePair("fname", CUSTOMER_FIRST_NAME));
		nameValuePairs.add(new BasicNameValuePair("lname", CUSTOMER_LAST_NAME));
		nameValuePairs.add(new BasicNameValuePair("email", CUSTOMER_EMAIL));
		nameValuePairs.add(new BasicNameValuePair("gender", CUSTOMER_GENDER));
		nameValuePairs.add(new BasicNameValuePair("dob", CUSTOMER_DOB));

		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// make GET request to the given URL
			HttpResponse response = httpclient.execute(httppost);

			// receive response as inputStream
			inputStream = response.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
			} else {
				result = "Did not work!";
			}

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	public void setCurrentDate() {

		final Calendar calendar = Calendar.getInstance();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);

		// set current date into textview
		/**
		 * customerDOB.setText(new StringBuilder() // Month is 0 based, so you
		 * have to add 1 .append(month + 1).append("-") .append(day).append("-")
		 * .append(year).append(" "));
		 **/
	}

	public void addButtonListener() {
		selectGenderBtn = (Button) findViewById(R.id.selectGenderBtn);
		selectGenderBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// set selected date into Text View
			customerDOB.setText(new StringBuilder().append(month + 1)
					.append("-").append(day).append("-").append(year)
					.append(" "));

		}
	};

}
