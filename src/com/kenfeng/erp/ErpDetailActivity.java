package com.kenfeng.erp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony.Mms.Rate;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

public class ErpDetailActivity extends Activity {
	private String mPoiID;
	private String mPoiType;
	private Query mQuery;
	private BarChart mBarChart;
	private TextView mMonth;
	private TextView mSeason;
	private TextView mYear;
	private TextView mTextChart;
	private String[] mTags;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		Intent mIntent = getIntent();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		mPoiType = mIntent.getExtras().getString("TYPE");
		mPoiID = mIntent.getExtras().getString("ID");
		String[] mQueryStrings = {mPoiID,mPoiType};
		mBarChart = (BarChart) findViewById(R.id.chart);
		mMonth = (TextView) findViewById(R.id.text_view_month);
		mSeason = (TextView) findViewById(R.id.text_view_season);
		mYear = (TextView) findViewById(R.id.text_view_year);
		mTextChart = (TextView)findViewById(R.id.text_view_chart);
		mMonth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				mMonth.setClickable(false);
				mMonth.setTextColor(getResources().getColorStateList(R.color.select_text));
				Drawable mMonthDraw = getResources().getDrawable(R.drawable.ic_month_selected);
				mMonthDraw.setBounds(0, 0, mMonthDraw.getMinimumWidth(), mMonthDraw.getMinimumHeight());
				mMonth.setCompoundDrawables(mMonthDraw, null, null, null);
				
				mSeason.setClickable(true);
				mSeason.setTextColor(getResources().getColorStateList(R.color.unselect_text));
				Drawable mSeasonDraw = getResources().getDrawable(R.drawable.ic_season);
				mSeasonDraw.setBounds(0, 0, mSeasonDraw.getMinimumWidth(), mSeasonDraw.getMinimumHeight());
				mSeason.setCompoundDrawables(mSeasonDraw, null, null, null);
				
				mYear.setClickable(true);
				mYear.setTextColor(getResources().getColorStateList(R.color.unselect_text));
				Drawable mYearDraw = getResources().getDrawable(R.drawable.ic_year);
				mYearDraw.setBounds(0, 0, mYearDraw.getMinimumWidth(), mYearDraw.getMinimumHeight());
				mYear.setCompoundDrawables(mYearDraw, null, null, null);
				
