package com.redhering.nunuaraha;

import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HelperDowloadImage extends AsyncTask<String, Void, Bitmap> {
	 ImageView bmImage;

     public HelperDowloadImage(ImageView bmImage) {
         this.bmImage = bmImage;
     }

     protected Bitmap doInBackground(String... urls) {
         String urldisplay = urls[0];
         Bitmap mIcon11 = null;
         try {
             InputStream in = new java.net.URL(urldisplay).openStream();
             mIcon11 = BitmapFactory.decodeStream(in);
         } catch (Exception e) {
             Log.e("Error", e.getMessage());
             e.printStackTrace();
         }
         return mIcon11;
     }

     protected void onPostExecute(Bitmap result) {
         //bmImage.setImageBitmap(result);
     }

}
