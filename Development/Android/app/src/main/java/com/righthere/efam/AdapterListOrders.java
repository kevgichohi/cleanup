package com.righthere.efam;

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
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class AdapterListOrders extends BaseAdapter implements
		View.OnClickListener {
	private static ArrayList<RequestedSimpleListOrders> searchArrayList;
	private LayoutInflater mInflater;
	private Context context;
	private Typeface font;
	public static String ORDER_ID;
	private String activityName;
	LinearLayout layout_progressbar;
	RelativeLayout ratingForm;

	public AdapterListOrders(Context ctx,
			ArrayList<RequestedSimpleListOrders> results,
			LinearLayout layout_loading, RelativeLayout rateForm) {
		searchArrayList = results;
		mInflater = LayoutInflater.from(ctx);
		this.font = font;
		context = ctx;
		activityName = context.getClass().getSimpleName();
		layout_progressbar = layout_loading;
		ratingForm = rateForm;

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
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.simple_list_orders, null);
			holder = new ViewHolder();
			holder.itemOne = (TextView) convertView.findViewById(R.id.itemOne);
			holder.itemTwo = (TextView) convertView.findViewById(R.id.itemTwo);
			holder.itemThree = (TextView) convertView
					.findViewById(R.id.itemThree);
			holder.confirmDelivery = (Button) convertView
					.findViewById(R.id.confirmDelivery);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String itemId = searchArrayList.get(position).getItemId();

		if (itemId.equals("")) {
			holder.itemOne.setText(searchArrayList.get(position).getItemOne());
			holder.itemOne.setGravity(Gravity.CENTER);

			holder.itemTwo.setText(searchArrayList.get(position).getItemTwo());
			holder.itemTwo.setGravity(Gravity.CENTER);

			holder.itemThree.setText(searchArrayList.get(position)
					.getItemThree());
			holder.itemThree.setGravity(Gravity.CENTER);

			holder.viewOrder.setVisibility(View.GONE);
			holder.confirmDelivery.setVisibility(View.GONE);
		} else {
			holder.itemOne.setText(" | "
					+ searchArrayList.get(position).getItemOne());
			holder.itemTwo.setText(" | "
					+ searchArrayList.get(position).getItemTwo());
			holder.itemThree.setText(" | KES."
					+ searchArrayList.get(position).getItemThree());
		}

		if (activityName.equals("ActivityMyAccountOrders")) {
			holder.viewOrder.setTypeface(font);
			holder.viewOrder.setOnClickListener(this);
			holder.confirmDelivery.setOnClickListener(this);

			String status = searchArrayList.get(position).getItemStatus();

			// CLICK ON VIEW ORDER
			holder.viewOrder.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					View parentRow = (View) v.getParent();
					ListView listView = (ListView) parentRow.getParent();
					final int position = listView.getPositionForView(parentRow);
					RequestedSimpleListOrders item = (RequestedSimpleListOrders) listView
							.getItemAtPosition(position);

					Intent intent = new Intent(context,
							ActivityMyAccountOrderView.class);
					intent.putExtra("customer_name", item.item_customer_name);
					intent.putExtra("customer_email", item.item_customer_email);
					intent.putExtra("customer_address",
							item.item_customer_address);
					intent.putExtra("order", item.item_customer_order);
					intent.putExtra("orderDate", item.item_one);
					intent.putExtra("orderOutlet", item.item_two);
					intent.putExtra("orderAmount", item.item_three);
					intent.putExtra("orderId", item.item_id);
					intent.putExtra("orderRefNumber", item.item_ref_number);
					context.startActivity(intent);
				}
			});

			// CLICK ON CONFIRM DELIVERY
			if (status.equals("1")) {
				holder.confirmDelivery
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {

								View parentRow = (View) v.getParent();
								ListView listView = (ListView) parentRow
										.getParent();
								final int position = listView
										.getPositionForView(parentRow);
								RequestedSimpleListOrders item = (RequestedSimpleListOrders) listView
										.getItemAtPosition(position);
								ORDER_ID = item.item_id;

								new HttpAsyncTask()
										.execute("http://www.e-fam.com/mobile_trolley_app/confirmdelivery.php"); // call
																															// AsynTask
																															// to
																															// perform
																															// network
																															// operation
																															// on
																															// separate
																															// thread
								Log.i("selected item ",
										"confirm delivery clicked");
							}
						});

				holder.confirmDelivery.setText("Confirm Delivery");
				holder.confirmDelivery
						.setBackgroundResource(R.drawable.customgreenbuttonshape_2);
			}

			if (status.equals("0")) {
				holder.confirmDelivery.setText("Delivery Confirmed");
				holder.confirmDelivery
						.setBackgroundResource(R.drawable.customgreybuttonshape);
			}
		}

		if (activityName.equals("ActivityMyAccountOrderView")) {
			holder.viewOrder.setVisibility(View.GONE);
			holder.confirmDelivery.setVisibility(View.GONE);

			// Reposition item one position
			RelativeLayout.LayoutParams itemTwoParams = (RelativeLayout.LayoutParams) holder.itemTwo
					.getLayoutParams();
			itemTwoParams.addRule(RelativeLayout.RIGHT_OF,
					holder.itemOne.getId());
			itemTwoParams.addRule(RelativeLayout.BELOW, 0);
			holder.itemTwo.setLayoutParams(itemTwoParams);

			// Reposition item two position
			RelativeLayout.LayoutParams itemThreeParams = (RelativeLayout.LayoutParams) holder.itemThree
					.getLayoutParams();
			itemThreeParams.addRule(RelativeLayout.RIGHT_OF,
					holder.itemTwo.getId());
			itemThreeParams.addRule(RelativeLayout.BELOW, 0);
			holder.itemThree.setLayoutParams(itemThreeParams);
		}

		return convertView;

	}

	private Object getResources() {
		// TODO Auto-generated method stub
		return null;
	}

	static class ViewHolder {
		TextView itemOne;
		TextView itemTwo;
		TextView itemThree;
		Button confirmDelivery;
		Button viewOrder;
	}

	private Context getContext() {
		// TODO Auto-generated method stub
		return null;
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

			if (result.equals("0")) {
				Toast.makeText(context, "Delivery Confirmed", Toast.LENGTH_LONG)
						.show();
				ratingForm.setVisibility(View.VISIBLE);

			} else {
				Toast.makeText(context, "Delivery not confirmed",
						Toast.LENGTH_LONG).show();
			}

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

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("order_id", ORDER_ID));

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
