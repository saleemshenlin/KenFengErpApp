package com.kenfeng.erp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;


public class DrawerActivity extends Activity {

	private String[] mItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mType;
    private ActionBar mActionBar;
    private static MapView mMapView;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer);
		
		mTitle = mDrawerTitle = getTitle();
		mActionBar = getActionBar();
		mItemTitles = getResources().getStringArray(R.array.items);
		mType = getResources().getStringArray(R.array.types);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        FragmentItemAdapter mFragmentItemAdapter = new FragmentItemAdapter(this,R.layout.drawer_list_item, mItemTitles);
        mDrawerList.setAdapter(mFragmentItemAdapter);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        // Retrieve the map and initial extent from XML layout
        mMapView = (MapView) findViewById(R.id.map);
        // Add dynamic layer to MapView
		mMapView.addLayer(new ArcGISTiledMapServiceLayer(getResources().getString(R.string.base_map)));
		
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // Create a tab listener that is called when the user changes tabs.
	    ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				Toast.makeText(DrawerActivity.this, tab.getText(), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}

	    };
        for (int i = 0; i < 2; i++) {
			Tab tab = mActionBar.newTab();
			tab.setText(mType[i]).setTabListener(tabListener);
			mActionBar.addTab(tab);
		}
        if (savedInstanceState == null) {
            selectItem(0);
        }
		
	}
	
	protected void onPause() {
		super.onPause();
		if(mMapView!=null)
		{
			mMapView.pause();
		}
	}

	protected void onResume() {
		super.onResume();
		if(mMapView!=null)
		{
			mMapView.unpause();
		}
	}
	
	 /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        selectItem(position);
	    }
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
	    // Create a new fragment and specify the planet to show based on position
//	    Fragment fragment = new ItemFragment();
//	    Bundle args = new Bundle();
//	    args.putInt(ItemFragment.ARG_ITEM_NUMBER, position);
//	    fragment.setArguments(args);
//
//	    // Insert the fragment by replacing any existing fragment
//	    FragmentManager fragmentManager = getFragmentManager();
//	    fragmentManager.beginTransaction()
//	                   .replace(R.id.content_frame, fragment)
//	                   .commit();

	    // Highlight the selected item, update the title, and close the drawer
	    mDrawerList.setItemChecked(position, true);
	    setTitle(mItemTitles[position]);
	    //Toast.makeText(getApplicationContext(), mItemTitles[position], Toast.LENGTH_SHORT).show();
	    mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
	    mTitle = title;
	    getActionBar().setTitle(mTitle);
	}
    /**
     * Fragment Item Adapter
     */
    public static class FragmentItemAdapter extends ArrayAdapter<CharSequence> {
    	private int resourceId;    	
		public FragmentItemAdapter(Context context, int resource, String[] mPlanetTitles) {
			super(context, resource, mPlanetTitles);
			this.resourceId = resource;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CharSequence mItem = getItem(position);
			LinearLayout mLinearLayout = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					inflater);
			vi.inflate(resourceId, mLinearLayout, true);
			TextView mItemTitle = (TextView) mLinearLayout
					.findViewById(R.id.draw_item_title);
			
			
			mItemTitle.setText(mItem);
			return mLinearLayout;
		}
		
		
	}
}
