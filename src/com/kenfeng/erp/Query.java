package com.kenfeng.erp;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;

public class Query {
	private static final String TAG_STRING = "Query";
	private ErpProvidor mErpProvidor;
	private StorageProvidor mStorageProvidor;
	private PoiDB mPoiDB;
	private Context mContext;
	
	public Query(Context context){
		this.mContext = context;
		this.mPoiDB = new PoiDB(context);
		this.mErpProvidor = new ErpProvidor(this.mPoiDB);
		this.mStorageProvidor = new StorageProvidor(this.mPoiDB);
	}
	
	
	public GraphicsLayer getPOIGraphicsViaType(int type) {
		GraphicsLayer mGraphicsLayer = new GraphicsLayer();
		Cursor mCursor = null;
		String mTypeString = null;
		try {
			switch (type) {
			case 0:
				mTypeString = "supplier";
				mCursor = mErpProvidor.query(ErpProvidor.CONTENT_URI, null, PoiDB.C_CLASSIFY + " = '"+type+"'", null, null);
				break;
			case 1:
				mTypeString = "dealer";
				mCursor = mErpProvidor.query(ErpProvidor.CONTENT_URI, null, PoiDB.C_CLASSIFY + " = '"+type+"'", null, null);
				break;
			case 2:
				mTypeString = "storage";
				mCursor = mStorageProvidor.query(StorageProvidor.CONTENT_URI, null, null, null, null);
				break;
			default:
				mTypeString = "supplier";
				break;
			}
			Map<String, Object> mMap = new HashMap<String, Object>();
			mCursor.moveToFirst();
			while (mCursor.moveToNext()) {
				String WKT = mCursor.getString(mCursor
						.getColumnIndex(PoiDB.C_GEOMETRY));
				String ID = mCursor.getString(mCursor
						.getColumnIndex(PoiDB.C_ID));
				String NAME = mCursor.getString(mCursor
						.getColumnIndex(PoiDB.C_NAME));
				String TYPE = type+"";
				mMap.put("NAME", NAME);
				mMap.put("ID", ID);
				mMap.put("TYPE", TYPE);
				int imgId = mContext.getResources().getIdentifier("ic_" + mTypeString,
						"drawable", "com.kenfeng.erp");
				Drawable mDrawable = mContext.getResources().getDrawable(imgId);
				PictureMarkerSymbol mPictureMarkerSymbol = new PictureMarkerSymbol(
						mDrawable);
				Point mPoint = (Point) Query.wkt2Geometry(WKT);
				Log.d("Query", mPoint.getX() + ";" + mPoint.getY());
				Graphic mGraphic = new Graphic(mPoint, mPictureMarkerSymbol,
						mMap, 0);
				mGraphicsLayer.addGraphic(mGraphic);
			}
		} catch (Exception e) {
			Log.e(TAG_STRING, "getGraphicsViaType:"+e.toString());
		}finally{
			if (mCursor.isClosed()) {
				mCursor.close();
			}
		}
		return mGraphicsLayer;
	}
	
