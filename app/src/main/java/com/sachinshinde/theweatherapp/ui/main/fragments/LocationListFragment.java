package com.sachinshinde.theweatherapp.ui.main.fragments;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sachinshinde.theweatherapp.R;
import com.sachinshinde.theweatherapp.db.LocationDBHandler;
import com.sachinshinde.theweatherapp.db.Locations;
import com.sachinshinde.theweatherapp.db.LocationsProvider;
import com.sachinshinde.theweatherapp.libs.SwipeDismissRecyclerViewTouchListener;
import com.sachinshinde.theweatherapp.ui.main.adapters.CursorRecyclerViewAdapter;


/**
 * A list fragment representing a list of Persons. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link LocationDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class LocationListFragment extends Fragment {

	/**
     * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(long l);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(long id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public LocationListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		setListAdapter(new SimpleCursorAdapter(getActivity(),
//				R.layout.location_listitem, null, new String[] {
//						Locations.KEY_NAME, Locations.KEY_IS_MY_LOC,
//						Locations.KEY_GMT, Locations.KEY_LAT, Locations.KEY_LON }, new int[] { R.id.cardLocationName,
//						R.id.cardIsMyLocation, R.id.cardGMT, R.id.cardLat, R.id.cardLon}, 0));


	}

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
//    private RecyclerView.Adapter<CustomViewHolder> mAdapter;
    CursorRecyclerViewAdapter<CustomViewHolder> mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_list, null);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.rvMain);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CursorRecyclerViewAdapter<CustomViewHolder>(getActivity(), null) {
            @Override
            public void onBindViewHolder(CustomViewHolder viewHolder, Cursor cursor) {
//                cursor.moveToFirst();
                Locations item = new Locations(cursor);
                viewHolder.location_name.setText(item.city_name);
                viewHolder.is_my_loc.setText(item.is_my_loc);
                viewHolder.gmt.setText(item.gmt);
                viewHolder.lat.setText(item.lat);
                viewHolder.lon.setText(item.lon);
            }

            @Override
            public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.location_listitem
                        , viewGroup, false);
                return new CustomViewHolder(view);
            }
        };

        // Load the content
        getLoaderManager().initLoader(0, null, new LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(getActivity(),
                        LocationsProvider.URI_LOCATIONS, Locations.FIELDS, null, null,
                        null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
                mAdapter.swapCursor(c);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> arg0) {
                mAdapter.swapCursor(null);
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        SwipeDismissRecyclerViewTouchListener touchListener =
                new SwipeDismissRecyclerViewTouchListener(
                        mRecyclerView,
                        new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
//                                    mLayoutManager.removeView(mLayoutManager.getChildAt(position));
//                                    items.remove(position);
                                    Cursor cursor = mAdapter.getCursor();
                                    cursor.moveToPosition(position);
                                    LocationDBHandler.getInstance(getActivity()).removeLocation(new Locations(cursor));
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });
        mRecyclerView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        mRecyclerView.setOnScrollListener(touchListener.makeScrollListener());
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getActivity(), "Clicked ", Toast.LENGTH_SHORT).show();
                        mCallbacks.onItemSelected(mAdapter.getItemId(position));
                    }
                }));

        return view;
	}

    private class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView location_name;
        private TextView is_my_loc;
        private TextView gmt;
        private TextView lat;
        private TextView lon;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.location_name = (TextView) itemView.findViewById(R.id.cardLocationName);
            this.is_my_loc = (TextView) itemView.findViewById(R.id.cardIsMyLocation);
            this.gmt = (TextView) itemView.findViewById(R.id.cardGMT);
            this.lat = (TextView) itemView.findViewById(R.id.cardLat);
            this.lon = (TextView) itemView.findViewById(R.id.cardLon);
        }
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            Log.d("TheWeatherApp", "restoring " + savedInstanceState
                    .getInt(STATE_ACTIVATED_POSITION));
//            setActivatedPosition(savedInstanceState
//					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

//	@Override
//	public void onListItemClick(ListView listView, View view, int position,
//			long id) {
//		super.onListItemClick(listView, view, position, id);
//
//		// Notify the active callbacks interface (the activity, if the
//		// fragment is attached to one) that an item has been selected.
//		mCallbacks.onItemSelected(getListAdapter().getItemId(position));
//	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

//	/**
//	 * Turns on activate-on-click mode. When this mode is on, list items will be
//	 * given the 'activated' state when touched.
//	 */
//	public void setActivateOnItemClick(boolean activateOnItemClick) {
//		// When setting CHOICE_MODE_SINGLE, ListView will automatically
//		// give items the 'activated' state when touched.
////		mRecyclerView.setChoiceMode(
////				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
////						: ListView.CHOICE_MODE_NONE);
//	}
//
//	private void setActivatedPosition(int position) {
////		if (position == ListView.INVALID_POSITION) {
////			getListView().setItemChecked(mActivatedPosition, false);
////		} else {
////			getListView().setItemChecked(position, true);
////		}
//
//		mActivatedPosition = position;
//	}

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }
    }
}