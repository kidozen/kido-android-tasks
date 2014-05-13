package com.kidozen.examples.tasks;

import org.json.JSONObject;

import com.kidozen.examples.services.DataHelper;
import com.kidozen.examples.services.IKidozenApplicationSetup;
import com.kidozen.examples.services.ITaskSaveComplete;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.security.KeyChain;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener, IKidozenApplicationSetup {
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	protected static final String TAG = "MainActivity";
	Boolean isInitialized = false;
	DataHelper dh= null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar to show tabs.
		dh= new DataHelper();
		dh.SetupKidozen(this);
		
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current tab position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
			case R.id.menu_new:
				final Dialog dialog = new Dialog(this); 
                dialog.setContentView(R.layout.dialog_create);
                dialog.setTitle("Create");                
                final TextView titleText = (TextView) dialog.findViewById(R.id.editTextTitle);
                final TextView descText = (TextView) dialog.findViewById(R.id.editTextDesc);
                   
                //set up button
                Button button = (Button) dialog.findViewById(R.id.button1);
                button.setOnClickListener(new OnClickListener() {
                	@Override
                    public void onClick(View v) {
                		runOnUiThread(new  Runnable() {
							public void run() {
		                		dh.insertTask(
		                				titleText.getText().toString(), 
		                				descText.getText().toString(), new ITaskSaveComplete() {
		    						@Override
		    						public void onSaveComplete(boolean status, JSONObject kidozenResponse) {
		    	                        dialog.cancel();
		    						}
		    					});
							}
						});
                		
                    }
                });
                dialog.show();
				//Toast.makeText(MainActivity.this,"Create Operation is performed!",Toast.LENGTH_SHORT ).show();
				return true;
			case R.id.menu_settings:
				Toast.makeText(MainActivity.this,"Settings Operation is performed!",Toast.LENGTH_SHORT ).show();
				return true;
			case R.id.action_refresh:
                final ActionBar actionBar = getActionBar();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                this.onTabSelected(actionBar.getSelectedTab(), ft);
                return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, show the tab contents in the
		// container view.
		Fragment fragment = new TasksFragment();
		Bundle args = new Bundle();
		args.putInt(TasksFragment.ARG_SECTION_NUMBER,
				tab.getPosition() + 1);
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onKidozenAppSetupComplete(int status) {
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section1)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section2)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section3)
				.setTabListener(this));
	}
}
