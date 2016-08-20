package com.authorwjf.loaderexample;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity implements LoaderCallbacks<List<String>>{

	private static final int THE_LOADER = 0x01;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getLoaderManager().initLoader(THE_LOADER, null, this).forceLoad();
	}

  	@Override
	public Loader<List<String>> onCreateLoader(int id, Bundle args) {
		SampleLoader loader = new SampleLoader(this);
		return loader;
	}
	
	@Override
	public void onLoadFinished(Loader<List<String>> loader, List<String> list) {
		final ListView listview = (ListView) findViewById(R.id.listview);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				  android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);
	}
	
	@Override
	public void onLoaderReset(Loader<List<String>> loader) {
		final ListView listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(null);
	}
	
	private static class SampleLoader extends AsyncTaskLoader<List<String>> {

		public SampleLoader(Context context) {
			super(context);
		}

		@Override
		public List<String> loadInBackground() {
			
			final String[] animals = new String[] { "Ape", "Bird", "Cat", "Dog", "Elephant","Fox",
				"Gorilla", "Hyena", "Inch Worm", "Jackalope", "King Salmon","Lizard",
				"Monkey", "Narwhal", "Octopus", "Pig", "Quail", "Rattle Snake", "Salamander",
				"Tiger", "Urchin", "Vampire Bat", "Wombat", "X-Ray Tetra", "Yak", "Zebra"};
		
		
			final ArrayList<String> list = new ArrayList<String>();		  
			for (int i = 0; i < animals.length; ++i) {
			      list.add(animals[i]);
			      try {				
			    	  Thread.sleep(100); //simulated network delay
			      } catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
			      }		  
			}
			return list;
		}
		
	}

} 