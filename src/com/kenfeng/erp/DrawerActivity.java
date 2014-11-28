package com.kenfeng.erp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.android.map.CalloutStyle;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Unit;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;


public class DrawerActivity extends Activity {

	private String[] mItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mType;
    private ActionBar mActionBar;
    private Query mQuery;
    private long mErpLayerID = 0;
    private long mAroundLayerId = 0;
    private long mBufferLayerID = 0;
    private static MapView mMapView;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer);
		
		mQuery = new Query(getApplicationContext());
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
        //init Map
        initMap();
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
		
		new AddGraphicsLayer().execute(position);
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
	 * 用于Dp转像素
	 * 
	 * @param context
	 *            上下文
	 * @param dp
	 *            DIP
	 * @return int PX
	 */
	public static int Dp2Px(Context context, int dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
	
	public void initMap() {
		// Retrieve the map and initial extent from XML layout
        mMapView = (MapView) findViewById(R.id.map);
        // Add tiled layer to MapView
		mMapView.addLayer(new ArcGISTiledMapServiceLayer(getResources().getString(R.string.base_map)));
		// Add single tab
		mMapView.setOnSingleTapListener(new OnSingleTapListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onSingleTap(float x, float y) {
				int[] graphicIDs = null;
				GraphicsLayer mGraphicsLayer =null;
				if(mErpLayerID != 0){
					if(mMapView.getLayerByID(mErpLayerID)!=null){
						if(mMapView.getLayerByID(mAroundLayerId)!=null){
							mGraphicsLayer = (GraphicsLayer) mMapView.getLayerByID(mAroundLayerId);
							graphicIDs = mGraphicsLayer.getGraphicIDs(x, y, 25);
							if (graphicIDs != null && graphicIDs.length == 0){
								mGraphicsLayer = (GraphicsLayer) mMapView.getLayerByID(mErpLayerID);
								graphicIDs = mGraphicsLayer.getGraphicIDs(x, y, 25);
							}
						}else{
							mGraphicsLayer = (GraphicsLayer) mMapView.getLayerByID(mErpLayerID);
							graphicIDs = mGraphicsLayer.getGraphicIDs(x, y, 25);
						}
						if (graphicIDs != null && graphicIDs.length > 0) {
							final Callout mCallout = mMapView.getCallout();
							CalloutStyle mStyle = new CalloutStyle();
							mStyle.setAnchor(5);
							mStyle.setCornerCurve(10);
							mStyle.setMaxHeight(Dp2Px(DrawerActivity.this, 48));
							mStyle.setMaxWidth(Dp2Px(DrawerActivity.this, 300));
							mStyle.setBackgroundColor(0xffFF8800);
							mStyle.setFrameColor(0xffFF8800);
							LayoutInflater mInflater = LayoutInflater
									.from(DrawerActivity.this);
							View mView = mInflater.inflate(R.layout.callout, null);
							TextView mTextView = (TextView) mView.findViewById(R.id.txtCallout);
							ImageView mImageView = (ImageView)mView.findViewById(R.id.imgCalloutMore);
							Graphic mGraphic = mGraphicsLayer
									.getGraphic(graphicIDs[0]);
							String poiName = (String) mGraphic
									.getAttributeValue("NAME");
							final String poiType = (String) mGraphic
									.getAttributeValue("TYPE");
							final String poiId = (String) mGraphic
									.getAttributeValue("ID");
							if (poiName.length() > 10) {
								String name = poiName.substring(0, 10);
								mTextView.setText(name + "...");
							} else {
								String name = poiName;
								mTextView.setText(name);
							}
							mImageView.setOnClickListener(new View.OnClickListener(){

								@Override
								public void onClick(View view) {
									// TODO Auto-generated method stub
									Intent mIntent = new Intent(DrawerActivity.this,
											ErpDetailActivity.class);
									mIntent.putExtra("TYPE", poiType);
									mIntent.putExtra("ID", poiId);
									DrawerActivity.this
									.startActivity(mIntent);
									DrawerActivity.this
									.overridePendingTransition(
											R.anim.anim_in_right2left,
											R.anim.anim_out_right2left);
									mCallout.hide();
								}
								
							});
							mCallout.setStyle(mStyle);
							mCallout.show((Point) mGraphic.getGeometry(), mView);
						}
					}
				}
				Callout mCallout = mMapView.getCallout();
				if (mCallout!=null) {
					mCallout.hide();
				}
				
			}
			
			
		});
		// Add long tab
		mMapView.setOnLongPressListener(new OnLongPressListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean onLongPress(float x, float y) {
				// TODO Auto-generated method stub
				if(mErpLayerID != 0){
					if(mMapView.getLayerByID(mErpLayerID)!=null){
						GraphicsLayer mGraphicsLayer = (GraphicsLayer) mMapView.getLayerByID(mErpLayerID);
						int[] graphicIDs = mGraphicsLayer.getGraphicIDs(x, y, 25);
						if (graphicIDs != null && graphicIDs.length > 0) {
							Graphic mGraphic = mGraphicsLayer.getGraphic(graphicIDs[0]);
							addBuffer(mGraphic);
							new AddAroundGraphicLayer().execute(mGraphic);
						}
						
						return true;
					}
					else{
						return false;
					}
				}
				else{
					Callout mCallout = mMapView.getCallout();
					if (mCallout!=null) {
						mCallout.hide();
					}
					return false;	
				}
			}
		});
	}
    
	public void addBuffer(Graphic graphic) {
		if(mBufferLayerID != 0){
			if(mMapView.getLayerByID(mBufferLayerID)!=null){
				mMapView.removeLayer(mMapView.getLayerByID(mBufferLayerID));
			}
		}
		Polygon mbuffer = GeometryEngine.buffer(graphic.getGeometry(), mMapView.getSpatialReference(), 100.0, Unit.create(LinearUnit.Code.KILOMETER));
		SimpleFillSymbol mFillSymbol = new SimpleFillSymbol(Color.argb(220, 255, 255, 0));
		GraphicsLayer mGraphicsLayer = new GraphicsLayer();
		Graphic mGraphic = new Graphic(mbuffer, mFillSymbol);
		mGraphicsLayer.addGraphic(mGraphic);
		mMapView.addLayer(mGraphicsLayer);
		mBufferLayerID = mGraphicsLayer.getID();
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
    
    public class AddGraphicsLayer extends AsyncTask<Integer, Integer, GraphicsLayer>{
    	    	
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(mErpLayerID != 0){
				if(mMapView.getLayerByID(mErpLayerID)!=null){
					mMapView.removeLayer(mMapView.getLayerByID(mErpLayerID));
				}
			}
			if(mErpLayerID != 0){
				if(mMapView.getLayerByID(mAroundLayerId)!=null){
					mMapView.removeLayer(mMapView.getLayerByID(mAroundLayerId));
				}
			}
		}

		@Override
		protected GraphicsLayer doInBackground(Integer... position) {
			// TODO Auto-generated method stub
			GraphicsLayer mGraphicsLayer = new GraphicsLayer();
			mGraphicsLayer = mQuery.getPOIGraphicsViaType(position[0]);
			mErpLayerID = mGraphicsLayer.getID();
			return mGraphicsLayer;
		}
    	
		@Override
		protected void onPostExecute(GraphicsLayer result) {
			super.onPostExecute(result);
			mMapView.addLayer(result);
			Toast.makeText(DrawerActivity.this, "成功加载地图数据", Toast.LENGTH_SHORT)
					.show();
		}
    }
    
    public class AddAroundGraphicLayer extends AsyncTask<Graphic, Graphic, GraphicsLayer>{
    	@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(mAroundLayerId != 0){
				if(mMapView.getLayerByID(mAroundLayerId)!=null){
					mMapView.removeLayer(mMapView.getLayerByID(mAroundLayerId));
				}
			}
		}

		@Override
		protected GraphicsLayer doInBackground(Graphic... poi) {
			// TODO Auto-generated method stub
			GraphicsLayer mGraphicsLayer = new GraphicsLayer();
			mGraphicsLayer = mQuery.getPOIsAround(poi[0]);
			mAroundLayerId = mGraphicsLayer.getID();
			return mGraphicsLayer;
		}
    	
		@Override
		protected void onPostExecute(GraphicsLayer result) {
			super.onPostExecute(result);
			mMapView.addLayer(result);
			Toast.makeText(DrawerActivity.this, "成功加载地图数据", Toast.LENGTH_SHORT)
					.show();
		}
    }
}