	public GraphicsLayer getPOIsAround(Graphic poi){
		GraphicsLayer mGraphicsLayer = new GraphicsLayer();
		Cursor mCursor = null;
		Map<String, Object> mMap = new HashMap<String, Object>();
		int mType = Integer.parseInt((String)poi.getAttributeValue("TYPE"));
		try {
			switch (mType) {
			case 0:
				mCursor = mStorageProvidor.query(StorageProvidor.CONTENT_URI, null, null, null, null);
				mCursor.moveToFirst();
				while (mCursor.moveToNext()) {
					String WKT = mCursor.getString(mCursor
							.getColumnIndex(PoiDB.C_GEOMETRY));
					String ID = mCursor.getString(mCursor
							.getColumnIndex(PoiDB.C_ID));
					String NAME = mCursor.getString(mCursor
							.getColumnIndex(PoiDB.C_NAME));
					String[] mSuppliers = mCursor.getString(mCursor.getColumnIndex(PoiDB.C_SUPPLIERID)).split(";");
					for (int i = 0; i < mSuppliers.length; i++) {
						if (Integer.parseInt(mSuppliers[i]) == Integer.parseInt((String)poi.getAttributeValue("ID"))) {
							String TYPE = "2";
							mMap.put("NAME", NAME);
							mMap.put("ID", ID);
							mMap.put("TYPE", TYPE);
							Drawable mDrawable = mContext.getResources().getDrawable(R.drawable.ic_storage);
							PictureMarkerSymbol mPictureMarkerSymbol = new PictureMarkerSymbol(
									mDrawable);
							Point mPoint = (Point) Query.wkt2Geometry(WKT);
							Log.d("Query", mPoint.getX() + ";" + mPoint.getY());
							Graphic mGraphic = new Graphic(mPoint, mPictureMarkerSymbol,
									mMap, 0);
							mGraphicsLayer.addGraphic(mGraphic);
						}
					}
				}
				break;
			case 1:
				mCursor = mStorageProvidor.query(StorageProvidor.CONTENT_URI, null, null, null, null);
				mCursor.moveToFirst();
				while (mCursor.moveToNext()) {
					String WKT = mCursor.getString(mCursor
							.getColumnIndex(PoiDB.C_GEOMETRY));
					String ID = mCursor.getString(mCursor
							.getColumnIndex(PoiDB.C_ID));
					String NAME = mCursor.getString(mCursor
							.getColumnIndex(PoiDB.C_NAME));
					String[] mDealers = mCursor.getString(mCursor.getColumnIndex(PoiDB.C_DEALERID)).split(";");
					for (int i = 0; i < mDealers.length; i++) {
						if (Integer.parseInt(mDealers[i]) == Integer.parseInt((String)poi.getAttributeValue("ID"))) {
							String TYPE = "2";
							mMap.put("NAME", NAME);
							mMap.put("ID", ID);
							mMap.put("TYPE", TYPE);
							Drawable mDrawable = mContext.getResources().getDrawable(R.drawable.ic_storage);
							PictureMarkerSymbol mPictureMarkerSymbol = new PictureMarkerSymbol(
									mDrawable);
							Point mPoint = (Point) Query.wkt2Geometry(WKT);
							Log.d("Query", mPoint.getX() + ";" + mPoint.getY());
							Graphic mGraphic = new Graphic(mPoint, mPictureMarkerSymbol,
									mMap, 0);
							mGraphicsLayer.addGraphic(mGraphic);
						}
					}
				}
				break;
			case 2:
				mCursor = getPOIViaId((String)poi.getAttributeValue("ID"),"2");
				if (mCursor.moveToFirst()) {
					String[] mSuppliers	= mCursor.getString(mCursor.getColumnIndex(PoiDB.C_SUPPLIERID)).split(";");
					String[] mDealers = mCursor.getString(mCursor.getColumnIndex(PoiDB.C_DEALERID)).split(";");
					for (int i = 0; i < mSuppliers.length; i++) {
						Cursor mSupplierCursor = getPOIViaId(mSuppliers[i], "0");
						if (mSupplierCursor.moveToFirst()) {
							String WKT = mSupplierCursor.getString(mSupplierCursor
									.getColumnIndex(PoiDB.C_GEOMETRY));
							String ID = mSupplierCursor.getString(mSupplierCursor
									.getColumnIndex(PoiDB.C_ID));
							String NAME = mSupplierCursor.getString(mSupplierCursor
									.getColumnIndex(PoiDB.C_NAME));
							String TYPE = "0";
							mMap.put("NAME", NAME);
							mMap.put("ID", ID);
							mMap.put("TYPE", TYPE);
							Drawable mDrawable = mContext.getResources().getDrawable(R.drawable.ic_supplier);
							PictureMarkerSymbol mPictureMarkerSymbol = new PictureMarkerSymbol(
									mDrawable);
							Point mPoint = (Point) Query.wkt2Geometry(WKT);
							Log.d("Query", mPoint.getX() + ";" + mPoint.getY());
							Graphic mGraphic = new Graphic(mPoint, mPictureMarkerSymbol,
									mMap, 0);
							mGraphicsLayer.addGraphic(mGraphic);
							mSupplierCursor.isClosed();
						}
					}
					for (int i = 0; i < mDealers.length; i++) {
						Cursor mDealerCursor = getPOIViaId(mDealers[i], "1");
						if (mDealerCursor.moveToFirst()) {
							String WKT = mDealerCursor.getString(mDealerCursor
									.getColumnIndex(PoiDB.C_GEOMETRY));
							String ID = mDealerCursor.getString(mDealerCursor
									.getColumnIndex(PoiDB.C_ID));
							String NAME = mDealerCursor.getString(mDealerCursor
									.getColumnIndex(PoiDB.C_NAME));
							String TYPE = "1";
							mMap.put("NAME", NAME);
							mMap.put("ID", ID);
							mMap.put("TYPE", TYPE);
							Drawable mDrawable = mContext.getResources().getDrawable(R.drawable.ic_dealer);
							PictureMarkerSymbol mPictureMarkerSymbol = new PictureMarkerSymbol(
									mDrawable);
							Point mPoint = (Point) Query.wkt2Geometry(WKT);
							Log.d("Query", mPoint.getX() + ";" + mPoint.getY());
							Graphic mGraphic = new Graphic(mPoint, mPictureMarkerSymbol,
									mMap, 0);
							mGraphicsLayer.addGraphic(mGraphic);
							mDealerCursor.isClosed();
						}
					}
				}						
				break;
			default:
				break;
			}
			
		} catch (Exception e) {
			Log.e("getPOIsAround", e.getMessage().toString());
		}finally{
			if (!mCursor.isClosed()) {
				mCursor.close();
			}
		}
		return mGraphicsLayer;
	}
	
