package com.kenfeng.erp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DealerActivity extends FragmentActivity {

	// When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    DealerPagerAdapter mDealerPagerAdapter;
    ViewPager mViewPager;
    ActionBar mActionBar;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dealer);
		mActionBar = getActionBar();
		
		// Specify that tabs should be displayed in the action bar.
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	    // Create a tab listener that is called when the user changes tabs.
	    ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}

	    };

	    // Add 3 tabs, specifying the tab's text and TabListener
	    for (int i = 0; i < 2; i++) {
	    	mActionBar.addTab(
	    			mActionBar.newTab()
	                        .setText("Tab " + (i + 1))
	                        .setTabListener(tabListener));
	    }
	    
	    mViewPager = (ViewPager) findViewById(R.id.dealer_pager);
	    mDealerPagerAdapter = new DealerPagerAdapter(getSupportFragmentManager());
	    mViewPager.setAdapter(mDealerPagerAdapter);
	    mViewPager.setOnPageChangeListener(
	            new ViewPager.SimpleOnPageChangeListener() {
	                @Override
	                public void onPageSelected(int position) {
	                    // When swiping between pages, select the
	                    // corresponding tab.
	                    getActionBar().setSelectedNavigationItem(position);
	                }
	            });
	}
		
	public class DealerPagerAdapter extends FragmentPagerAdapter{

		public DealerPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int i) {
			// The other sections of the app are dummy placeholders.
            Fragment fragment = new DealerObjectFragment();
            Bundle args = new Bundle();
            args.putInt(DealerObjectFragment.ARG_OBJECT, i + 1);
            fragment.setArguments(args);
            return fragment;
		}

	    @Override
	    public int getCount() {
	        return 2;
	    }

		
	}
	
	public static class DealerObjectFragment extends Fragment{
		public static final String ARG_OBJECT = "object";

		
	    @Override
	    public View onCreateView(LayoutInflater inflater,
	            ViewGroup container, Bundle savedInstanceState) {
	    	
	        // The last two arguments ensure LayoutParams are inflated
	        // properly.
	        View rootView = inflater.inflate(
	                R.layout.fragment_item, container, false);
	        Bundle args = getArguments();
	        ((TextView) rootView.findViewById(R.id.fragment_item_title)).setText(
	                Integer.toString(args.getInt(ARG_OBJECT)));

	        
	        return rootView;
	    }
	}
	
}
