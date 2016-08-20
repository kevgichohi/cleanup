package com.redhering.nunuaraha;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.redhering.nunuaraha.AnimatedGifImageView.TYPE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityStep11PaymentOptionsGatewaymobileexit extends Activity {
	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	public static ArrayList<RequestedResultsSimpleList> DELIVERYOPTIONS;
	AdapterSimpleList myadapter;

	LinearLayout layout_progressbar;
	RelativeLayout loadingPanel;
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
	TextView cartButtonNotification;
	TextView headerText;
	Button shoppingButton;
	Button cartButton;
	WebView webView;
	ImageView shopLogoview;
	String extStorageDirectory;
	Bitmap bm;

	Button proceedToCheckout;
	Button clearcart;


	private ImageView headerTextimage2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mobilexit);
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn
																				// animation
		initViews();

		if (sharedPreferences.contains("loggedIn")
				&& sharedPreferences.contains("customerInfo")) {
			headerText.setText("Payment Options");

			headerTextimage2 = (ImageView) findViewById(R.id.headerTextimage);
			headerTextimage2.setVisibility(View.VISIBLE);
			headerTextimage2.setImageResource(R.drawable.paymentmode);

			// LOAD QUICKLINKS
			/*HelperQuickLinks helperQuickLinks = new HelperQuickLinks();
			helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
							cartButtonNotification,
							ActivityStep11PaymentOptionsGatewaymobileexit.this,
							sharedPreferences);*/
			new ApplyViewParamsTask().execute();

			// DISPLAY OUTLET LOGO
			// File file = new File(extStorageDirectory+"/NunuaRaha/",
			// SELECTED_BRAND_LOGO);
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			// Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
			// options);
			// shopLogoview.setImageBitmap(bitmap);

		} else {
			Intent intent = new Intent(
					ActivityStep11PaymentOptionsGatewaymobileexit.this,
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
		loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
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
		shopLogoview = (ImageView) findViewById(R.id.shopLogo);

		proceedToCheckout = (Button) findViewById(R.id.proceedToCheckout);
		clearcart = (Button) findViewById(R.id.clearcart);

		
		String nodename1 = sharedPreferences.getString("node1", null);
		String type1 = sharedPreferences.getString("type1", null);
		
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
			//layout_progressbar.setVisibility(View.VISIBLE);
			//AnimatedGifImageView animatedGifImageView = ((AnimatedGifImageView) findViewById(R.id.animatedGifImageView));
			//animatedGifImageView.setAnimatedGif(R.drawable.loading_bar,
					//TYPE.FIT_CENTER);
			
			
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"https://secure.3gdirectpay.com/API/v5/");
			String transactionToken = sharedPreferences.getString(
					"transactionToken", null);
			String companyToken = sharedPreferences.getString(
					"companyToken", null);
			
			
			String chargeMpesaCardToken = "<API3G>" + "<CompanyToken>"
					+ companyToken + "</CompanyToken>"
					+ "<Request>verifyToken</Request>"
					+ "<TransactionToken>" + transactionToken
					+ "</TransactionToken></API3G>";
			
			

			try {
				StringEntity se = new StringEntity(
						chargeMpesaCardToken, HTTP.UTF_8);
				se.setContentType("text/xml");
				httppost.setEntity(se);

				HttpResponse httpresponse = httpclient
						.execute(httppost);
				HttpEntity resEntity = httpresponse.getEntity();
				String xmlStringResults = EntityUtils
						.toString(resEntity);

				// Log.i("Token XML"," --> "+xmlStringResults);

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

							 System.out.println("\nCurrent Element :"
							 + nodeName);
							 System.out.println("\nCurrent Element Value:"
							 + nodeText);


								//carries result response id
								editor.putString("codedata", nodeText).commit();
							
							if(nodeText.equals("000")){
							new CountDownTimer(5000, 1000) {
							    public void onFinish() {
							         Intent startActivity = new Intent(ActivityStep11PaymentOptionsGatewaymobileexit.this,ActivityStep12ThankYou.class);
							         startActivity(startActivity);
							        finish();
							    }

							    public void onTick(long millisUntilFinished) {
							    }

							}.start();
							
						}
						
							
						}

						NodeList nListResultExplanation = doc
								.getElementsByTagName("ResultExplanation");
						for (int i = 0; i < nListResultExplanation
								.getLength(); i++) {
							Node nNode = nListResultExplanation.item(i);
							String nodeName = nNode.getNodeName();
							String nodeText = nListResultExplanation
									.item(i).getTextContent();
							 System.out.println("\nCurrent Element :"
							 + nodeName);
							 System.out.println("\nResultExplanation:"
							 + nodeText);
							 
							 String nodeName2 = sharedPreferences.getString("codedata",null);
							 
							 
							 if(nodeName2.equals("900")){
								 TextView mobilechargetoken1 = (TextView) findViewById(R.id.mobilechargetoken);
									mobilechargetoken1.setVisibility(View.VISIBLE);

									Bundle extras = getIntent().getExtras();

									// Get endResult
									mobilechargetoken1.setText(" The page will continually re-load itself as we await for your payment");
									
									new CountDownTimer(5000, 1000) {
									    public void onFinish() {
									         Intent startActivity = new Intent(ActivityStep11PaymentOptionsGatewaymobileexit.this,ActivityStep11PaymentOptionsGatewaymobileexit.class);
									         startActivity(startActivity);
									        finish();
									    }

									    public void onTick(long millisUntilFinished) {
									    }

									}.start();
								}
							 
							 
							 if(nodeName2.equals("901")){
								 TextView mobilechargetoken1 = (TextView) findViewById(R.id.mobilechargetoken);
									mobilechargetoken1.setVisibility(View.VISIBLE);

									Bundle extras = getIntent().getExtras();

									// Get endResult
									mobilechargetoken1.setText(nodeText);
									
									new CountDownTimer(5000, 1000) {
									    public void onFinish() {
									         Intent startActivity = new Intent(ActivityStep11PaymentOptionsGatewaymobileexit.this,ActivityStep10ListPaymentOptions.class);
									         startActivity(startActivity);
									        finish();
									    }

									    public void onTick(long millisUntilFinished) {
									    }

									}.start();
								}
								if(nodeName2.equals("904")){
									TextView mobilechargetoken1 = (TextView) findViewById(R.id.mobilechargetoken);
									mobilechargetoken1.setVisibility(View.VISIBLE);

									Bundle extras = getIntent().getExtras();

									// Get endResult
									mobilechargetoken1.setText(nodeText);
									
									new CountDownTimer(5000, 1000) {
									    public void onFinish() {
									         Intent startActivity = new Intent(ActivityStep11PaymentOptionsGatewaymobileexit.this,ActivityStep10ListPaymentOptions.class);
									         startActivity(startActivity);
									        finish();
									    }

									    public void onTick(long millisUntilFinished) {
									    }

									}.start();
								}
								
								if(nodeName2.equals("902")){
									
									TextView mobilechargetoken1 = (TextView) findViewById(R.id.mobilechargetoken);
									mobilechargetoken1.setVisibility(View.VISIBLE);

									Bundle extras = getIntent().getExtras();

									// Get endResult
									mobilechargetoken1.setText(nodeText);
									
									new CountDownTimer(5000, 1000) {
									    public void onFinish() {
									         Intent startActivity = new Intent(ActivityStep11PaymentOptionsGatewaymobileexit.this,ActivityStep10ListPaymentOptions.class);
									         startActivity(startActivity);
									        finish();
									    }

									    public void onTick(long millisUntilFinished) {
									    }

									}.start();
									
								}
								

						}

						NodeList nListResultInstructions = doc
								.getElementsByTagName("instructions");
						for (int i = 0; i < nListResultInstructions
								.getLength(); i++) {
							Node nNode = nListResultInstructions
									.item(i);
							String nodeName = nNode.getNodeName();
							String nodeTextinstruction = nListResultInstructions
									.item(i).getTextContent();
							 System.out.println("\nCurrent Element :"
							 + nodeName);
							 System.out.println("\nInstructions:" +
							 nodeTextinstruction);

							TextView mobilechargetoken1 = (TextView) findViewById(R.id.mobilechargetoken);
							mobilechargetoken1.setVisibility(View.VISIBLE);

							Bundle extras = getIntent().getExtras();

							// Get endResult
							mobilechargetoken1.setText(nodeTextinstruction);

							
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

					super.onPreExecute();
		}

		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return "Done";
		}

		protected void onPostExecute(String params) {
			layout_progressbar.setVisibility(View.GONE);


		}

		}


	public void showDialogEmptyTrolley(final String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityStep11PaymentOptionsGatewaymobileexit.this);
		builder.setMessage("You will lose all the items that you have selected. "
				+ message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Clear Cart Session
				sharedPreferences.edit().remove("myTrolley").commit();

				Intent intent = new Intent(
						ActivityStep11PaymentOptionsGatewaymobileexit.this,
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