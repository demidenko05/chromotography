package org.demidenko05.android.chromatography.sqlite;

import org.demidenko05.android.chromatography.model.TableDescriptor;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChromSqliteHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "chromagraphy.db";
	public static final int DB_VERSION = 1;
	
	public ChromSqliteHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		DbCreator dbCreator = new DbCreator();//To avoid use a lot of memory then the DB is already created and populated
		dbCreator.createAndPopulate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(ChromSqliteHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		recreate(db);
	}

	public void recreate(SQLiteDatabase db) {
		DbCreator dbCreator = new DbCreator();
		for(TableDescriptor table : dbCreator.getTables()) {
		    db.execSQL("DROP TABLE IF EXISTS " + table.getTableName());
		}
		dbCreator.createAndPopulate(db);
	}
}
