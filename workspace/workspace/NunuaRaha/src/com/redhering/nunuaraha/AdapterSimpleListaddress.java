package com.redhering.nunuaraha;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class AdapterSimpleListaddress extends BaseAdapter {
	private static ArrayList<RequestedResultsSimpleList> searchArrayList;
	private LayoutInflater mInflater;
	private Context context;
	private Typeface font;
	private String activityName; // instance variable
	public static String Itemghg_ID;
	SQLiteDatabase nunuaRahaDatabase;


	public static final String MY_SESSION = "mySession";
	public static SharedPreferences sharedPreferences;
	protected static final String MODE_PRIVATE = null;
	public static String MY_PHONE_NUMBER;
	SharedPreferences.Editor editor;

	
	
	public AdapterSimpleListaddress(Context ctx,
			ArrayList<RequestedResultsSimpleList> results, Typeface font) {
		searchArrayList = results;
		mInflater = LayoutInflater.from(ctx);
		this.font = font;
		context = ctx;
		activityName = context.getClass().getSimpleName();
		sharedPreferences = context.getSharedPreferences(MY_SESSION,
				Context.MODE_PRIVATE);

		editor = sharedPreferences.edit();
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

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		String ActivityListOutlets = "ActivityStep01ListOutlets";
		String ActivityListBranches = "ActivityStep04ListBranches";
		String ActivityMyAddresses = "ActivityMyAccountAddresses";
		String Activityneighbour = "ActivityStep03ListNeighbourhood";

		if (convertView == null) {
			if (activityName.equals(ActivityMyAddresses)) {
				convertView = mInflater.inflate(R.layout.simple_listaddress,
						null);

				String CUSTOMER_PHONE_NUMBER = sharedPreferences.getString("userPhoneNumber", null);

				AdapterView<ListAdapter> listView;

				// inflate other items here :
				Button deleteButton = (Button) convertView
						.findViewById(R.id.arrowRight);
				deleteButton.setTag(position);

				deleteButton.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View v) {
						// View view = null;
						// Integer index = (Integer) view.getTag();
						int pos = (int) v.getTag();

						String strI = Integer.toString(pos);

						Itemghg_ID = searchArrayList.get(pos).getId();

						Log.i("itemID", Itemghg_ID);

						//onPostExecute(itemID);
						
						new HttpAsyncTask().execute("http://www.e-fam.com/mobile_trolley_app/delete.php");
						
						Log.i("selected item ","confirm delivery clicked");

						searchArrayList.remove(pos);
						notifyDataSetChanged();

					}

				});

			} else if (activityName.equals(Activityneighbour)) {
				convertView = mInflater.inflate(
						R.layout.simple_listneighbourhood, null);
			}

			else {
				convertView = mInflater.inflate(R.layout.simple_list, null);
			}
			holder = new ViewHolder();
			holder.title = (TextView) convertView
					.findViewById(R.id.mysimplelist);

			// Delivery charges view only appears on ActivityStep04ListBranches
			/**
			 * if(activityName.equals(ActivityStep04ListBranches)){
			 * holder.deliverycharge = new
			 * TextView(context.getApplicationContext()); ((ViewGroup)
			 * convertView).addView(holder.deliverycharge);
			 * RelativeLayout.LayoutParams params =
			 * (RelativeLayout.LayoutParams)
			 * holder.deliverycharge.getLayoutParams();
			 * params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			 * params.addRule(RelativeLayout.RIGHT_OF, R.id.mysimplelist);
			 * params.addRule(RelativeLayout.LEFT_OF, R.id.arrowRight);
			 * holder.deliverycharge.setLayoutParams(params); //causes layout
			 * update }
			 **/

			if (activityName.equals(ActivityListOutlets)
					|| activityName.equals(ActivityListBranches)) {
				holder.currentSelection = new TextView(
						context.getApplicationContext());
				((ViewGroup) convertView).addView(holder.currentSelection);
				RelativeLayout.LayoutParams currentSelectionParams = (RelativeLayout.LayoutParams) holder.currentSelection
						.getLayoutParams();
				currentSelectionParams.addRule(RelativeLayout.BELOW,
						R.id.mysimplelist);
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

		// Delivery charges view only appears on ActivityStep04ListBranches
		if (activityName.equals(ActivityListBranches)) {
			/**
			 * String charges =
			 * searchArrayList.get(position).getDeliveryCharge(); String
			 * freedelivery = "Free Delivery";
			 * holder.deliverycharge.setText(" - " + charges);
			 * holder.deliverycharge.setTypeface(font,Typeface.ITALIC);
			 * 
			 * //Asigning text color based on the text info
			 * holder.deliverycharge
			 * .setTextColor(context.getResources().getColor(R.color.blue));
			 * if(charges.equals(freedelivery)){
			 * holder.deliverycharge.setTextColor
			 * (context.getResources().getColor(R.color.red)); }
			 **/

			if (sharedPreferences.contains("selectedBranchId")
					&& sharedPreferences.contains("myTrolley")) {
				String SELECTED_BRANCH_ID = sharedPreferences.getString(
						"selectedBranchId", null);
				if (SELECTED_BRANCH_ID.equals(searchArrayList.get(position)
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
				Log.i("Current Branch --> " + SELECTED_BRANCH_ID,
						"This Branch --> "
								+ searchArrayList.get(position).getId());
			} else {
				holder.currentSelection.setText("");
				holder.currentSelection.getLayoutParams().height = 0;
			}

		}

		if (activityName.equals(ActivityListOutlets)) {
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
					Log.i("Current Outlet --> " + SELECTED_OUTLET_ID,
							"This Outlet --> "
									+ searchArrayList.get(position).getId());
				} else {
					holder.currentSelection.setText("");
					holder.currentSelection.getLayoutParams().height = 0;
				}
			} else {
				holder.currentSelection.setText("");
				holder.currentSelection.getLayoutParams().height = 0;
			}
		}

		// convertView.setClickable(true);
		// convertView.setFocusable(true);
		// convertView.setBackgroundResource(android.R.drawable.menuitem_background);

		return convertView;
	}

	private Object getResources() {
		// TODO Auto-generated method stub
		return null;
	}

	static class ViewHolder {
		public Object arrowRight;
		TextView title;
		TextView deliverycharge;
		TextView currentSelection;
		ImageView itemImage;
	}

	private Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}



	/*protected void onPostExecute(String itemID) {

		MY_PHONE_NUMBER = sharedPreferences.getString("userPhoneNumber", null);

		String user_id3 = sharedPreferences.getString("user_id2", null);
		Log.i("user_id2", user_id3);

		
			String query1 = "DELETE FROM hdjgf_customer_addresses WHERE location_id="
					+ itemID + " AND user_id=" + user_id3;

			Log.i("quer", user_id3);
			Log.i("query1", query1);
			Log.i("Orders", " --> " + searchArrayList);
			//nunuaRahaDatabase.execSQL(query1,null);
			
			//nunuaRahaDatabase.delete("hdjgf_customer_addresses","location_id=? and user_id=?",new String[]{itemID,user_id3});
		

	}
*/
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			String user_id3 = sharedPreferences.getString("user_id2", null);
			super.onPostExecute(result);
			Log.i("Delivery ", " fata1 --> " + Itemghg_ID);
			Log.i("Delivery ", " New status --> " + user_id3);
			Log.i("Delivery ", " New status --> " + result);
		}

		@Override
		protected String doInBackground(String... urls) {

			return GET(urls[0]);
		}

	}

	public static String GET(String url) {
		InputStream inputStream = null;
		String result = "";
		String user_id3 = sharedPreferences.getString("user_id2", null);
		Log.i("Delivery ", " fata1 --> " + Itemghg_ID);
		Log.i("Delivery ", " data2 --> " + user_id3);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("user_id", user_id3));
		nameValuePairs.add(new BasicNameValuePair("item_data", Itemghg_ID));

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

}
