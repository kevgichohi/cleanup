package com.righthere.efam;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class AdapterMenu extends BaseAdapter {
	private static ArrayList<RequestedResultsSimpleList> searchArrayList;
	private LayoutInflater mInflater;
	private Context context;
	private Typeface font;
	private String activityName; // instance variable

	public AdapterMenu(Context ctx,
			ArrayList<RequestedResultsSimpleList> results, Typeface font) {
		searchArrayList = results;
		mInflater = LayoutInflater.from(ctx);
		this.font = font;
		context = ctx;
		activityName = context.getClass().getSimpleName();
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
		String ActivityListBranches = "ActivityStep04ListBranches";
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.menu_list, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.mymenulist);

			// Delivery charges view only appears on ActivityStep04ListBranches
			if (activityName.equals(ActivityListBranches)) {
				holder.deliverycharge = new TextView(
						context.getApplicationContext());
				((ViewGroup) convertView).addView(holder.deliverycharge);

				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.deliverycharge
						.getLayoutParams();
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				params.addRule(RelativeLayout.RIGHT_OF, R.id.mysimplelist);
				params.addRule(RelativeLayout.LEFT_OF, R.id.arrowRight);

				holder.deliverycharge.setLayoutParams(params); // causes layout
																// update
			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.title.setText(searchArrayList.get(position).getTitle());
		holder.title.setTypeface(font);

		return convertView;
	}

	private Object getResources() {
		// TODO Auto-generated method stub
		return null;
	}

	static class ViewHolder {
		TextView title;
		TextView deliverycharge;
		ImageView itemImage;
	}

	private Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}
}
