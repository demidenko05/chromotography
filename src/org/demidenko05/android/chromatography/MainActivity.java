package org.demidenko05.android.chromatography;

import org.demidenko05.android.chromatography.model.TableDescriptor;
import org.demidenko05.android.chromatography.sqlite.Datasource;
import org.demidenko05.android.chromatography.sqlite.DbCreator;
import org.demidenko05.android.chromatography.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {
	
	private Datasource ds;
	private SQLiteDatabase db;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		ds = Datasource.getInstance();
		ds.init(getApplicationContext());
        setContentView(R.layout.activity_main);
       	findViewById(R.id.buttonShowStoredData).setOnClickListener(this);
       	findViewById(R.id.buttonGetNewData).setOnClickListener(this);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.for_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    
		case R.id.act_recreate:
			AlertDialog.Builder builder = new Builder(this);
		    builder
		    .setTitle(R.string.recreate_db)
		    .setMessage(R.string.are_you_sure)
		    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int id) {
	    		dialog.cancel();
	    	}})
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    	public void onClick(DialogInterface dialog, int id) {
		    		db = ds.getDbToWrite();
		    		DbCreator dbCreator = new DbCreator();
		    		for(TableDescriptor table : dbCreator.getTables()) {
		    		    db.execSQL("DROP TABLE IF EXISTS " + table.getTableName());
		    		}
		    		dbCreator.createAndPopulate(db);
		    	}})
		    .show();
			return true;
			
		default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void onClick(View view) {
	    switch (view.getId()) {
		case R.id.buttonGetNewData:
			
			break;
		case R.id.buttonShowStoredData:
			Intent intent = new Intent(this, StoredDataActivity.class);
			startActivity(intent);
			break;
		}
	}
	    
	@Override
	protected void onPause() {
		closeDb();
		super.onPause();
	}
	
	private void closeDb() {
		if(db != null) {
			db.close();
			db = null;
		}
	}
}
