package com.kenfeng.erp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ErpDetailActivity extends Activity {
	private String mPoiID;
	private String mPoiType;
	private Query mQuery;
	
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
		new PoiQuery().execute(mQueryStrings);
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
				Toast.makeText(ErpDetailActivity.this, "成功获取数据",Toast.LENGTH_LONG).show();
				result.moveToFirst();
				TextView mTextAddress = (TextView)findViewById(R.id.text_view_address);
				TextView mTextTel = (TextView)findViewById(R.id.text_view_tele);
				TextView mTextMaster = (TextView)findViewById(R.id.text_view_tele);
				mTextMaster.setText(result.getString(result.getColumnIndex(PoiDB.C_MASTER)));
				mTextAddress.setText(result.getString(result.getColumnIndex(PoiDB.C_ADDRESS)));
				mTextTel.setText(result.getString(result.getColumnIndex(PoiDB.C_TELE)));
				setTitle(result.getString(result.getColumnIndex(PoiDB.C_NAME)));
			} else {
				Toast.makeText(ErpDetailActivity.this, "获取数据失败",
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
