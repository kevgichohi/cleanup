package com.righthere.efam;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

public class Terms extends Activity {
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
	TextView cartButtonNotification;

	Typeface EkMukta_Light;

	private Button title1;
	private TextView text1;
	private TextView text11;
	private Button title2;
	private TextView text2;
	private TextView text22;
	private Button title3;
	private TextView text3;
	private Button title4;
	private TextView text4;
	private ImageView headerTextimage2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_page_in, R.anim.slide_page_out);// SlideIn
																				// animation
		setContentView(R.layout.terms);
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

		title1 = (Button) findViewById(R.id.title1);
		text1 = (TextView) findViewById(R.id.text1);
		text11 = (TextView) findViewById(R.id.text11);
		title2 = (Button) findViewById(R.id.title2);
		text2 = (TextView) findViewById(R.id.text2);
		text22 = (TextView) findViewById(R.id.text22);
		title3 = (Button) findViewById(R.id.title3);
		text3 = (TextView) findViewById(R.id.text3);
		title4 = (Button) findViewById(R.id.title4);
		text4 = (TextView) findViewById(R.id.text4);
		cartButtonNotification = (TextView) findViewById(R.id.cartButtonNotification);

		// LOAD QUICKLINKS
		HelperQuickLinks helperQuickLinks = new HelperQuickLinks();

		helperQuickLinks.create(menuIcon, shoppingButton, cartButton,
				cartButtonNotification, Terms.this, sharedPreferences);

		headerText.setText("Terms");
		headerTextimage2 = (ImageView) findViewById(R.id.headerTextimage);
		headerTextimage2.setVisibility(View.VISIBLE);
		headerTextimage2.setImageResource(R.mipmap.menutermsactive);

		onBtnClick();

	}

	public void onBtnClick() {

		title1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				text1.setVisibility((text1.getVisibility() == View.VISIBLE) ? View.GONE
						: View.VISIBLE);
				text11.setVisibility((text11.getVisibility() == View.VISIBLE) ? View.GONE
						: View.VISIBLE);
				text2.setVisibility(View.GONE);
				text22.setVisibility(View.GONE);
				text3.setVisibility(View.GONE);
				text4.setVisibility(View.GONE);

			}
		});

		title2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				text2.setVisibility((text2.getVisibility() == View.VISIBLE) ? View.GONE
						: View.VISIBLE);
				text22.setVisibility((text22.getVisibility() == View.VISIBLE) ? View.GONE
						: View.VISIBLE);
				text1.setVisibility(View.GONE);
				text11.setVisibility(View.GONE);
				text3.setVisibility(View.GONE);
				text4.setVisibility(View.GONE);
			}
		});

		title3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				text3.setVisibility((text3.getVisibility() == View.VISIBLE) ? View.GONE
						: View.VISIBLE);
				text1.setVisibility(View.GONE);
				text11.setVisibility(View.GONE);
				text22.setVisibility(View.GONE);
				text2.setVisibility(View.GONE);
				text4.setVisibility(View.GONE);

			}
		});

		title4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				text4.setVisibility((text4.getVisibility() == View.VISIBLE) ? View.GONE
						: View.VISIBLE);
				text1.setVisibility(View.GONE);
				text11.setVisibility(View.GONE);
				text22.setVisibility(View.GONE);
				text2.setVisibility(View.GONE);
				text3.setVisibility(View.GONE);

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
	}

}
