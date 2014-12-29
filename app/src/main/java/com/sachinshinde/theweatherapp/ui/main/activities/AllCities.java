package com.sachinshinde.theweatherapp.ui.main.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.sachinshinde.theweatherapp.R;
import com.sachinshinde.theweatherapp.ui.main.fragments.LocationDetailFragment;
import com.sachinshinde.theweatherapp.ui.main.fragments.LocationListFragment;
import com.sachinshinde.theweatherapp.ui.main.views.DrawShadowFrameLayout;


public class AllCities extends BaseActivity implements
        LocationListFragment.Callbacks  {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_all_cities);

//        Toolbar toolbar = getActionBarToolbar();

//        overridePendingTransition(0, 0);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        CollectionView collectionView = (CollectionView) findViewById(R.id.sessions_collection_view);
//
//        if (collectionView != null) {
//            Log.d(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", "not null");
//            enableActionBarAutoHide(collectionView);
//        } else {
//            Log.d(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", "null");
//        }


        mLocationsFrag = (LocationListFragment) getSupportFragmentManager().findFragmentById(
                R.id.sessions_fragment);
        if (mLocationsFrag != null && savedInstanceState == null) {
            Bundle args = intentToFragmentArguments(getIntent());
//            mLocationsFrag.reloadFromArguments(args);
        }

        registerHideableHeaderView(findViewById(R.id.headerbar));
    }

    /**
     * Converts an intent into a {@link android.os.Bundle} suitable for use as fragment arguments.
     */
    public static Bundle intentToFragmentArguments(Intent intent) {
        Bundle arguments = new Bundle();
        if (intent == null) {
            return arguments;
        }

        final Uri data = intent.getData();
        if (data != null) {
            arguments.putParcelable("_uri", data);
        }

        final Bundle extras = intent.getExtras();
        if (extras != null) {
            arguments.putAll(intent.getExtras());
        }

        return arguments;
    }


    @Override
    protected void onActionBarAutoShowOrHide(boolean shown) {
        super.onActionBarAutoShowOrHide(shown);
        mDrawShadowFrameLayout.setShadowVisible(shown, shown);
    }

    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        if (mLocationsFrag != null) {
            return mLocationsFrag.canCollectionViewScrollUp();
        }
        return super.canSwipeRefreshChildScrollUp();
    }

    private LocationListFragment mLocationsFrag = null;
    private DrawShadowFrameLayout mDrawShadowFrameLayout;

    @Override protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return BaseActivity.NAVDRAWER_ITEM_OVERVIEW;
    }
}