	public Cursor getPOIViaId(String id ,String type){
		Cursor mCursor = null;
		try {
			if(Integer.parseInt(type) == 2){
				final Uri queryUri = Uri.parse(StorageProvidor.CONTENT_URI.toString() + "/"
						 + id);
				mCursor = mStorageProvidor.query(queryUri, null, null, null, null);
			}
			else{
				final Uri queryUri = Uri.parse(ErpProvidor.CONTENT_URI.toString() + "/"
						 + id);
				mCursor =  mErpProvidor.query(queryUri, null, null, null, null);
			}
		} catch (Exception e) {
			Log.e(TAG_STRING, "getPOIViaId:"+e.toString());
		} 
		return mCursor;
	}
	
	public static Geometry wkt2Geometry(String wkt) {
		Geometry geo = null;
		if (wkt == null || wkt == "") {
			return null;
		}
		String headStr = wkt.substring(0, wkt.indexOf("("));
		String temp = wkt.substring(wkt.indexOf("(") + 1, wkt.lastIndexOf(")"));
		if (headStr.equals("POINT")) {
			String[] values = temp.split(" ");
			geo = new Point(Double.valueOf(values[0]),
					Double.valueOf(values[1]));
		} else if (headStr.equals("POLYLINE") || headStr.equals("Polygon")) {
			geo = parseWKT(temp, headStr);
		} else if (headStr.equals("Envelope")) {
			String[] extents = temp.split(",");
			geo = new Envelope(Double.valueOf(extents[0]),
					Double.valueOf(extents[1]), Double.valueOf(extents[2]),
					Double.valueOf(extents[3]));
		} else if (headStr.equals("MultiPoint")) {
		} else {
			return null;
		}
		return geo;
	}
	
	private static Geometry parseWKT(String multipath, String type) {
		String subMultipath = multipath.substring(1, multipath.length() - 1);
		String[] paths;
		if (subMultipath.indexOf("),(") >= 0) {
			paths = subMultipath.split("),(");// 多个几何对象的字符串
		} else {
			paths = new String[] { subMultipath };
		}
		Point startPoint = null;
		MultiPath path = null;
		if (type.equals("POLYLINE")) {
			path = new Polyline();
		} else {
			path = new Polygon();
		}
		for (int i = 0; i < paths.length; i++) {
			String[] points = paths[i].split(",");
			startPoint = null;
			for (int j = 0; j < points.length; j++) {
				String[] pointStr = points[j].split(" ");
				if (startPoint == null) {
					startPoint = new Point(Double.valueOf(pointStr[0]),
							Double.valueOf(pointStr[1]));
					path.startPath(startPoint);
				} else {
					path.lineTo(new Point(Double.valueOf(pointStr[0]), Double
							.valueOf(pointStr[1])));
				}
			}
		}
		return path;
	}
	
}
