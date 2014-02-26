/**
 * Yury Demidenko 2014
 * licensed under the MIT licence 
 */
package org.demidenko05.android.chromatography;

import org.demidenko05.android.chromatography.R;
import org.demidenko05.android.chromatography.model.Analyte;
import org.demidenko05.android.chromatography.sqlite.DatabaseService;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {
	
	private DatabaseService databaseService;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseService = DatabaseService.getInstance();
        databaseService.init(getApplicationContext());
        setContentView(R.layout.activity_main);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.for_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		AlertDialog.Builder builder;
	    switch (item.getItemId()) {
	    
		case R.id.act_recreate:
			builder = new Builder(this);
		    builder
		    .setTitle(R.string.recreate_db)
		    .setMessage(R.string.are_you_sure)
		    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int id) {
	    		dialog.cancel();
	    	}})
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    	public void onClick(DialogInterface dialog, int id) {
		    		databaseService.recreateDb();
		    	}})
		    .show();
			return true;
			
		case R.id.act_testmultithreading:
			runMultithreadingTest();
			return true;
			
		default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	/**
	 * It works well when sqlitedb is synchronized or not
	 */
	protected void runMultithreadingTest() {
		AlertDialog.Builder builder;
		builder = new Builder(this);
		builder
		.setTitle(R.string.act_test_multi_threading)
		.setMessage(R.string.are_you_sure)
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int id) {
			dialog.cancel();
		}})
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Thread thread1 = new Thread( new Runnable() {					
					@Override
					public void run() {
						for(int i=0; i<30; i++) {
							Analyte entity = new Analyte();
							entity.setName("1st thread-"+i+" time "+System.currentTimeMillis());
							OrmServicesFactory.getInstance().getOrmService(Analyte.class).insert(entity);
						}
					}
				});
				Thread thread2 = new Thread( new Runnable() {					
					@Override
					public void run() {
						for(int i=0; i<30; i++) {
							Analyte entity = new Analyte();
							entity.setName("2nd thread-"+i+" time "+System.currentTimeMillis());
							OrmServicesFactory.getInstance().getOrmService(Analyte.class).insert(entity);
						}
					}
				});
				Thread thread3 = new Thread( new Runnable() {					
					@Override
					public void run() {
						for(int i=0; i<30; i++) {
							Analyte entity = new Analyte();
							entity.setName("2nd thread-"+i+" time "+System.currentTimeMillis());
							OrmServicesFactory.getInstance().getOrmService(Analyte.class).getEntities(null, null, null, null, null);
						}
					}
				});
				thread3.start();
				thread1.start();
				thread2.start();
			}})
		.show();
	}

	@Override
	public void onClick(View view) {
		Intent intent;
	    switch (view.getId()) {
		case R.id.buttonGetNewData:
			intent = new Intent(this, CreateSeriesActivity.class);
			startActivity(intent);
			break;
		case R.id.buttonShowStoredData:
			intent = new Intent(this, StoredDataActivity.class);
			startActivity(intent);
			break;
		}
	}
	    
}
