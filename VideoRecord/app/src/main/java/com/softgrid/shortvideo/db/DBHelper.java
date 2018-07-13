package com.softgrid.shortvideo.db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.softgrid.shortvideo.model.CacheModel;
import com.softgrid.shortvideo.model.FavModel;
import com.softgrid.shortvideo.model.HisModel;
import com.softgrid.shortvideo.tool.DeviceInfoTool;

public class DBHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;
	private final String ROOT_PATH = ".meteorFM_DbData";
	
	private final static String DATABASE_NAME = "fm_database";
	private final String DB_FILE_NAME = "fm_database.db";
	private String root_path;
	private SQLiteDatabase db;
	private File dbFile = null;
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
		if (sdCheck()) {
			//所有数据放在一起
			root_path = Environment.getExternalStorageDirectory() + "/" +
					ROOT_PATH + "/" + DeviceInfoTool.getPageName(context) + "/database";
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			createTable(db);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	/**
	 * 检查sd卡
	 * 
	 * */
	private boolean sdCheck() {
		try {
			String sdStatus = Environment.getExternalStorageState();// 获取SD卡状态
			// SD卡识别正常
			if (sdStatus.equals(Environment.MEDIA_MOUNTED)) {
				return true;
			}
			// SD卡被移除或者未插入
			else if (sdStatus.equals(Environment.MEDIA_REMOVED)
					|| sdStatus.equals(Environment.MEDIA_BAD_REMOVAL)) {
				return false;
			}
			// 正在检查SD卡
			else if (sdStatus.equals(Environment.MEDIA_CHECKING)) {
				Thread.sleep(1000);
				return this.sdCheck();
			}
			// sd卡无法被识别
			else {
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	
	/**打开sd卡上的数据库*/
	private SQLiteDatabase openDBInSd(){
		SQLiteDatabase db = null;
		if (!sdCheck()) {
			db = this.getWritableDatabase();
		}
		else{
			if (dbFile == null) {
				File dbRoot = new File(root_path);
				if (!dbRoot.exists()) {
					dbRoot.mkdirs();
				}
				dbFile = new File(root_path + "/" + DB_FILE_NAME);
				if (!dbFile.exists()) {
					try {
						dbFile.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
						createTable(db);
					} catch (Exception e) {
						// TODO: handle exception
						db = this.getWritableDatabase();
					}
				}
			}
			if (db == null) {
				try {
					db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
				} catch (Exception e) {
					// TODO: handle exception
					db = this.getWritableDatabase();
				}
			}
		}
		return db;
	}
	

	
	/**
	 * 打开数据库,并开启事务
	 * */
	public void openDB(){
		
		if (db != null) {
			if (db.isOpen()) {
				db.close();
			}
			db = null;
		}
		db = openDBInSd();
		db.beginTransaction();
	}
	
	/**
	 * 提交事务，关闭数据库
	 * 
	 * @param transactionSuccessful 是否提交事务
	 * 
	 * */
	public void closeDB(boolean transactionSuccessful){
		if (db != null) {
			if (db.inTransaction()) {
				if (transactionSuccessful) {
					db.setTransactionSuccessful();
				}
				db.endTransaction();
			}
			if (db.isOpen()) {
				db.close();
			}
			db = null;
		}
	}

	/**建立数据库表*/
	private void createTable(SQLiteDatabase db){

        String createCacheTable =
                "CREATE TABLE " + CacheItems.TABLE_NAME + " ("
                        + CacheItems._ID + " integer primary key autoincrement,"
                        + CacheItems.CACHE_ACT + " TEXT NOT NULL,"
                        + CacheItems.CACHE_PARAMS + " TEXT NOT NULL,"
                        + CacheItems.CACHE_CONTENT + " TEXT"
                        +")";

        String createFavTable =
                "CREATE TABLE " + FavItems.TABLE_NAME + " ("
                        + FavItems._ID + " integer primary key autoincrement,"
                        + FavItems.FAV_ID + " TEXT NOT NULL,"
                        + FavItems.FAV_TYPE + " integer,"
                        + FavItems.FAV_CONTENT + " TEXT,"
                        + FavItems.FAV_CREATEDTIME + " TEXT,"
                        + FavItems.FAV_ISUPDATE + " integer,"
                        + FavItems.FAV_UPDATETIME + " TEXT"
                        +")";

        String createHisTable =
                "CREATE TABLE " + HisItems.TABLE_NAME + " ("
                        + HisItems._ID + " integer primary key autoincrement,"
                        + HisItems.HIS_ID + " TEXT NOT NULL,"
                        + HisItems.HIS_TYPE + " integer,"
                        + HisItems.HIS_PLAYEDTIME + " integer,"
                        + HisItems.HIS_CONTENT + " TEXT,"
                        + HisItems.HIS_SUBCONTENT + " TEXT,"
                        + HisItems.HIS_CREATEDTIME + " TEXT,"
                        + HisItems.HIS_ISUPDATE + " integer,"
                        + HisItems.HIS_UPDATETIME + " TEXT"
                        +")";

        db.execSQL(createCacheTable);
        db.execSQL(createFavTable);
        db.execSQL(createHisTable);

	}


	/**
	 * 缓存实体是否已经存在
	 * */
	public CacheModel cacheIsExist(CacheModel cacheModel, boolean nowSave){

		CacheModel model = null;

		if (db == null || !db.isOpen()) {
			db = openDBInSd();
		}

		Cursor cursor = null;
		try {
			String sql = "select * from " + CacheItems.TABLE_NAME +
					" where " + CacheItems.CACHE_ACT +"='" + cacheModel.getAct() + "' and " +
					CacheItems.CACHE_PARAMS + "='" + cacheModel.getParams() + "'";
			cursor = db.rawQuery(sql, null);
			if (cursor == null) {
				return model;
			}
			if (cursor.getCount() == 0) {
				return model;
			}
			if (cursor.moveToFirst()) {
				model = new CacheModel();
				model.setId(cursor.getInt(cursor.getColumnIndex(CacheItems._ID)));
				model.setAct(cursor.getString(cursor.getColumnIndex(CacheItems.CACHE_ACT)));
				model.setParams(cursor.getString(cursor.getColumnIndex(CacheItems.CACHE_PARAMS)));
				model.setContent(cursor.getString(cursor.getColumnIndex(CacheItems.CACHE_CONTENT)));
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		if (nowSave) {
			if (db != null && db.isOpen()) {
				db.close();
			}
			db = null;
		}
		return model;
	}

	/**
	 * 保存一条缓存实体
	 * */
	public long saveCache(CacheModel cacheModel, boolean nowSave){

		if (db == null || !db.isOpen()) {
			db = openDBInSd();
		}

		long id = -1;

		try {
			ContentValues values = new ContentValues();
			values.put(CacheItems.CACHE_ACT, cacheModel.getAct());
			values.put(CacheItems.CACHE_PARAMS, cacheModel.getParams());
			values.put(CacheItems.CACHE_CONTENT, cacheModel.getContent());
			id = db.insert(CacheItems.TABLE_NAME, null, values);

		}catch (Exception e){
			e.printStackTrace();
		}

		if (nowSave) {
			if (db != null && db.isOpen()) {
				db.close();
			}
			db = null;
		}

		return id;
	}

	/**
	 * 删除一条缓存实体
	 * */
	public void deleteCache(CacheModel cacheModel, boolean nowSave){

		if (db == null || !db.isOpen()) {
			db = openDBInSd();
		}

		if (cacheModel.getId() > 0){
			db.delete(CacheItems.TABLE_NAME,
					CacheItems._ID + "=?",
					new String[]{String.valueOf(cacheModel.getId())});
		}
		else {
			db.delete(CacheItems.TABLE_NAME,
					CacheItems.CACHE_ACT + "=? and " + CacheItems.CACHE_PARAMS + "=?",
					new String[]{cacheModel.getAct(),cacheModel.getParams()});
		}

		if (nowSave) {
			if (db != null && db.isOpen()) {
				db.close();
			}
			db = null;
		}
	}

	/**
	 * 删除所有缓存实体
	 * */
	public void deleteAllCache(boolean nowSave){

		if (db == null || !db.isOpen()) {
			db = openDBInSd();
		}

		db.delete(CacheItems.TABLE_NAME, null, null);

		if (nowSave) {
			if (db != null && db.isOpen()) {
				db.close();
			}
			db = null;
		}
	}

	/**
	 * 收藏实体是否已经存在
	 * */
	public FavModel favIsExist(FavModel favModel, boolean nowSave){

		FavModel model = null;

		if (db == null || !db.isOpen()) {
			db = openDBInSd();
		}

		Cursor cursor = null;
		try {
			String sql = "select * from " + FavItems.TABLE_NAME +
					" where " + FavItems.FAV_ID +"='" + favModel.getFavId() + "' and " +
					FavItems.FAV_TYPE + "='" + favModel.getType() + "'";
			cursor = db.rawQuery(sql, null);
			if (cursor == null) {
				return model;
			}
			if (cursor.getCount() == 0) {
				return model;
			}
			if (cursor.moveToFirst()) {
				model = new FavModel();
				model.setId(cursor.getInt(cursor.getColumnIndex(FavItems._ID)));
				model.setFavId(cursor.getString(cursor.getColumnIndex(FavItems.FAV_ID)));
				model.setType(cursor.getInt(cursor.getColumnIndex(FavItems.FAV_TYPE)));
				model.setFavContent(cursor.getString(cursor.getColumnIndex(FavItems.FAV_CONTENT)));
				model.setFavCreatedTime(cursor.getString(cursor.getColumnIndex(FavItems.FAV_CREATEDTIME)));
				model.setFavUpdated(cursor.getInt(cursor.getColumnIndex(FavItems.FAV_ISUPDATE)));
				model.setFavUpdatedTime(cursor.getString(cursor.getColumnIndex(FavItems.FAV_UPDATETIME)));
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		if (nowSave) {
			if (db != null && db.isOpen()) {
				db.close();
			}
			db = null;
		}
		return model;
	}

	/**
	 * 保存一条收藏实体
	 * */
	public long saveFav(FavModel favModel, boolean nowSave){
		if (db == null || !db.isOpen()) {
			db = openDBInSd();
		}

		long id = -1;

		try {
			ContentValues values = new ContentValues();
			values.put(FavItems.FAV_ID, favModel.getFavId());
			values.put(FavItems.FAV_TYPE, favModel.getType());
			values.put(FavItems.FAV_CONTENT, favModel.getFavContent());
			values.put(FavItems.FAV_CREATEDTIME, favModel.getFavCreatedTime());
			values.put(FavItems.FAV_ISUPDATE, favModel.getFavUpdated());
			values.put(FavItems.FAV_UPDATETIME, favModel.getFavUpdatedTime());
			id = db.insert(FavItems.TABLE_NAME, null, values);

		}catch (Exception e){
			e.printStackTrace();
		}

		if (nowSave) {
			if (db != null && db.isOpen()) {
				db.close();
			}
			db = null;
		}

		return id;
	}

	/**
	 * 删除一条收藏实体
	 * */
	public void deleteFav(FavModel favModel, boolean nowSave){
		if (db == null || !db.isOpen()) {
			db = openDBInSd();
		}

		if (favModel.getId() > 0){
			db.delete(FavItems.TABLE_NAME,
					FavItems._ID + "=?",
					new String[]{String.valueOf(favModel.getId())});
		}
		else {
			db.delete(FavItems.TABLE_NAME,
					FavItems.FAV_ID + "=? and " + FavItems.FAV_TYPE + "=?",
					new String[]{favModel.getFavId(),String.valueOf(favModel.getType())});
		}

		if (nowSave) {
			if (db != null && db.isOpen()) {
				db.close();
			}
			db = null;
		}
	}

	/**
	 * 删除所有收藏实体
	 * */
	public void deleteAllFav(boolean nowSave){
		if (db == null || !db.isOpen()) {
			db = openDBInSd();
		}

		db.delete(FavItems.TABLE_NAME, null, null);

		if (nowSave) {
			if (db != null && db.isOpen()) {
				db.close();
			}
			db = null;
		}
	}

	public ArrayList<FavModel> getFavByType(int type, boolean nowSave){

		ArrayList<FavModel> dataList = new ArrayList<>();

		if (db == null || !db.isOpen()) {
			db = openDBInSd();
		}

		Cursor cursor = null;
		try {
			String sql = "select * from " + FavItems.TABLE_NAME +
					" where " + FavItems.FAV_TYPE + "='" + type + "'";
			cursor = db.rawQuery(sql, null);
			if (cursor == null) {
				return dataList;
			}
			if (cursor.getCount() == 0) {
				return dataList;
			}

			cursor.moveToLast();
			boolean hasMore = true;
			while (hasMore) {

				FavModel model = new FavModel();
				model.setId(cursor.getInt(cursor.getColumnIndex(FavItems._ID)));
				model.setFavId(cursor.getString(cursor.getColumnIndex(FavItems.FAV_ID)));
				model.setType(cursor.getInt(cursor.getColumnIndex(FavItems.FAV_TYPE)));
				model.setFavContent(cursor.getString(cursor.getColumnIndex(FavItems.FAV_CONTENT)));
				model.setFavCreatedTime(cursor.getString(cursor.getColumnIndex(FavItems.FAV_CREATEDTIME)));
				model.setFavUpdated(cursor.getInt(cursor.getColumnIndex(FavItems.FAV_ISUPDATE)));
				model.setFavUpdatedTime(cursor.getString(cursor.getColumnIndex(FavItems.FAV_UPDATETIME)));
				dataList.add(model);
				hasMore = cursor.moveToPrevious();
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		if (nowSave) {
			if (db != null && db.isOpen()) {
				db.close();
			}
			db = null;
		}
		return dataList;
	}

	/**
	 * 历史实体是否已经存在
	 * */
	public HisModel hisIsExist(HisModel hisModel, boolean nowSave){
		HisModel model = null;

		if (db == null || !db.isOpen()) {
			db = openDBInSd();
		}

		Cursor cursor = null;
		try {
			String sql = "select * from " + HisItems.TABLE_NAME +
					" where " + HisItems.HIS_ID +"='" + hisModel.getHisId() + "' and " +
					HisItems.HIS_TYPE + "='" + hisModel.getHisType() + "'";
			cursor = db.rawQuery(sql, null);
			if (cursor == null) {
				return model;
			}
			if (cursor.getCount() == 0) {
				return model;
			}
			if (cursor.moveToFirst()) {
				model = new HisModel();
				model.setId(cursor.getInt(cursor.getColumnIndex(HisItems._ID)));
				model.setHisId(cursor.getString(cursor.getColumnIndex(HisItems.HIS_ID)));
				model.setHisType(cursor.getInt(cursor.getColumnIndex(HisItems.HIS_TYPE)));
				model.setHisPlayedTime(cursor.getInt(cursor.getColumnIndex(HisItems.HIS_PLAYEDTIME)));
				model.setHisContent(cursor.getString(cursor.getColumnIndex(HisItems.HIS_CONTENT)));
				model.setHisSubContent(cursor.getString(cursor.getColumnIndex(HisItems.HIS_SUBCONTENT)));
				model.setHisCreatedTime(cursor.getString(cursor.getColumnIndex(HisItems.HIS_CREATEDTIME)));
				model.setHisUpdated(cursor.getInt(cursor.getColumnIndex(HisItems.HIS_ISUPDATE)));
				model.setHisUpdatedTime(cursor.getString(cursor.getColumnIndex(HisItems.HIS_UPDATETIME)));
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		if (nowSave) {
			if (db != null && db.isOpen()) {
				db.close();
			}
			db = null;
		}
		return model;
	}

	/**
	 * 保存一条历史实体
	 * */
	public long saveHis(HisModel hisModel, boolean nowSave){
		if (db == null || !db.isOpen()) {
			db = openDBInSd();
		}

		long id = -1;

		try {
			ContentValues values = new ContentValues();
			values.put(HisItems.HIS_ID, hisModel.getHisId());
			values.put(HisItems.HIS_TYPE, hisModel.getHisType());
			values.put(HisItems.HIS_PLAYEDTIME, hisModel.getHisPlayedTime());
			if (hisModel.getHisContent() != null){
				values.put(HisItems.HIS_CONTENT, hisModel.getHisContent());
			}
			values.put(HisItems.HIS_SUBCONTENT, hisModel.getHisSubContent());
			values.put(HisItems.HIS_CREATEDTIME, hisModel.getHisCreatedTime());
			values.put(HisItems.HIS_ISUPDATE, hisModel.getHisUpdated());
			values.put(HisItems.HIS_UPDATETIME, hisModel.getHisUpdatedTime());

			id = db.insert(HisItems.TABLE_NAME, null, values);

		}catch (Exception e){
			e.printStackTrace();
		}

		if (nowSave) {
			if (db != null && db.isOpen()) {
				db.close();
			}
			db = null;
		}

		return id;
	}

	/**
	 * 删除一条历史实体
	 * */
	public void deleteHis(HisModel hisModel, boolean nowSave){
		if (db == null || !db.isOpen()) {
			db = openDBInSd();
		}

		if (hisModel.getId() > 0){
			db.delete(HisItems.TABLE_NAME,
					HisItems._ID + "=?",
					new String[]{String.valueOf(hisModel.getId())});
		}
		else {
			db.delete(HisItems.TABLE_NAME,
					HisItems.HIS_ID + "=? and " + HisItems.HIS_TYPE + "=?",
					new String[]{hisModel.getHisId(),String.valueOf(hisModel.getHisType())});
		}

		if (nowSave) {
			if (db != null && db.isOpen()) {
				db.close();
			}
			db = null;
		}
	}

	/**
	 * 删除所有历史实体
	 * */
	public void deleteAllHis(boolean nowSave){
		if (db == null || !db.isOpen()) {
			db = openDBInSd();
		}

		db.delete(HisItems.TABLE_NAME, null, null);

		if (nowSave) {
			if (db != null && db.isOpen()) {
				db.close();
			}
			db = null;
		}
	}

	public ArrayList<HisModel> getHis(int type, boolean nowSave){

		ArrayList<HisModel> dataList = new ArrayList<>();

		if (db == null || !db.isOpen()) {
			db = openDBInSd();
		}

		Cursor cursor = null;
		try {
			String sql = "select * from " + HisItems.TABLE_NAME +
					" where " + HisItems.HIS_TYPE + "='" + type + "'";
			cursor = db.rawQuery(sql, null);
			if (cursor == null) {
				return dataList;
			}
			if (cursor.getCount() == 0) {
				return dataList;
			}

			cursor.moveToLast();
			boolean hasMore = true;
			while (hasMore) {

				HisModel model = new HisModel();
				model.setId(cursor.getInt(cursor.getColumnIndex(HisItems._ID)));
				model.setHisId(cursor.getString(cursor.getColumnIndex(HisItems.HIS_ID)));
				model.setHisType(cursor.getInt(cursor.getColumnIndex(HisItems.HIS_TYPE)));
				model.setHisPlayedTime(cursor.getInt(cursor.getColumnIndex(HisItems.HIS_PLAYEDTIME)));
				model.setHisContent(cursor.getString(cursor.getColumnIndex(HisItems.HIS_CONTENT)));
				model.setHisSubContent(cursor.getString(cursor.getColumnIndex(HisItems.HIS_SUBCONTENT)));
				model.setHisCreatedTime(cursor.getString(cursor.getColumnIndex(HisItems.HIS_CREATEDTIME)));
				model.setHisUpdated(cursor.getInt(cursor.getColumnIndex(HisItems.HIS_ISUPDATE)));
				model.setHisUpdatedTime(cursor.getString(cursor.getColumnIndex(HisItems.HIS_UPDATETIME)));
				dataList.add(model);
				hasMore = cursor.moveToPrevious();
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		if (nowSave) {
			if (db != null && db.isOpen()) {
				db.close();
			}
			db = null;
		}
		return dataList;
	}

	public ArrayList<HisModel> getHis(boolean nowSave){

		ArrayList<HisModel> dataList = new ArrayList<>();

		if (db == null || !db.isOpen()) {
			db = openDBInSd();
		}

		Cursor cursor = null;
		try {
			String sql = "select * from " + HisItems.TABLE_NAME;
			cursor = db.rawQuery(sql, null);
			if (cursor == null) {
				return dataList;
			}
			if (cursor.getCount() == 0) {
				return dataList;
			}

			cursor.moveToLast();
			boolean hasMore = true;
			while (hasMore) {

				HisModel model = new HisModel();
				model.setId(cursor.getInt(cursor.getColumnIndex(HisItems._ID)));
				model.setHisId(cursor.getString(cursor.getColumnIndex(HisItems.HIS_ID)));
				model.setHisType(cursor.getInt(cursor.getColumnIndex(HisItems.HIS_TYPE)));
				model.setHisPlayedTime(cursor.getInt(cursor.getColumnIndex(HisItems.HIS_PLAYEDTIME)));
				model.setHisContent(cursor.getString(cursor.getColumnIndex(HisItems.HIS_CONTENT)));
				model.setHisSubContent(cursor.getString(cursor.getColumnIndex(HisItems.HIS_SUBCONTENT)));
				model.setHisCreatedTime(cursor.getString(cursor.getColumnIndex(HisItems.HIS_CREATEDTIME)));
				model.setHisUpdated(cursor.getInt(cursor.getColumnIndex(HisItems.HIS_ISUPDATE)));
				model.setHisUpdatedTime(cursor.getString(cursor.getColumnIndex(HisItems.HIS_UPDATETIME)));
				dataList.add(model);
				hasMore = cursor.moveToPrevious();
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		if (nowSave) {
			if (db != null && db.isOpen()) {
				db.close();
			}
			db = null;
		}
		return dataList;
	}
}
