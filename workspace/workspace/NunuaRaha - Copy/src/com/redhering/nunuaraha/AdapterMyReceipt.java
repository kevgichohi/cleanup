package com.redhering.nunuaraha;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.redhering.nunuaraha.R;
 
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.TextView;
 
public class AdapterMyReceipt extends BaseAdapter {
	private static ArrayList<RequestedResultsReceipt> myReceipt;
    private LayoutInflater mInflater;
    private Context context;
    private int resource;
    
    public static final String MY_SESSION = "mySession";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    Typeface EkMukta_Light;
 
    public AdapterMyReceipt(Context ctx, ArrayList<RequestedResultsReceipt> results) {
    	myReceipt = results;
        mInflater = LayoutInflater.from(ctx);
        context=ctx;
  		EkMukta_Light = Typeface.createFromAsset(context.getAssets(),"fonts/ek_mukta/EkMukta-Light.ttf");
  		sharedPreferences = context.getSharedPreferences(MY_SESSION,Context.MODE_PRIVATE);
  		editor = sharedPreferences.edit();
        gson = new Gson();
    }
 
    public int getCount() {
        return myReceipt.size();
    }
 
    public Object getItem(int position) {
        return myReceipt.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.receipt, null);
            holder = new ViewHolder();
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.qty = (TextView) convertView.findViewById(R.id.qty);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.total = (TextView) convertView.findViewById(R.id.total);
            
 
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        
        Integer itemId = Integer.parseInt(myReceipt.get(position).getId());
        if(itemId.equals(100002)){
        	holder.description.setText(myReceipt.get(position).getTitle());
        	holder.description.setTypeface(Typeface.DEFAULT_BOLD);
        	
        	holder.qty.setText(myReceipt.get(position).getUnits().toString());
        	holder.qty.setTypeface(Typeface.DEFAULT_BOLD);
        	
        	holder.price.setText(myReceipt.get(position).getPrice());
        	holder.price.setTypeface(Typeface.DEFAULT_BOLD);
        	
        	holder.total.setText("");
        	holder.total.setTypeface(Typeface.DEFAULT_BOLD);
        	
        }else if(itemId > 100000){
        	holder.description.setText(myReceipt.get(position).getTitle());
        	holder.description.setTypeface(Typeface.DEFAULT_BOLD);
        	
        	holder.qty.setText("");
        	holder.qty.setTypeface(Typeface.DEFAULT_BOLD);
        	
        	holder.price.setText("");
        	holder.price.setTypeface(Typeface.DEFAULT_BOLD);
        	
        	holder.total.setText(" = "+myReceipt.get(position).getPrice());
        	holder.total.setTypeface(Typeface.DEFAULT_BOLD);
        }
        else{
        	holder.description.setText(myReceipt.get(position).getTitle() + " (" +myReceipt.get(position).getSize()+")");
        	holder.description.setTypeface(EkMukta_Light, Typeface.NORMAL);
            
            String price = myReceipt.get(position).getPrice();
            if(android.text.TextUtils.isEmpty(price) == false && android.text.TextUtils.isDigitsOnly(price)){
    			DecimalFormat precision = new DecimalFormat("0.00");
    			Double item_price_double = Double.parseDouble(myReceipt.get(position).getPrice()); 
    			String item_price = precision.format(item_price_double);
    	        holder.price.setText(item_price);
            }else{
            	holder.price.setVisibility(View.GONE);
            }
        	holder.price.setTypeface(EkMukta_Light, Typeface.NORMAL);
            
            holder.qty.setText(myReceipt.get(position).getUnits()+" X ");
        	holder.qty.setTypeface(EkMukta_Light, Typeface.NORMAL);
            
	        Integer total = myReceipt.get(position).getUnits() * Integer.parseInt(myReceipt.get(position).getPrice());
	        String totalString = total.toString();
	        DecimalFormat precisionTotal = new DecimalFormat("0.00");
			Double item_total_double = Double.parseDouble(totalString); 
			String item_total = precisionTotal.format(item_total_double);
	        holder.total.setText(" = "+item_total);
        	holder.total.setTypeface(EkMukta_Light, Typeface.NORMAL);
        }
        
        
        return convertView;
    }
    

	static class ViewHolder {
        TextView description;
        TextView qty;
        TextView price;
        TextView total;
    }
 
   
	private Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}

}
