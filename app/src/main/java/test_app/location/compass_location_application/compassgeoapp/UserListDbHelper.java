package test_app.location.compass_location_application.compassgeoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class UserListDbHelper extends SQLiteOpenHelper {
	
//	public static final String DATABASE_NAME = "Android.db";
	public static final int VERSION = 1;
	public static final String TABLE = "Loc_Data_DB";
	public static final String ID = "ID";
	public static final String LOC_Name = "LOC_NAME";
	public static final String LOC_Description = "LOC_Description";
	public static final String LOC_Latitude = "LOC_Latitude";
	public static final String LOC_Longitude = "LOC_Longitude";
	public static final String LOC_Image_URL = "LOC_Image_URL";

	//public static final String PASSWORD = "PASSWORD";
	public String databasePath = "";
	public Context con;
	static public int var=0;

	public UserListDbHelper(Context context) {
		super(context, TABLE, null, VERSION);

		databasePath = context.getDatabasePath("Android.db").getPath();

		this.con  = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		Log.i("Database Path", databasePath);

		try {

			createTable();

		} catch (Exception e) {

		}
	}

	public void createTable() {


		SQLiteDatabase sdb = this.getWritableDatabase();
		
		String sql = "CREATE TABLE IF NOT EXISTS "+ TABLE + " (" +ID+
				" INTEGER PRIMARY KEY AUTOINCREMENT, " + LOC_Name
				+ " VARCHAR NOT NULL, " + LOC_Description + " VARCHAR(200) NOT NULL, " + LOC_Latitude + " VARCHAR NOT NULL, " +LOC_Longitude
		  +  " VARCHAR NOT NULL, "+ LOC_Image_URL + " VARCHAR NOT NULL);" ;


		sdb.execSQL(sql);

         var=1;

		 sdb.close();
		
	}


	public static boolean doesDatabaseExist(Context context, String dbName) {
		File dbFile = context.getDatabasePath(dbName);
		return dbFile.exists();
	}

	public void insertData(String loc_name, String loc_description, String loc_latitude, String loc_longitude, String loc_image_url){


	//	long rowid=0;
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			System.out.println("Sumitting:::" + loc_name);

			ContentValues values = new ContentValues();
			values.put(LOC_Name, loc_name);
			values.put(LOC_Description, loc_description);

			values.put(LOC_Latitude, loc_latitude);
			values.put(LOC_Longitude, loc_longitude);
			values.put(LOC_Image_URL, loc_image_url);

			db.insert(TABLE, null, values);
			System.out.println("Sumitting_description:::" + loc_description);
			System.out.println("Submitted...");
			//Toast.makeText(con, "Data saved in the Database", Toast.LENGTH_SHORT).show();

			db.close();
		}
		catch(Exception e){
			e.printStackTrace();

		}
		finally {

			System.out.println("The location nae is "+LOC_Name);
			System.out.println("The location Desc is "+LOC_Description);
			System.out.println("Latitude is "+LOC_Latitude);
			System.out.println("Longitude is is "+LOC_Longitude);

		}
		
	}
	
	public ArrayList<String> getLoc_name_Data(){
		
		ArrayList<String> holdUsernames = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor allData = query(db, "SELECT * From "+TABLE);
		
		if (allData.moveToFirst()) {
			while(!allData.isAfterLast()){
				
				
				holdUsernames.add(allData.getString(allData.getColumnIndex(LOC_Name)));
				
				allData.moveToNext();
			}
			
		}
		
		if (allData != null && !allData.isClosed()) {
			allData.close();
		}
		System.out.println("Data Fetched...");
		
		return holdUsernames;
	}


	public ArrayList<String> getLoc_description_Data(){

		ArrayList<String> description = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor allData = query(db, "SELECT * From "+TABLE);

		if (allData.moveToFirst()) {
			while(!allData.isAfterLast()){


				description.add(allData.getString(allData.getColumnIndex(LOC_Description)));

				allData.moveToNext();
			}

		}

		if (allData != null && !allData.isClosed()) {

			allData.close();

		}

		System.out.println("Data Fetched...");

		return description;
	}

	public ArrayList<String> getLoc_Img_URL_Data(){

		ArrayList<String> Img_URL = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor allData = query(db, "SELECT * From "+TABLE);

		if (allData.moveToFirst()) {
			while(!allData.isAfterLast()){

				Img_URL.add(allData.getString(allData.getColumnIndex(LOC_Image_URL)));

				allData.moveToNext();
			}

		}

		if (allData != null && !allData.isClosed()) {
			allData.close();
		}
		System.out.println("Data Fetched...");

		return Img_URL;
	}

	public Cursor getSQLiteDBdata() {

		Cursor C_Data = null;
		boolean status = doesDatabaseExist(con, "/data/data/test_app.location.compass_location_application.compassgeoapp/databases/"+TABLE);

		Log.i("File Status ", String.valueOf(status));

		if(doesDatabaseExist(con, TABLE)) {

			SQLiteDatabase db = this.getReadableDatabase();

			String Query = "SELECT * FROM " + TABLE;
			C_Data = db.rawQuery(Query, null);

			return C_Data;
		}
		else
			return C_Data;

		}


		public ArrayList<String> getLoc_latitude_Data(){

		ArrayList<String> latitude_list = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor allData = query(db, "SELECT * From "+TABLE);

		if (allData.moveToFirst()) {
			while(!allData.isAfterLast()){


				latitude_list.add(allData.getString(allData.getColumnIndex(LOC_Latitude)));

				allData.moveToNext();
			}

		}

		if (allData != null && !allData.isClosed()) {
			allData.close();
		}
		System.out.println("Data Fetched...");

		return latitude_list;
	}

	public ArrayList<String> getLoc_longitude_Data(){

		ArrayList<String> longitude_list = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor allData = query(db, "SELECT * From "+TABLE);

		if (allData.moveToFirst()) {
			while(!allData.isAfterLast()){


				longitude_list.add(allData.getString(allData.getColumnIndex(LOC_Longitude)));

				allData.moveToNext();
			}

		}

		if (allData != null && !allData.isClosed()) {
			allData.close();
		}
		System.out.println("Data Fetched...");

		return longitude_list;
	}


	public Cursor query(SQLiteDatabase db, String query) {

		Cursor cursor = db.rawQuery(query, null);
		System.out.println("Executing Query: " + query);
		return cursor;

	}
	public Cursor getDetails()
	{
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select name, age from ", null);
	}
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		
	}

	public Cursor fetchAlllocations() {

		SQLiteDatabase db = getReadableDatabase();

		Cursor mCursor = db.query(TABLE, new String[] {ID,
						LOC_Name, LOC_Description, LOC_Latitude, LOC_Longitude},
				null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}


	public ArrayList<String> get_all_details_in_a_row(){

		ArrayList<String> latitude_list = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor allData = query(db, "SELECT * From "+TABLE);

		if (allData.moveToFirst()) {
			while(!allData.isAfterLast()){


				latitude_list.add(allData.getString(allData.getColumnIndex(LOC_Latitude)));

				allData.moveToNext();
			}

		}

		if (allData != null && !allData.isClosed()) {
			allData.close();
		}
		System.out.println("Data Fetched...");

		return latitude_list;
	}
}
