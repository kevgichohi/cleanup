package com.redhering.nunuaraha;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class AdapterListWithImage extends BaseAdapter {
	private static ArrayList<RequestedResults> searchArrayList;
	private LayoutInflater mInflater;
	private Context context;
	private Typeface font;
	private String activityName;

	public static final String MY_SESSION = "mySession";
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	public AdapterListWithImage(Context ctx,
			ArrayList<RequestedResults> results, Typeface font) {
		searchArrayList = results;
		mInflater = LayoutInflater.from(ctx);
		this.font = font;
		context = ctx;
		activityName = context.getClass().getSimpleName();
		sharedPreferences = context.getSharedPreferences(MY_SESSION,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();

		Log.i("sharedPreferences", " --> " + sharedPreferences);
	}

	public int getCount() {
		return searchArrayList.size();
	}

	public Object getItem(int position) {
		return searchArrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		String ActivityListBrands = "ActivityStep02ListBrands";
		String Activityoutlets = "ActivityStep01ListOutlets";
		String ActivityBranches = "ActivityStep04ListBranches";
		String ActivityAisle = "ActivityStep05ListAisles";
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.simple_list_with_image,
					null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.mylist);
			holder.itemImage = (ImageView) convertView
					.findViewById(R.id.itemImage);

			if (activityName.equals(ActivityListBrands)) {
				holder.currentSelection = new TextView(
						context.getApplicationContext());
				((ViewGroup) convertView).addView(holder.currentSelection);
				RelativeLayout.LayoutParams currentSelectionParams = (RelativeLayout.LayoutParams) holder.currentSelection
						.getLayoutParams();
				currentSelectionParams.addRule(RelativeLayout.BELOW,
						R.id.mylist);
				currentSelectionParams.addRule(RelativeLayout.RIGHT_OF,
						R.id.itemImage);
				holder.currentSelection.setLayoutParams(currentSelectionParams); // causes
																					// layout
																					// update

			}

			if (activityName.equals(Activityoutlets)) {
				holder.currentSelection = new TextView(
						context.getApplicationContext());
				((ViewGroup) convertView).addView(holder.currentSelection);
				RelativeLayout.LayoutParams currentSelectionParams = (RelativeLayout.LayoutParams) holder.currentSelection
						.getLayoutParams();
				currentSelectionParams.addRule(RelativeLayout.BELOW,
						R.id.mylist);
				currentSelectionParams.addRule(RelativeLayout.RIGHT_OF,
						R.id.itemImage);
				holder.currentSelection.setLayoutParams(currentSelectionParams); // causes
																					// layout
																					// update
			}

			if (activityName.equals(ActivityBranches)) {
				holder.currentSelection = new TextView(
						context.getApplicationContext());
				((ViewGroup) convertView).addView(holder.currentSelection);
				RelativeLayout.LayoutParams currentSelectionParams = (RelativeLayout.LayoutParams) holder.currentSelection
						.getLayoutParams();
				currentSelectionParams.addRule(RelativeLayout.BELOW,
						R.id.mylist);
				currentSelectionParams.addRule(RelativeLayout.RIGHT_OF,
						R.id.itemImage);
				holder.currentSelection.setLayoutParams(currentSelectionParams); // causes
																					// layout
																					// update
			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.title.setText(searchArrayList.get(position).getTitle());
		holder.title.setTypeface(font);

		// download image
		ImageDownloader imageDownloader = new ImageDownloader();
		imageDownloader.download(searchArrayList.get(position)
				.getThumbnailUrl(), holder.itemImage);

		// Log.i("Thumbnail"," --> "+
		// searchArrayList.get(position).getThumbnailUrl());

		// Log.i("title"," --> "+ searchArrayList.get(position).getTitle());
		if (activityName.equals(ActivityBranches)) {
			holder.itemImage.setImageResource(R.drawable.superheading1);

		}

		if (activityName.equals(Activityoutlets)) {

			if (searchArrayList.get(position).getTitle().equals("Supermarkets")) {
				holder.itemImage.setImageResource(R.drawable.branchheader);
			}

			if (searchArrayList.get(position).getTitle().equals("Wholesalers")) {
				holder.itemImage.setImageResource(R.drawable.wholesale);
			}

		}

		if (activityName.equals(ActivityListBrands)) {
			holder.itemImage.setImageResource(R.drawable.ic_shop);

		}

		if (activityName.equals(ActivityAisle)) {
			if (searchArrayList.get(position).getTitle()
					.equals("Special Offers")) {
				holder.itemImage.setImageResource(R.drawable.specialoffer);
			}
			if (searchArrayList.get(position).getTitle()
					.equals("Bread & Bakery")) {
				holder.itemImage.setImageResource(R.drawable.grocery_bread);
			}
			if (searchArrayList.get(position).getTitle().equals("Dairy & Eggs")) {
				holder.itemImage.setImageResource(R.drawable.grocery_dairy);

			}
			if (searchArrayList.get(position).getTitle().equals("Beverage")) {
				holder.itemImage.setImageResource(R.drawable.grocery_beverage);
			}
			if (searchArrayList.get(position).getTitle()
					.equals("Dry/Baking Goods")) {
				holder.itemImage.setImageResource(R.drawable.grocery_baking);
			}
			if (searchArrayList.get(position).getTitle().equals("Fruits")) {
				holder.itemImage.setImageResource(R.drawable.grocery_fruits);
			}
			if (searchArrayList.get(position).getTitle().equals("Baby Care")) {
				holder.itemImage.setImageResource(R.drawable.grocery_babycare);
			}
			if (searchArrayList.get(position).getTitle()
					.equals("Frozen Food & Ice Cream")) {
				holder.itemImage.setImageResource(R.drawable.grocery_ice);
			}
			if (searchArrayList.get(position).getTitle().equals("Seafood")) {
				holder.itemImage.setImageResource(R.drawable.grocery_seafood);
			}
			if (searchArrayList.get(position).getTitle().equals("Vegetables")) {
				holder.itemImage
						.setImageResource(R.drawable.grocery_vegetables);
			}
			if (searchArrayList.get(position).getTitle()
					.equals("Canned & Jarred Foods")) {
				holder.itemImage.setImageResource(R.drawable.grocery_canned);
			}
			if (searchArrayList.get(position).getTitle()
					.equals("Cooking Oil & Fat")) {
				holder.itemImage.setImageResource(R.drawable.grocery_oil);
			}
			if (searchArrayList.get(position).getTitle()
					.equals("Confectionery")) {
				holder.itemImage
						.setImageResource(R.drawable.grocery_confectionaries);
			}
			if (searchArrayList.get(position).getTitle().equals("Home Care")) {
				holder.itemImage.setImageResource(R.drawable.grocery_homecare);
			}
			if (searchArrayList.get(position).getTitle().equals("Seasoning")) {
				holder.itemImage.setImageResource(R.drawable.grocery_seasoning);
			}
			if (searchArrayList.get(position).getTitle()
					.equals("Cereals & Breakfast Food")) {
				holder.itemImage.setImageResource(R.drawable.grocery_cereals);
			}
			if (searchArrayList.get(position).getTitle()
					.equals("Processed Foods")) {
				holder.itemImage
						.setImageResource(R.drawable.grocery_processedfood);
			}
			if (searchArrayList.get(position).getTitle()
					.equals("Personal Care")) {
				holder.itemImage.setImageResource(R.drawable.grocery_bodycare);
			}
			if (searchArrayList.get(position).getTitle().equals("Meat")) {
				holder.itemImage.setImageResource(R.drawable.grocery_meat);
			}
			if (searchArrayList.get(position).getTitle().equals("Deli")) {
				holder.itemImage.setImageResource(R.drawable.grocery_deli);
			}
			if (searchArrayList.get(position).getTitle().equals("Medicare")) {
				holder.itemImage.setImageResource(R.drawable.grocery_medicare);
			}
		}

		if (activityName.equals(ActivityListBrands)) {
			if (sharedPreferences.contains("selectedBrandId")
					&& sharedPreferences.contains("myTrolley")) {
				String SELECTED_BRAND_ID = sharedPreferences.getString(
						"selectedBrandId", null);
				if (SELECTED_BRAND_ID.equals(searchArrayList.get(position)
						.getId())) {
					holder.currentSelection.setText("(Your trolley is here)");
					holder.currentSelection.setTypeface(font, Typeface.ITALIC);
					holder.currentSelection.setTextColor(context.getResources()
							.getColor(R.color.green));
					holder.currentSelection.getLayoutParams().height = 30;
				} else {
					holder.currentSelection.setText("");
					holder.currentSelection.getLayoutParams().height = 0;
				}
			} else {
				holder.currentSelection.setText("");
				holder.currentSelection.getLayoutParams().height = 0;
			}
		}

		if (activityName.equals(Activityoutlets)) {
			if (sharedPreferences.contains("selectedOutletId")
					&& sharedPreferences.contains("myTrolley")) {
				String SELECTED_OUTLET_ID = sharedPreferences.getString(
						"selectedOutletId", null);
				if (SELECTED_OUTLET_ID.equals(searchArrayList.get(position)
						.getId())) {
					holder.currentSelection.setText("(Your trolley is here)");
					holder.currentSelection.setTypeface(font, Typeface.ITALIC);
					holder.currentSelection.setTextColor(context.getResources()
							.getColor(R.color.green));
					holder.currentSelection.getLayoutParams().height = 30;
				} else {
					holder.currentSelection.setText("");
					holder.currentSelection.getLayoutParams().height = 0;
				}
			} else {
				holder.currentSelection.setText("");
				holder.currentSelection.getLayoutParams().height = 0;
			}
		}

		return convertView;
	}

	static class ViewHolder {
		TextView title;
		ImageView itemImage;
		TextView currentSelection;
	}

	private Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}
}
