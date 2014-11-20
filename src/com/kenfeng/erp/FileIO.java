package com.kenfeng.erp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;

public class FileIO {
	private Context mContext;
	private PoiDB mPoiDB;
	
	public FileIO(Context context){
		this.mContext = context;
		this.mPoiDB = new PoiDB(mContext);
	}

	
	public void JsonToDB() {
		AssetManager mAssetManager = mContext .getAssets();
		String[] files = null;
		try {
			files = mAssetManager.list("");
			for (String filename : files) {

				InputStream in = null;
				OutputStream out = null;
				if (filename.equals("erp.json")
					||filename.equals("storage.json")
					||filename.equals("supplier.json")
					||filename.equals("dealer.json")) {
						in = mAssetManager.open(filename);
						File outFile = null;
						outFile = new File(mContext.getExternalFilesDir("data"),
								filename);
						if (!outFile.exists()) {
							out = new FileOutputStream(outFile);
							copyFile(in, out);
							in.close();
							in = null;
							out.flush();
							out.close();
							out = null;
						}
				}
			}
		} catch (IOException e) {
			Log.e("FileIO", e.toString());
		}
	}
	/*
	 * 经销商、供应商
	 */
	public void initPoiData() {
		File poiJson = new File(mContext.getExternalFilesDir("data"), "erp.json");
		JsonFactory mJsonFactory = new JsonFactory();
		try {
			System.out.println("Done!");
			JsonParser mjJsonParser = mJsonFactory.createJsonParser(poiJson);
			mjJsonParser.nextToken();
			FeatureSet mFeatureSet = FeatureSet.fromJson(mjJsonParser);
			Graphic[] mGraphics = mFeatureSet.getGraphics();
			for (Graphic mGraphic : mGraphics) {
				ContentValues values = new ContentValues();
				String mFID = (String) mGraphic.getAttributeValue("FID").toString();
				values.put(PoiDB.C_FID, mFID);
				String mDIRECTION = (String) mGraphic.getAttributeValue("DIRECTION");
				values.put(PoiDB.C_DIRECTION, mDIRECTION);
				String mCLASSIFY = (String) mGraphic.getAttributeValue("CLASSIFY").toString();
				values.put(PoiDB.C_CLASSIFY, mCLASSIFY);
				String mID = (String) mGraphic.getAttributeValue("ID").toString();
				values.put(PoiDB.C_ID, mID);
				String mPRODUCTID = (String) mGraphic.getAttributeValue("PRODUCT_ID");
				values.put(PoiDB.C_PRODUCT_ID, mPRODUCTID);
				String mNAME = (String) mGraphic.getAttributeValue("NAME");
				values.put(PoiDB.C_NAME, mNAME);
				String mLEVEL = (String) mGraphic.getAttributeValue("LEVEL").toString();
				values.put(PoiDB.C_LEVEL, mLEVEL);
				String mMASTER = (String) mGraphic.getAttributeValue("MASTER");
				values.put(PoiDB.C_MASTER, mMASTER);
				String mTELE = (String) mGraphic.getAttributeValue("TELE").toString();
				values.put(PoiDB.C_TELE, mTELE);
				String mADDRESS = (String) mGraphic.getAttributeValue("ADDRESS");
				values.put(PoiDB.C_ADDRESS, mADDRESS);
				String mCLASSIFY_S = (String) mGraphic.getAttributeValue("CLASSIFY_S");
				values.put(PoiDB.C_CLASSIFY_S, mCLASSIFY_S);
				String mCLASSIFY_L = (String) mGraphic.getAttributeValue("CLASSIFY_L");
				values.put(PoiDB.C_CLASSIFY_L, mCLASSIFY_L);
				Geometry mGeometry = mGraphic.getGeometry();
				String mShape = GeometryToWKT(mGeometry);
				values.put(PoiDB.C_GEOMETRY, mShape);
				mPoiDB.insertOrIgnore(values,PoiDB.TABLE_ERP);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/*
	 * 仓储
	 */
	public void initStorageData() {
		File poiJson = new File(mContext.getExternalFilesDir("data"), "storage.json");
		JsonFactory mJsonFactory = new JsonFactory();
		try {
			System.out.println("Done!");
			JsonParser mjJsonParser = mJsonFactory.createJsonParser(poiJson);
			mjJsonParser.nextToken();
			FeatureSet mFeatureSet = FeatureSet.fromJson(mjJsonParser);
			Graphic[] mGraphics = mFeatureSet.getGraphics();
			for (Graphic mGraphic : mGraphics) {
				ContentValues values = new ContentValues();
				String mFID = (String) mGraphic.getAttributeValue("FID").toString();
				values.put(PoiDB.C_FID, mFID);
				String mSUPPLIERID = (String) mGraphic.getAttributeValue("SUPPLIERID");
				values.put(PoiDB.C_SUPPLIERID, mSUPPLIERID);
				String mDEALERID = (String) mGraphic.getAttributeValue("DEALERID");
				values.put(PoiDB.C_DEALERID, mDEALERID);
				String mID = (String) mGraphic.getAttributeValue("ID").toString();
				values.put(PoiDB.C_ID, mID);
				String mNAME = (String) mGraphic.getAttributeValue("NAME");
				values.put(PoiDB.C_NAME, mNAME);
				String mLEVEL = (String) mGraphic.getAttributeValue("LEVEL").toString();
				values.put(PoiDB.C_LEVEL, mLEVEL);
				String mMASTER = (String) mGraphic.getAttributeValue("MASTER");
				values.put(PoiDB.C_MASTER, mMASTER);
				String mTELE = (String) mGraphic.getAttributeValue("TELE").toString();
				values.put(PoiDB.C_TELE, mTELE);
				String mADDRESS = (String) mGraphic.getAttributeValue("ADDRESS");
				values.put(PoiDB.C_ADDRESS, mADDRESS);
				String mCLASSIFY_S = (String) mGraphic.getAttributeValue("CLASSIFY_S");
				values.put(PoiDB.C_CLASSIFY_S, mCLASSIFY_S);
				String mCLASSIFY_L = (String) mGraphic.getAttributeValue("CLASSIFY_L");
				values.put(PoiDB.C_CLASSIFY_L, mCLASSIFY_L);
				Geometry mGeometry = mGraphic.getGeometry();
				String mShape = GeometryToWKT(mGeometry);
				values.put(PoiDB.C_GEOMETRY, mShape);
				mPoiDB.insertOrIgnore(values,PoiDB.TABLE_STORAGE);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int intRead;
		while ((intRead = in.read(buffer)) != -1) {
			out.write(buffer, 0, intRead);
		}
	}
	
	public static String GeometryToWKT(Geometry geometry) {
		if (geometry == null) {
			return null;
		}
		String geoStr = "";
		Geometry.Type type = geometry.getType();
		if ("POINT".equals(type.name())) {
			Point pt = (Point) geometry;
			geoStr = type.name() + "(" + pt.getX() + " " + pt.getY() + ")";
		} else if ("Polygon".equals(type.name())
				|| "POLYLINE".equals(type.name())) {
			MultiPath pg = (MultiPath) geometry;
			geoStr = type.name() + "(" + "";
			int pathSize = pg.getPathCount();
			for (int j = 0; j < pathSize; j++) {
				String temp = "(";
				int size = pg.getPathSize(j);
				for (int i = 0; i < size; i++) {
					Point pt = pg.getPoint(i);
					temp += pt.getX() + " " + pt.getY() + ",";
				}
				temp = temp.substring(0, temp.length() - 1) + ")";
				geoStr += temp + ",";
			}
			geoStr = geoStr.substring(0, geoStr.length() - 1) + ")";
		} else if ("Envelope".equals(type.name())) {
			Envelope env = (Envelope) geometry;
			geoStr = type.name() + "(" + env.getXMin() + "," + env.getYMin()
					+ "," + env.getXMax() + "," + env.getYMax() + ")";
		} else if ("MultiPoint".equals(type.name())) {
		} else {
			geoStr = null;
		}
		return geoStr;
	}
}
