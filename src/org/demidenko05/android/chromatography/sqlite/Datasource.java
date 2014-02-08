package org.demidenko05.android.chromatography.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Datasource {
	private ChromSqliteHelper dbHelper;
	
	private static Datasource datasource = new Datasource();
	//synchronized no need because activities have same thread
	public static synchronized Datasource getInstance() {
		return datasource;
	}
	
	public synchronized void init(Context context) {//app context
		if(dbHelper==null) 
			dbHelper = new ChromSqliteHelper(context);
	}
	//on an activity start/resume
	public SQLiteDatabase getDbToWrite() {
		return dbHelper.getWritableDatabase();
	}
	//on an activity start/resume
	public SQLiteDatabase getDbToRead() {
		return dbHelper.getReadableDatabase();
	}
	
}
