package com.righthere.efam;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.righthere.efam.AnimatedGifImageView.TYPE;
import com.righthere.efam.adapters.DynamicListView;
import com.righthere.efam.interfaces.Constants;

public class ActivityStep11MyReceipt extends Activity {
	SQLiteDatabase nunuaRahaDatabase;
	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	Gson gson;

	ArrayList<RequestedResultsReceipt> MYRECEIPT;
	AdapterMyReceipt myadapterreceipt;

	LinearLayout layout_progressbar;
	RelativeLayout headersection,centerwrap,footersection,subtitlesection;

	ListView listView;
	public Integer TOTAL = 0;
	public static String SELECTED_OUTLET_TITLE,SELECTED_BRANCH_ID,SELECTED_BRANCH_TITLE,SELECTED_BRAND_ID,SELECTED_BRAND_TITLE,SELECTED_BRAND_LOGO
	,SELECTED_LOCATION,SELECTED_LOCATION_ID,SELECTED_DELIVERY_CHARGES,CUSTOMER_FIRST_NAME,CUSTOMER_LAST_NAME,CUSTOMER_PHONE_NUMBER,CUSTOMER_EMAIL_ADDRESS
	,CUSTOMER_ADDRESS,Ovaraltatol,total_items,totalitem2,vat_able,vatrate2,vatable2,vat_amt,vatamt2;

	Boolean error;
	Typeface EkMukta_Light;
    Button menuIcon,shoppingButton,cartButton,paycash,paycreditcard,paymobilemoney,checkout,backToAisles;
    TextView app_name, headerText,shopDetails,userName,emailAddress,totalovaraltext,totalitem,vatrate,vatable, vatamt, phoneNumber, delivery,errorMessage;

	ImageView shopLogoview;
	String extStorageDirectory;
	Bitmap bm;
	TextView deliveryinfo;
	TextView service;
	TextView reciepttotal;
	TextView reciepttotaltext;
	TextView cartButtonNotification;

