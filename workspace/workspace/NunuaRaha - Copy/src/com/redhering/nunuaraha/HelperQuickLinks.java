package com.redhering.nunuaraha;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HelperQuickLinks extends Activity {
	public static final String MY_SESSION = "mySession";
    SharedPreferences sharedPreferences;
    private static Context context;
    TextView cartButtonNotification;

    public void create(Button menuIcon, Button resumeShopping, Button pay, TextView cartBtnNotification, Context ctx, SharedPreferences sharedPref) {
    	context = ctx;
	    sharedPreferences = sharedPref;
	    cartButtonNotification = cartBtnNotification;
	    
    	menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(context, ActivityGlobalMenu.class);
            	context.startActivity(intent);
            }
        });

    	resumeShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(context, ActivityStep01ListOutlets.class);
        		context.startActivity(intent);
            }
        }); 
    	
    	/**resumeShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	if(sharedPreferences.contains("myTrolley")) {
            		Intent intent = new Intent(context, ActivityStep05ListAisles.class);
            		context.startActivity(intent);
            	}else{
            		Toast.makeText(context, "No Shop Selected!", Toast.LENGTH_LONG).show();
            	}
	    	    
            }
        }); **/
    	
    	//SET NOTIFICATION FOR THE NUMBER OF ITEMS CURRENTLY IN THE TROLLEY
    	pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(context, ActivityStep08ListCart.class);
            	context.startActivity(intent);
                
            }
        });
    	
		if(sharedPreferences.contains("myTrolley")) {
			String jsonMyCartString = sharedPreferences.getString("myTrolley", null);
			JSONObject jsonMyCartObject;
			try {
				jsonMyCartObject = new JSONObject(jsonMyCartString);
				Integer numberOfItemsInMyTrolley = 0;
				Iterator<String> loop = jsonMyCartObject.keys();
			    while (loop.hasNext()) {
			        String key = loop.next();
			        try {
			            String unitsValue = jsonMyCartObject.getString(key);
			            numberOfItemsInMyTrolley = numberOfItemsInMyTrolley + Integer.parseInt(unitsValue);
			            
			        } catch (JSONException e) {
			            // Something went wrong!
			        }
			    }
			    cartButtonNotification.setText(numberOfItemsInMyTrolley.toString());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
			
		}else{
			cartButtonNotification.setText("0");
		}
		
		pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	if(sharedPreferences.contains("myTrolley")) {
	            	Intent intent = new Intent(context, ActivityStep08ListCart.class);
	            	context.startActivity(intent);
            	}else{
            		Toast.makeText(context, "No items in your cart!", Toast.LENGTH_LONG).show();
            	}
                
            }
        });

    }

}
