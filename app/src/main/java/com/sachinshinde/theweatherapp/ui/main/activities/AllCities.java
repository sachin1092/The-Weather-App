package com.sachinshinde.theweatherapp.ui.main.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.sachinshinde.theweatherapp.R;
import com.sachinshinde.theweatherapp.ui.main.fragments.LocationListFragment;
import com.sachinshinde.theweatherapp.ui.main.views.DrawShadowFrameLayout;


public class AllCities extends BaseActivity /*implements
        MultiSwipeRefreshLayout.CanChildScrollUpCallback*/ {


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


        mLocationsFrag = (LocationListFragment) getFragmentManager().findFragmentById(
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