	Button proceedToCheckout;
	Button clearcart;
	private String SELECTED_OUTLET_ID;
	private FirebaseAnalytics firebaseAnalytics;
	private String reorderif2;
    Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receipt);
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn

        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        bundle = new Bundle();

		MYRECEIPT = new ArrayList<RequestedResultsReceipt>();
		myadapterreceipt = new AdapterMyReceipt(ActivityStep11MyReceipt.this,
				MYRECEIPT);																	// animation

		initViews();

        // LIST VIEW
        new populateListViewTask().execute();

		new ApplyViewParamsTask().execute();

		setListViewHeightBasedOnChildren(listView);
		listView.setAdapter(myadapterreceipt);
	//	boolean resized = DynamicListView.setListViewHeightBasedOnItems(listView);
	//	Log.d("AS11MR_resized", String.valueOf(resized));
	}

	private void initViews() {
		extStorageDirectory = Environment.getExternalStorageState().toString();
		extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();

		listView = (ListView) findViewById(R.id.receiptView);
		layout_progressbar = (LinearLayout) findViewById(R.id.progressbar_view);
		headersection = (RelativeLayout) findViewById(R.id.header);
		footersection = (RelativeLayout) findViewById(R.id.footer);
		subtitlesection = (RelativeLayout) findViewById(R.id.subHeader);
		sharedPreferences = getSharedPreferences(MY_SESSION,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		gson = new Gson();
		EkMukta_Light = Typeface.createFromAsset(getAssets(),
				"fonts/ek_mukta/EkMukta-Light.ttf");
		app_name = (TextView) findViewById(R.id.app_name);
		headerText = (TextView) findViewById(R.id.headerText);
		menuIcon = (Button) findViewById(R.id.menu_icon);
		paycash = (Button) findViewById(R.id.paycash1);
		paycreditcard = (Button) findViewById(R.id.paycreditcard1);
		paymobilemoney = (Button) findViewById(R.id.paymobilemoney1);
		backToAisles = (Button) findViewById(R.id.backToAisles);
		errorMessage = (TextView) findViewById(R.id.errors);
		shopDetails = (TextView) findViewById(R.id.shopDetails);
		userName = (TextView) findViewById(R.id.userName);
		emailAddress = (TextView) findViewById(R.id.email);
		phoneNumber = (TextView) findViewById(R.id.phone);
		totalovaraltext = (TextView) findViewById(R.id.totalovaraltext);
		totalitem = (TextView) findViewById(R.id.totalitem);
		vatrate = (TextView) findViewById(R.id.vatrate);
		vatable = (TextView) findViewById(R.id.vatable);
		vatamt = (TextView) findViewById(R.id.vatamt);
		delivery = (TextView) findViewById(R.id.delivery);
		shoppingButton = (Button) findViewById(R.id.shoppingButton);
		cartButton = (Button) findViewById(R.id.cartButton);
		service = (TextView) findViewById(R.id.service);
		deliveryinfo = (TextView) findViewById(R.id.deliveryinfo);
		reciepttotal = (TextView) findViewById(R.id.reciepttotal);
		reciepttotaltext = (TextView) findViewById(R.id.reciepttotaltext);

		cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);

		proceedToCheckout = (Button) findViewById(R.id.proceedToCheckout);
		clearcart = (Button) findViewById(R.id.clearcart);
		// shopLogoview = (ImageView) findViewById(R.id.shopLogo);

		// SELECTED_BRAND_ID = sharedPreferences.getString("selectedBrandId",
		// null);
		SELECTED_BRAND_TITLE = sharedPreferences.getString("selectedBrand",
				null);
		SELECTED_BRAND_LOGO = sharedPreferences.getString("selectedBrandLogo",
				null);
		// SELECTED_BRANCH_ID = sharedPreferences.getString("selectedBranchId",
		// null);
		// SELECTED_BRANCH_TITLE = sharedPreferences.getString("selectedBranch",
		// null);
		SELECTED_LOCATION_ID = sharedPreferences.getString(
				"selectedLocationId", null);
		SELECTED_LOCATION = sharedPreferences.getString("selectedLocation",
				null);
		SELECTED_DELIVERY_CHARGES = sharedPreferences.getString(
				"selectedBranchDeliveryCharges", null);
		CUSTOMER_FIRST_NAME = sharedPreferences.getString("customerFirstName",
				null);
		CUSTOMER_LAST_NAME = sharedPreferences.getString("customerLastName",
				null);
		CUSTOMER_EMAIL_ADDRESS = sharedPreferences.getString("customerEmail",
				null);
		CUSTOMER_PHONE_NUMBER = sharedPreferences.getString("userPhoneNumber",
				null);
		CUSTOMER_ADDRESS = sharedPreferences.getString("deliveryAddress", null);
		SELECTED_OUTLET_TITLE = sharedPreferences.getString("selectedOutlet",
				null);

		// reorderif2 = sharedPreferences.getString("reorder", null);
		if (sharedPreferences.contains("reorderif")) {

			SELECTED_BRANCH_ID = sharedPreferences.getString("newbranchid",
					null);
			SELECTED_BRAND_ID = sharedPreferences.getString("newbrandid", null);
			SELECTED_OUTLET_ID = sharedPreferences.getString("newoutletid",
					null);
			SELECTED_BRANCH_TITLE = sharedPreferences.getString("newbranch",
					null);

		} else {
			SELECTED_BRANCH_ID = sharedPreferences.getString(
					"selectedBranchId", null);
			SELECTED_BRAND_ID = sharedPreferences.getString("selectedBrandId",
					null);
			SELECTED_OUTLET_ID = sharedPreferences.getString(
					"selectedOutletId", null);
			SELECTED_BRANCH_TITLE = sharedPreferences.getString(
					"selectedBranch", null);
		}

		Log.i("SELECTED_BRANCH_ID", SELECTED_BRANCH_ID);
		Log.i("SELECTED_BRAND_ID", SELECTED_BRAND_ID);
		Log.i("selectedOutletId2", SELECTED_OUTLET_ID);
		Log.i("SELECTED_BRANCH_TITLE", SELECTED_BRANCH_TITLE);

		Ovaraltatol = sharedPreferences.getString("myTrolleyTotal", null);
    //    Log.i("Ovaraltatol ", Ovaraltatol);
		totalitem2 = sharedPreferences.getString("totalitem", null);

//		Log.i("totalitem2 ", totalitem2);
		vatrate2 = sharedPreferences.getString("vatrate", null);
    //    Log.i("vatrate2 ", vatrate2);
		vatable2 = sharedPreferences.getString("vatable", null);
     //   Log.i("vatable2 ", vatable2);
		vatamt2 = sharedPreferences.getString("vatamt", null);
     //   Log.i("vatamt2 ", vatamt2);

	//	Log.i("totalitem2 ", totalitem2);
		vatrate2 = sharedPreferences.getString("vatrate", null);
    //  Log.i("vatrate2 ", vatrate2);
		vatable2 = sharedPreferences.getString("vatable", null);
     //   Log.i("vatable2 ", vatable2);
		vatamt2 = sharedPreferences.getString("vatamt", null);
    //    Log.i("vatamt2 ", vatamt2);


		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, ActivityStep11MyReceipt.this,
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
					Toast.makeText(ActivityStep11MyReceipt.this,
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
					Toast.makeText(ActivityStep11MyReceipt.this,
							"click your cart to confirm first",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(ActivityStep11MyReceipt.this,
							"No items in your cart !", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		ImageView headerTextimage2 = (ImageView) findViewById(R.id.headerTextimage);
		headerTextimage2.setVisibility(View.VISIBLE);
		headerTextimage2.setImageResource(R.mipmap.superheading);

		headerText.setText(SELECTED_BRANCH_TITLE);

		if (sharedPreferences.contains("paymentOption")) {
			String paymentOption = sharedPreferences.getString("paymentOption",
					null);
			Log.i("paymentOption", " --> " + paymentOption);

			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
			String strDate1 = sdf1.format(c.getTime());
			Random rand = new Random();
			int max = 1000;

			int min = 10;
			int randomNum = min + (int) (Math.random() * ((max - min) + 1));
			String OrderTransactionRef = randomNum + "_" + strDate1;

			editor.putString("transactionRef", OrderTransactionRef).commit();
			if (paymentOption.equals("Online")) {
				paycreditcard.setVisibility(View.VISIBLE);
				paymobilemoney.setVisibility(View.VISIBLE);
				Log.i("online", " --> " + paymentOption);
				// Integer OrderTotal = 1000;

				DefaultHttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"https://secure.3gdirectpay.com/API/v5/");

				Calendar cs = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String strDate = sdf.format(cs.getTime());
				String xmlTotalPrice3 = sharedPreferences.getString(
						"reciepttotaltextdata", null);
				String customerphone = sharedPreferences.getString(
						"userPhoneNumber", null);
				String customerfname = sharedPreferences.getString(
						"customerFirstName", null);
				String customerlname = sharedPreferences.getString(
						"customerLastName", null);
				String customeremail = sharedPreferences.getString(
						"customerEmail", null);
				String customercountry = sharedPreferences
						.getString("KE", null);

				// Create Payment Token
				// String OrderTransactionRef = "1006";

			//	String OrderCompanyToken = "68B90B5E-25F6-4146-8AB1-C7A3A0C41A7F";
				editor.putString("companyToken", Constants.COMPANY_TOKEN).commit();

				// XML example to send via Web Service.
				String createToken = "<API3G>" + "<CompanyToken>"
						+ Constants.COMPANY_TOKEN + "</CompanyToken>"
						+ "<Request>createToken</Request>" + "<Transaction>"
						+ "<PaymentAmount>" + xmlTotalPrice3
						+ "</PaymentAmount>"
						+ "<PaymentCurrency>KES</PaymentCurrency>"
						+ "<CompanyRef>" + OrderTransactionRef
						+ "</CompanyRef>"
						+ "<CompanyRefUnique>0</CompanyRefUnique>"
						+ "</Transaction>" + "<Services>" + "<Service>"
						+ "<ServiceType>" + Constants.SERVICE_TYPE + "</ServiceType>"
						+ "<ServiceDescription>Shopping at"
						+ SELECTED_BRAND_TITLE + SELECTED_BRANCH_TITLE
						+ SELECTED_OUTLET_TITLE + "</ServiceDescription>"
						+ "<ServiceDate>" + strDate + "</ServiceDate>"
						+ "</Service>" + "</Services>" + "<customerEmail>"
						+ customeremail + "</customerEmail>"
						+ "<customerFirstName>" + customerfname
						+ "</customerFirstName>" + "<customerLastName>"
						+ customerlname + "</customerLastName>"
						+ "<customerPhone>" + customerphone
						+ "</customerPhone>"
						+ "<customerDialCode>KE</customerDialCode>"
						+ "<customerCountry>KE</customerCountry>" +

						"</API3G>";
				
				Log.i("createToken"," --> "+createToken);

				try {
					StringEntity se = new StringEntity(createToken, HTTP.UTF_8);
					se.setContentType("text/xml");
					httppost.setEntity(se);

					HttpResponse httpresponse = httpclient.execute(httppost);
					HttpEntity resEntity = httpresponse.getEntity();
					String xmlStringResults = EntityUtils.toString(resEntity);

					 Log.i("Token XML"," --> "+xmlStringResults);

					DocumentBuilderFactory factory = DocumentBuilderFactory
							.newInstance();
					DocumentBuilder db;
					try {
						db = factory.newDocumentBuilder();
						InputSource inStream = new InputSource();
						inStream.setCharacterStream(new StringReader(
								xmlStringResults));
						org.w3c.dom.Document doc;
						try {
							doc = db.parse(inStream);
							doc.getDocumentElement().normalize();

							NodeList nListResult = doc
									.getElementsByTagName("Result");
							for (int i = 0; i < nListResult.getLength(); i++) {
								Node nNode = nListResult.item(i);
								String nodeName = nNode.getNodeName();
								String nodeText = nListResult.item(i)
										.getTextContent();

								// System.out.println("\nCurrent Element :" +
								// nodeName);
								// System.out.println("\nCurrent Element Value:"
								// + nodeText);

							}

							NodeList nListResultExplanation = doc
									.getElementsByTagName("ResultExplanation");
							for (int i = 0; i < nListResultExplanation
									.getLength(); i++) {
								Node nNode = nListResultExplanation.item(i);
								String nodeName = nNode.getNodeName();
								String nodeText = nListResultExplanation
										.item(i).getTextContent();

							}

							String transactionToken = null;
							NodeList nListTransToken = doc.getElementsByTagName("TransToken");
							for (int i = 0; i < nListTransToken.getLength(); i++) {
								Node nNode = nListTransToken.item(i);
								String nodeName = nNode.getNodeName();
								String nodeText = nListTransToken.item(i).getTextContent();

								//transactionToken = nodeText;
								
								editor.putString("transactionToken",nodeText).commit();
								System.out.println("\nCurrent Element :" + nodeText);
								System.out.println("\nCurrent Element :" + nodeName);

							}

							if (transactionToken != null) {

								String getServices = "<API3G>"
										+ "<CompanyToken>"
										+ Constants.COMPANY_TOKEN + "</CompanyToken>"
										+ "<Request>getServices</Request>"
										+ "</API3G>";
								try {
									StringEntity getServicesdata = new StringEntity(
											getServices, HTTP.UTF_8);
									getServicesdata.setContentType("text/xml");
									httppost.setEntity(getServicesdata);

									HttpResponse httpresponsegetServices = httpclient
											.execute(httppost);
									HttpEntity resEntitygetServices = httpresponsegetServices
											.getEntity();
									String xmlStringResultsgetServices = EntityUtils
											.toString(resEntitygetServices);

									Log.i("Get Service XML", " --> "+ xmlStringResultsgetServices);

									DocumentBuilderFactory factorygetServices = DocumentBuilderFactory
											.newInstance();
									DocumentBuilder dbchargetoken;
									DocumentBuilder dbgetServices = factorygetServices
											.newDocumentBuilder();
									InputSource inStreamgetServices = new InputSource();
									inStreamgetServices
											.setCharacterStream(new StringReader(
													xmlStringResultsgetServices));
									org.w3c.dom.Document docgetServices;
									try {
										docgetServices = dbgetServices
												.parse(inStreamgetServices);
										docgetServices.getDocumentElement()
												.normalize();

										NodeList nListResultgetServices = docgetServices
												.getElementsByTagName("Result");
										for (int i = 0; i < nListResultgetServices
												.getLength(); i++) {
											Node nNode = nListResultgetServices
													.item(i);
											String nodeName = nNode
													.getNodeName();
											String nodeText = nListResultgetServices
													.item(i).getTextContent();

											System.out
													.println("\nCurrent Element :"
															+ nodeName);
											System.out
													.println("\nCurrent Element Value:"
															+ nodeText);

										}

										NodeList nListResultExplanationgetServices = docgetServices
												.getElementsByTagName("ResultExplanation");
										for (int i = 0; i < nListResultExplanationgetServices
												.getLength(); i++) {
											Node nNode = nListResultExplanationgetServices
													.item(i);
											String nodeName = nNode
													.getNodeName();
											String nodeText = nListResultExplanationgetServices
													.item(i).getTextContent();
										}

									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							} else {
								// retry connection code to go here
							}

						} catch (SAXException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} catch (ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				paycash.setVisibility(View.VISIBLE);
			}

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

	private class populateListViewTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			layout_progressbar.setVisibility(View.VISIBLE);
			AnimatedGifImageView animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.animatedGifImageView));
			animatedGifImageView.setAnimatedGif(R.mipmap.loading_bar,
					TYPE.FIT_CENTER);

			// animatedGifImageView.setImageResource(R.drawable.loading_bar);
		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return "Done";
		}

		protected void onPostExecute(String params) {
			layout_progressbar.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			listView.setDividerHeight(0);
			myadapterreceipt.notifyDataSetChanged();

			JSONObject jsonMyCartObject = null;
			String stockString = "";
			if (sharedPreferences.contains("myTrolley")) {
				String jsonMyCartString = sharedPreferences.getString(
						"myTrolley", null);

				Log.d("myTrolley",jsonMyCartString);
				try {
					jsonMyCartObject = new JSONObject(jsonMyCartString);
					Iterator<String> loop = jsonMyCartObject.keys();
					while (loop.hasNext()) {
						String key = loop.next();
						stockString = stockString + key + ",";
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			String[] stockArr = stockString.split(",");
			String inClause = gson.toJson(stockArr); // at this point inClause
														// will look like
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
			// Log.i("in clause"," -->" +inClause);

			nunuaRahaDatabase = openOrCreateDatabase("nunuaRahaDatabase",
					MODE_PRIVATE, null);
			Cursor resultsProductsCursor = nunuaRahaDatabase.rawQuery(
					"SELECT * FROM hdjgf_shops_stocks_brands WHERE id IN "
							+ inClause, null);
			resultsProductsCursor.moveToFirst();
			String[] list_item_ids = new String[resultsProductsCursor
					.getCount()];
			String[] list_item_titles = new String[resultsProductsCursor
					.getCount()];
			Integer totalItems = 0;
			int k = 0;
			while (resultsProductsCursor.isAfterLast() == false) {
				String item_id = resultsProductsCursor.getString(0);
				String item_title = resultsProductsCursor.getString(1);
				String item_size = resultsProductsCursor.getString(2);
				String item_price = resultsProductsCursor.getString(3);
				String item_thumbnail_url = resultsProductsCursor.getString(4);
				Integer item_units_in_cart = 0;
				try {
					item_units_in_cart = Integer.parseInt(jsonMyCartObject
							.getString(item_id));
					totalItems = totalItems + item_units_in_cart;
				} catch (NumberFormatException | JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				TOTAL = TOTAL + Integer.parseInt(item_price)
						* item_units_in_cart;

				list_item_ids[k] = item_id;
				list_item_titles[k] = item_title;

				RequestedResultsReceipt d = new RequestedResultsReceipt();

				d.setId(item_id);
				d.setTitle(item_title);
				d.setPrice(item_price);
				d.setSize(item_size);
				d.setUnits(item_units_in_cart);

				d.item_id = item_id;
				d.item_title = item_title;
				d.item_price = item_price;
				d.item_size = item_size;
				d.item_units_in_cart = item_units_in_cart;

                Log.d("item: ",item_title +" price: "+ item_price+" quantity: "+ item_units_in_cart);
                bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, Integer.parseInt(item_id));
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, item_title);
                bundle.putInt(FirebaseAnalytics.Param.QUANTITY,item_units_in_cart);
                bundle.putInt(FirebaseAnalytics.Param.PRICE, Integer.parseInt(item_price));

                MYRECEIPT.add(d);

				k++;
				resultsProductsCursor.moveToNext();
			}

			// TOTAL

			DecimalFormat precision = new DecimalFormat("0.00");
			Double total_double = Double.parseDouble(TOTAL.toString());
			String total_price = precision.format(total_double);

			editor.putString("myTrolleyTotal", total_price).commit();

			// VAT AMT
			Integer vat = TOTAL * 16;
			Double vatdouble = Double.parseDouble(vat.toString());
			vatdouble /= 100;

			// VATABLE AMT
			Double vatableAmt = total_double - vatdouble;
			String vatableAmtStr = vatableAmt.toString();

			editor.putString("vatrate", "16%").commit();

			editor.putString("vatable", vatableAmtStr).commit();

            Log.d("vat_able: ",vatableAmtStr);

            vat_able = vatableAmtStr;

			editor.putString("vatamt", vatdouble.toString()).commit();

            Log.d("vat_amt: ",vatdouble.toString());

            vat_amt = vatdouble.toString();

			editor.putString("totalitem", totalItems.toString()).commit();

            Log.d("totalitem: ",totalItems.toString());

            total_items = totalItems.toString();


            RequestedResultsReceipt d = new RequestedResultsReceipt();
			d.setId("100001");
			d.setTitle("TOTAL");
			d.setPrice(total_price);
			d.setSize("");
			d.setUnits(0);
			d.item_id = "100001";
			d.item_title = "TOTAL";
			d.item_price = total_price;
			d.item_size = "";
			d.item_units_in_cart = 0;

			// MYRECEIPT.add(d);

			// TOTAL ITEMS
			RequestedResultsReceipt e = new RequestedResultsReceipt();
			e.setId("100002");
			e.setTitle("TOTAL ITEMS");
			e.setPrice("");
			e.setSize("");
			e.setUnits(totalItems);
			e.item_id = "100002";
			e.item_title = "TOTAL ITEMS";
			e.item_price = "";
			e.item_size = "";
			e.item_units_in_cart = totalItems;
			// MYRECEIPT.add(e);

            Log.d("totalItems: ",totalItems.toString());

			// VAT RATE
			RequestedResultsReceipt f = new RequestedResultsReceipt();
			f.setId("100003");
			f.setTitle("VAT RATE");
			f.setPrice(total_price);
			f.setSize("16%");
			f.setUnits(0);
			f.item_id = "100003";
			f.item_title = "VAT RATE";
			f.item_price = "16%";
			f.item_size = "";
			f.item_units_in_cart = 0;
			// MYRECEIPT.add(f);

			// VATABLE AMOUNT
			RequestedResultsReceipt g = new RequestedResultsReceipt();
			g.setId("100004");
			g.setTitle("VATABLE AMT");
			g.setPrice(vatableAmtStr);
			g.setSize("");
			g.setUnits(0);
			g.item_id = "100004";
			g.item_title = "VATABLE AMT";
			g.item_price = vatableAmtStr;
			g.item_size = "";
			g.item_units_in_cart = 0;
			// MYRECEIPT.add(g);

			// VAT AMOUNT
			RequestedResultsReceipt h = new RequestedResultsReceipt();
			h.setId("100005");
			h.setTitle("VAT AMT");
			h.setPrice(vatdouble.toString());
			h.setSize("");
			h.setUnits(0);
			h.item_id = "100005";
			h.item_title = "VAT AMT";
			h.item_price = vatdouble.toString();
			h.item_size = "";
			h.item_units_in_cart = 0;
			// MYRECEIPT.add(h);

            //Logs an app event.
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            //Sets whether analytics collection is enabled for this app on this device.
            firebaseAnalytics.setAnalyticsCollectionEnabled(true);

            //Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
            firebaseAnalytics.setMinimumSessionDuration(20000);

            //Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
            firebaseAnalytics.setSessionTimeoutDuration(500);

            //Sets the user ID property.
            firebaseAnalytics.setUserId(String.valueOf(totalItems));

            //Sets a user property to a given value.
            firebaseAnalytics.setUserProperty("Food", "Receipt");
		}

		private void getText(String total_price) {
			// TODO Auto-generated method stub

		}

	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.UNSPECIFIED);
		int totalHeight = -10;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new LayoutParams(desiredWidth,
						LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
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
			// headersection.setVisibility(View.GONE);

			// DISPLAY OUTLET LOGO
			// File file = new File(extStorageDirectory+"/NunuaRaha/",
			// SELECTED_BRAND_LOGO);
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			// Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
			// options);
			// shopLogoview.setImageBitmap(bitmap);

			shopDetails.setText(SELECTED_BRANCH_TITLE);
			userName.setText(CUSTOMER_FIRST_NAME + " " + CUSTOMER_LAST_NAME);
			emailAddress.setText(CUSTOMER_EMAIL_ADDRESS);
			phoneNumber.setText(" - " + CUSTOMER_PHONE_NUMBER);
			totalovaraltext.setText(Ovaraltatol);

			totalitem.setText(" " + total_items);
			vatrate.setText(" " + vatrate2);
			vatable.setText(" " + vat_able);
			vatamt.setText(" " + vat_amt);

			Log.i("total_items ", " --> " + total_items);
			Log.i("vatrate", " --> " + vatrate2);
			Log.i("vatable", " --> " + vatable2);
			Log.i("vatamt", " --> " + vatamt2);
			Log.i("totalovaraltext", " --> " + Ovaraltatol);

			int sw = Double.valueOf(Ovaraltatol).intValue();
			Integer vat1 = sw * 10;
			Double vatdouble1 = Double.parseDouble(vat1.toString());
			vatdouble1 /= 100;

			// editor.putString("servedata", "10%");
			// editor.commit();

			service.setText(" " + vatdouble1.toString());

			Log.i("totalovaraltext", " --> " + service);

			int i = Double.valueOf(Ovaraltatol).intValue();
			int ii = Double.valueOf(vatdouble1.toString()).intValue();

			if (sharedPreferences.getString("deliverShopping", null).equals(
					"No")) {
				delivery.setText("Pick Up");
				deliveryinfo.setText(" " + 0.00);

				int recieptdata = i + ii + 0;

				String strI = Integer.toString(recieptdata);

				DecimalFormat precisionTotal = new DecimalFormat("0.00");
				Double item_total_double = Double.parseDouble(strI);
				String item_total1 = precisionTotal.format(item_total_double);

				reciepttotaltext.setText(item_total1);

				editor.putString("reciepttotaltextdata", item_total1).commit();

			} else {
				delivery.setText(CUSTOMER_ADDRESS);

				deliveryinfo.setText(" " + 250.00);

				int recieptdata = i + ii + 250;

				String strI = Integer.toString(recieptdata);

				DecimalFormat precisionTotal = new DecimalFormat("0.00");
				Double item_total_double = Double.parseDouble(strI);
				String item_total1 = precisionTotal.format(item_total_double);

				reciepttotaltext.setText(item_total1);

				editor.putString("reciepttotaltextdata", item_total1).commit();

			}

			paycash.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					editor.putString("paymentMode", "Cash").commit();
					Intent intent = new Intent(ActivityStep11MyReceipt.this,
							ActivityStep12ThankYou.class);
					startActivity(intent);

				}
			});

			paycreditcard.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					editor.putString("paymentMode", "CreditCard").commit();
					Intent intent = new Intent(ActivityStep11MyReceipt.this,
							ActivityStep11PaymentOptionsGateway.class);
					startActivity(intent);
				}
			});

			paymobilemoney.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					editor.putString("paymentMode", "MobileMoney").commit();
					Intent intent = new Intent(ActivityStep11MyReceipt.this,
							ActivityStep11PaymentOptionsGateway.class);
					startActivity(intent);
				}
			});

		}

	}

	public void showDialogEmptyTrolley(final String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityStep11MyReceipt.this);
		builder.setMessage("You will lose all the items that you have selected. "
				+ message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				Intent intent = new Intent(ActivityStep11MyReceipt.this,
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
