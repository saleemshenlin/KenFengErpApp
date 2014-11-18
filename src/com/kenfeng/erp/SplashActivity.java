package com.kenfeng.erp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class SplashActivity extends Activity {
	/* 
	 * ����һ������,������¼ʱ���Ƿ����״μ���,
	 */
	boolean isFirstIn = false;
	/**
	 * ����һ������,���ڱ�ʾ����HomeActivity
	 */
	private static final int Add_DATA = 1000;
	/**
	 * ����һ������,���ڱ�ʾ����GuideActivity
	 */
	private static final int GO_HOME = 1001;
	/**
	 * ����һ������,���ڱ�ʾ�ȴ�ʱ��,ֵΪ1.5s
	 */
	private static final long SPLASH_DELAY_MILLIS = 1500;
	/**
	 * ����һ������,���ڱ�ʾSharedPreferences������
	 */
	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	/**
	 * ����һ��Handler,������ʾ��ת����ͬ����
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Add_DATA:
				addData();
				break;
			case GO_HOME:
				goHome();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		init();
	}

	private void init() {
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);
		isFirstIn = preferences.getBoolean("isFirstIn", true);
		if (!isFirstIn) {
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		} else {
			mHandler.sendEmptyMessageDelayed(Add_DATA, SPLASH_DELAY_MILLIS);
		}
	}
	
	/**
	 * ������ת����HomeActivity
	 */
	private void goHome() {
		Intent mIntent = new Intent(SplashActivity.this, DrawerActivity.class);
		SplashActivity.this.startActivity(mIntent);
		SplashActivity.this.finish();
		SplashActivity.this.overridePendingTransition(
				R.anim.anim_in_right2left, R.anim.anim_out_left2right);
	}

	/**
	 * ������תj����GuideActivity
	 */
	private void addData() {
		new InitDataBaseData().execute();
	}
	
	/**
	 * ��InitDataBaseData<br>
	 * �������״μ���ʱ����FileIO.getDateFromXML()<br>
	 * ���ö��̵߳������ݡ������n�����ڣ�<br>
	 * ��������ʱ��ȵ����ͼ����ʱ�䳤,�ڽ���֮�����init();
	 */
	class InitDataBaseData extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Toast.makeText(SplashActivity.this, result, Toast.LENGTH_SHORT)
					.show();
			setGuided();
			goHome();
		}

		@Override
		protected String doInBackground(String... params) {
			getDateFromJson();
			return "�������";
		}

	}
	
	/**
	 * ���ڸ���SharedPreferences���´����������ٴ�����
	 */
	private void setGuided() {
		SharedPreferences mSharedPreferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor mEditor = mSharedPreferences.edit();
		mEditor.putBoolean("isFirstIn", false);
		mEditor.commit();
	}

	public void getDateFromJson() {
		// TODO Auto-generated method stub
		FileIO mFileIO = new FileIO(SplashActivity.this);
		mFileIO.JsonToDB();
		mFileIO.initSupplierData();
	}
	
}