				drawChart(0, 300, mBarChart, mTags);
				mTextChart.setText(getResources().getStringArray(R.array.chart_times)[0]
						+getResources().getStringArray(R.array.chart_titles)[Integer.parseInt(mPoiType)]);
			}
		});
		mSeason.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View view) {
				mMonth.setClickable(true);
				mMonth.setTextColor(getResources().getColorStateList(R.color.unselect_text));
				Drawable mMonthDraw = getResources().getDrawable(R.drawable.ic_month);
				mMonthDraw.setBounds(0, 0, mMonthDraw.getMinimumWidth(), mMonthDraw.getMinimumHeight());
				mMonth.setCompoundDrawables(mMonthDraw, null, null, null);
				
				mSeason.setClickable(false);
				mSeason.setTextColor(getResources().getColorStateList(R.color.select_text));
				Drawable mSeasonDraw = getResources().getDrawable(R.drawable.ic_season_selected);
				mSeasonDraw.setBounds(0, 0, mSeasonDraw.getMinimumWidth(), mSeasonDraw.getMinimumHeight());
				mSeason.setCompoundDrawables(mSeasonDraw, null, null, null);
				
				mYear.setClickable(true);
				mYear.setTextColor(getResources().getColorStateList(R.color.unselect_text));
				Drawable mYearDraw = getResources().getDrawable(R.drawable.ic_year);
				mYearDraw.setBounds(0, 0, mYearDraw.getMinimumWidth(), mYearDraw.getMinimumHeight());
				mYear.setCompoundDrawables(mYearDraw, null, null, null);
				
				drawChart(1, 800, mBarChart, mTags);
				mTextChart.setText(getResources().getStringArray(R.array.chart_times)[1]
						+getResources().getStringArray(R.array.chart_titles)[Integer.parseInt(mPoiType)]);
			}
		});
		mYear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				mMonth.setClickable(true);
				mMonth.setTextColor(getResources().getColorStateList(R.color.unselect_text));
				Drawable mMonthDraw = getResources().getDrawable(R.drawable.ic_month);
				mMonthDraw.setBounds(0, 0, mMonthDraw.getMinimumWidth(), mMonthDraw.getMinimumHeight());
				mMonth.setCompoundDrawables(mMonthDraw, null, null, null);
				
				mSeason.setClickable(true);
				mSeason.setTextColor(getResources().getColorStateList(R.color.unselect_text));
				Drawable mSeasonDraw = getResources().getDrawable(R.drawable.ic_season);
				mSeasonDraw.setBounds(0, 0, mSeasonDraw.getMinimumWidth(), mSeasonDraw.getMinimumHeight());
				mSeason.setCompoundDrawables(mSeasonDraw, null, null, null);
				
				mYear.setClickable(false);
				mYear.setTextColor(getResources().getColorStateList(R.color.select_text));
				Drawable mYearDraw = getResources().getDrawable(R.drawable.ic_year_selected);
				mYearDraw.setBounds(0, 0, mYearDraw.getMinimumWidth(), mYearDraw.getMinimumHeight());
				mYear.setCompoundDrawables(mYearDraw, null, null, null);
				
				drawChart(2, 2800, mBarChart, mTags);
				mTextChart.setText(getResources().getStringArray(R.array.chart_times)[2]
						+getResources().getStringArray(R.array.chart_titles)[Integer.parseInt(mPoiType)]);
			}
		});
		mMonth.setTextColor(getResources().getColorStateList(R.color.select_text));
		Drawable mMonthDraw = getResources().getDrawable(R.drawable.ic_month_selected);
		mMonthDraw.setBounds(0, 0, mMonthDraw.getMinimumWidth(), mMonthDraw.getMinimumHeight());
		mMonth.setCompoundDrawables(mMonthDraw, null, null, null);
		mMonth.setClickable(false);
		new PoiQuery().execute(mQueryStrings);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
        	// This is called when the Home (Up) button is pressed in the action bar.
            // Create a simple intent that starts the hierarchical parent activity and
            // use NavUtils in the Support Package to ensure proper handling of Up.
	    	finish();
            return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
		}
		return false;
	}
	
	class PoiQuery extends AsyncTask<String, Cursor, Cursor> {
		Cursor mCursor;
		
		@Override
		protected Cursor doInBackground(String... types) {
			mQuery = new Query(getApplicationContext());
			try {
				mCursor = mQuery.getPOIViaId(types[0],types[1]);
			} catch (Exception e) {
				Log.e("ErpDetail", e.toString());
			} 
			return mCursor;
		}

		@Override
		protected void onPostExecute(Cursor result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
//				Toast.makeText(ErpDetailActivity.this, "成功获取数据",Toast.LENGTH_LONG).show();
				result.moveToFirst();
				TextView mTextAddress = (TextView)findViewById(R.id.text_view_address);
				TextView mTextTel = (TextView)findViewById(R.id.text_view_tele);
				TextView mTextMaster = (TextView)findViewById(R.id.text_view_master);
				TextView mTextTag = (TextView)findViewById(R.id.text_view_tag);
				TextView mTextInfo = (TextView)findViewById(R.id.text_view_info);
				RatingBar mRatingBar = (RatingBar)findViewById(R.id.rating_bar);
				mTextChart.setText(getResources().getStringArray(R.array.chart_times)[0]
						+getResources().getStringArray(R.array.chart_titles)[Integer.parseInt(mPoiType)]);
				mTextMaster.setText(result.getString(result.getColumnIndex(PoiDB.C_MASTER)));
				mTextAddress.setText(result.getString(result.getColumnIndex(PoiDB.C_ADDRESS)));
				mTextTel.setText(result.getString(result.getColumnIndex(PoiDB.C_TELE)));
				mTextTag.setText(result.getString(result.getColumnIndex(PoiDB.C_CLASSIFY_L)));
				mTextInfo.setText(result.getString(result.getColumnIndex(PoiDB.C_CLASSIFY_S)));
				mRatingBar.setNumStars(Integer.parseInt(result.getString(result.getColumnIndex(PoiDB.C_LEVEL))+1));
				mTags = result.getString(result.getColumnIndex(PoiDB.C_CLASSIFY_L)).split(";");
				drawChart(0,300,mBarChart,mTags);
				setTitle(result.getString(result.getColumnIndex(PoiDB.C_NAME)));
			} else {
				Toast.makeText(ErpDetailActivity.this, "获取数据失败",
						Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private void drawChart(int type, float range,BarChart chart,String[]tags) {
		String[] mMonths = getResources().getStringArray(R.array.months);
		String[] mColor = {"#3498DB","#9B59B6","#2ECC71","#E67E22","#E74C3C"};
		final float mult = 200;
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        if (type == 2) {
        	for (int j = 0; j < tags.length; j++) {
            	ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
            	for (int i = 0; i < mMonths.length; i++) {
            		if (j==0) {
                        xVals.add(mMonths[i]);
					}
            		float val = (float) (Math.random() * mult)+range;
                 	yVals1.add(new BarEntry(val, i));
                }
            	BarDataSet set1 = new BarDataSet(yVals1, tags[j]);
            	set1.setColor(Color.parseColor(mColor[j]));
            	dataSets.add(set1);
        	}
			chart.setScaleMinima(2.6f, 2f);
			chart.centerViewPort(1, 3600);
		} else {
			xVals.add(getResources().getString(R.string.txt_tag));
			for (int j = 0; j < tags.length; j++) {
            	ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
                float val = (float) (Math.random() * mult)+range;
                yVals1.add(new BarEntry(val, 0));
            	BarDataSet set1 = new BarDataSet(yVals1, tags[j]);
            	set1.setColor(Color.parseColor(mColor[j]));
            	dataSets.add(set1);
        	}
			chart.setScaleMinima(1f, 1f);
			chart.fitScreen();
		}

        BarData data = new BarData(xVals, dataSets);
        chart.setDescription("");
        chart.animateXY(1500, 1500);
        chart.setData(data);
        chart.setHighlightEnabled(false);
        chart.invalidate();
    }
}
