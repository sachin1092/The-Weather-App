package com.sachinshinde.theweatherapp.ui.main.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sachinshinde.theweatherapp.R;
import com.sachinshinde.theweatherapp.db.LocationDBHandler;
import com.sachinshinde.theweatherapp.db.Locations;

public class LocationDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The Location this fragment is presenting.
     */
    private Locations mItem;


    /**
     * The UI elements showing the details of the Location
     */
    private TextView textLocationName;
    private TextView textIsMyLoc;
    private TextView textGMT;
    private TextView textLatitude;
    private TextView textLongitude;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LocationDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = LocationDBHandler.getInstance(getActivity()).getLocation(getArguments().getLong(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_locations_detail, container, false);

        if (mItem != null) {
            textLocationName = ((TextView) rootView.findViewById(R.id.textLocationName));
            textLocationName.setText(mItem.city_name);

            textIsMyLoc = ((TextView) rootView.findViewById(R.id.textIsMyLocation));
            textIsMyLoc.setText(mItem.is_my_loc);

            textGMT = ((TextView) rootView.findViewById(R.id.textgmt));
            textGMT.setText(mItem.gmt);

            textLatitude = ((TextView) rootView.findViewById(R.id.textlat));
            textLatitude.setText(mItem.lat);

            textLongitude = ((TextView) rootView.findViewById(R.id.textlon));
            textLongitude.setText(mItem.lon);
        }

        rootView.findViewById(R.id.bUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locations p = new Locations(textLocationName.getText().toString(), textIsMyLoc.getText().toString(), textGMT.getText().toString(), textLatitude.getText().toString(), textLongitude.getText().toString(), mItem == null ? -1 : mItem.id);
                if (LocationDBHandler.getInstance(getActivity().getApplicationContext()).putLocation(p)) {
                    Toast.makeText(getActivity(), "Update successful", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }

            }
        });

        rootView.findViewById(R.id.bDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItem != null) {
                    LocationDBHandler.getInstance(getActivity().getApplicationContext()).removeLocation(mItem);
                    getActivity().finish();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
//        updateLocationFromUI();
        }
//
//    private void updateLocationFromUI() {
//        if (mItem != null) {
//            mItem.city_name = textLocationName.getText().toString();
//            mItem.is_my_loc = textIsMyLoc.getText().toString();
//            mItem.gmt = textGMT.getText().toString();
//            mItem.lat = textLatitude.getText().toString();
//            mItem.lon = textLongitude.getText().toString();
//
//            LocationDBHandler.getInstance(getActivity()).putLocation(mItem);
//        }
//    }
}