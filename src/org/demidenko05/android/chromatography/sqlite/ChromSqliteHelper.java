package org.demidenko05.android.chromatography.sqlite;

import java.util.Map;
import java.util.Map.Entry;
import org.demidenko05.android.chromatography.OrmServicesFactory;
import org.demidenko05.android.chromatography.model.AbstractEntity;
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
		OrmServicesFactory ormServicesFactory = OrmServicesFactory.getInstance();
		ormServicesFactory.init(db);
		dbCreator.createAndPopulate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(ChromSqliteHelper.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		//recreate(db);
	}

	@SuppressWarnings("rawtypes")
	public void recreate(SQLiteDatabase db) {
		DbCreator dbCreator = new DbCreator();
		Map<Class<? extends AbstractEntity>, OrmService> ormServices = OrmServicesFactory.getInstance().getOrmServices();
		//create:
		for(Entry<Class<? extends AbstractEntity>, OrmService> entry : ormServices.entrySet()) {
		    db.execSQL("DROP TABLE IF EXISTS " + entry.getValue().getTableName());
		}
		dbCreator.createAndPopulate(db);
	}
}
