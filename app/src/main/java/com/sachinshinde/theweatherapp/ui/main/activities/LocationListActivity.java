package com.sachinshinde.theweatherapp.ui.main.activities;

import com.sachinshinde.theweatherapp.R;
import com.sachinshinde.theweatherapp.db.LocationDBHandler;
import com.sachinshinde.theweatherapp.db.Locations;
import com.sachinshinde.theweatherapp.ui.main.fragments.LocationDetailFragment;
import com.sachinshinde.theweatherapp.ui.main.fragments.LocationListFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * An activity representing a list of Persons. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link com.sachinshinde.theweatherapp.ui.main.activities.LocationDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link com.sachinshinde.theweatherapp.ui.main.fragments.LocationListFragment} and the item details (if present) is a
 * {@link com.sachinshinde.theweatherapp.ui.main.fragments.LocationDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link com.sachinshinde.theweatherapp.ui.main.fragments.LocationListFragment.Callbacks} interface to listen for item selections.
 */
public class LocationListActivity extends FragmentActivity implements
        LocationListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_list);

//		if (findViewById(R.id.person_detail_container) != null) {
//			// The detail container view will be present only in the
//			// large-screen layouts (res/values-large and
//			// res/values-sw600dp). If this view is present, then the
//			// activity should be in two-pane mode.
//			mTwoPane = true;
//
//			// In two-pane mode, list items should be given the
//			// 'activated' state when touched.
//			((LocationListFragment) getSupportFragmentManager().findFragmentById(
//					R.id.person_list)).setActivateOnItemClick(true);
//		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link com.sachinshinde.theweatherapp.ui.main.fragments.LocationListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(long id) {
//		if (mTwoPane) {
//			// In two-pane mode, show the detail view in this activity by
//			// adding or replacing the detail fragment using a
//			// fragment transaction.
//			Bundle arguments = new Bundle();
//			arguments.putLong(LocationDetailFragment.ARG_ITEM_ID, id);
//			LocationDetailFragment fragment = new LocationDetailFragment();
//			fragment.setArguments(arguments);
//			getSupportFragmentManager().beginTransaction()
//					.replace(R.id.person_detail_container, fragment).commit();
//
//		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
        Intent detailIntent = new Intent(this, LocationDetailActivity.class);
        detailIntent.putExtra(LocationDetailFragment.ARG_ITEM_ID, id);
        startActivity(detailIntent);
//		}
	}


	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result = false;
		if (R.id.newLocation == item.getItemId()) {
			result = true;
			// Create a new person.
			Locations p = new Locations("","","","","", -1);
			LocationDBHandler.getInstance(this).putLocation(p);
			// Open a new fragment with the new id
			onItemSelected(p.id);
		} else if (R.id.action_remove == item.getItemId()){

        }

		return result;
	}*/
}