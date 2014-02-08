package org.demidenko05.android.chromatography;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.demidenko05.android.chromatography.provider.ContentProviderForChartdroid;
import org.demidenko05.android.chromatography.exception.SeriesNotSameDurationException;
import org.demidenko05.android.chromatography.model.Column;
import org.demidenko05.android.chromatography.model.Detector;
import org.demidenko05.android.chromatography.model.SeriesHead;
import org.demidenko05.android.chromatography.model.TableDescriptor;
import org.demidenko05.android.chromatography.sqlite.DatabaseService;
import org.demidenko05.android.chromatography.sqlite.Datasource;
import org.demidenko05.android.chromatography.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StoredDataActivity extends ListActivity {

	public static final int REQUEST_COLUMN = 0;
	public static final int REQUEST_DETECTOR = 1;
	public static final String FILTER_FOR = "Filter for";
	private Datasource ds;
	private DatabaseService databaseService = DatabaseService.getInstance();
	private DataToPlot dataToPlot = DataToPlot.getInstance();
	private SQLiteDatabase db;
	private Column filterColumn;
	private Detector filterDetector;
	private List<SeriesHead> list = new ArrayList<SeriesHead>();
	private TextView txtTitleStoredData;
	private ArrayAdapter<SeriesHead> arrAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stored_data);
		ds = Datasource.getInstance();
		txtTitleStoredData = (TextView) findViewById(R.id.txtTitleStoredData);
		refreshList();
		arrAdapter = new ArrayAdapter<SeriesHead>(this, android.R.layout.simple_list_item_multiple_choice, list);
		setListAdapter(arrAdapter);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stored_data, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
	    switch (item.getItemId()) {
		case R.id.action_filter_column:
			intent = new Intent(this, PickerForSingleActivity.class);
			intent.putExtra(FILTER_FOR, REQUEST_COLUMN);
			startActivityForResult(intent, REQUEST_COLUMN);			
			return true;

		case R.id.act_fltr_detect:
			intent = new Intent(this, PickerForSingleActivity.class);
			intent.putExtra(FILTER_FOR, REQUEST_DETECTOR);
			startActivityForResult(intent, REQUEST_DETECTOR);
			return true;
			
		case R.id.act_clear_fltcolumn:
			if(filterColumn != null) {
				filterColumn = null;
				refreshList();
			}
			else alertDlg(R.string.there_is_no_filter);
			return true;

		case R.id.act_clear_fltdetector:
			if(filterDetector != null) {
				filterDetector = null;
				refreshList();
			}
			else alertDlg(R.string.there_is_no_filter);
			return true;

		case R.id.act_show:
			showDiagramm();
			return true;

		default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	private void alertDlg(int message) {
		AlertDialog.Builder builder = new Builder(this);
	    builder
	    .setMessage(message)
	    .setCancelable(false)
	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int id) {
	    		dialog.cancel();
	    	}})
	    .show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == Activity.RESULT_OK) {
	    	String name = data.getStringExtra("name");
	    	Long id = data.getLongExtra("id", -1L);
	    	if(!id.equals(-1L)) {
			    if (requestCode == REQUEST_COLUMN) {
			    	filterColumn = new Column(); filterColumn.setId(id); filterColumn.setName(name);
			    }
			    else if (requestCode == REQUEST_DETECTOR) {
			    	filterDetector = new Detector(); filterDetector.setId(id); filterDetector.setName(name);
			    }
			    refreshList();
	    	}
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
	
	private SQLiteDatabase getDbToRead() {
		if(db == null)
			db = ds.getDbToRead();
		return db;
	}
	
	public String getFilterColumnText() {
		if(filterColumn == null) return "No";
		return filterColumn.getName();
	}
	
	public String getFilterDetectorText() {
		if(filterDetector == null) return "No";
		return filterDetector.getName();
	}
	
	private void showDiagramm() {
        Intent intent = new Intent(Intent.ACTION_VIEW, ContentProviderForChartdroid.PROVIDER_URI);
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> listPkg = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if(listPkg.size() <= 0) {
        	alertDlg(R.string.need_chartdroid);
        	return;
        }
		SparseBooleanArray selPoss = getListView().getCheckedItemPositions();
		if(selPoss == null || selPoss.size() == 0) {
        	alertDlg(R.string.pl_sel_ser_to_show);
        	return;
		}
		Map<String, SeriesHead> seriesToShow = new LinkedHashMap<String, SeriesHead>();
		String title = "";
		//get selected series:
		int j=0;
		for(int i=0;i<list.size();i++){
			//char[] letter = Character.toChars(Character.getNumericValue('a')+i);
			if(selPoss.get(i)) {
				char letter = (char) ('a'+j++);
				seriesToShow.put(Character.toString(letter), list.get(i));
				title += " (" +letter + ") " + list.get(i).getName();
			}
		}
		//put series to chart activity
		try {
			dataToPlot.setSeriesToShow(seriesToShow);
			dataToPlot.setTitles(databaseService.getTitles(getDbToRead(), seriesToShow));
			dataToPlot.setXAxisData(databaseService.getXAxisData(getDbToRead(), seriesToShow));
			dataToPlot.setYAxisDataList(databaseService.getYAxisDataList(getDbToRead(), seriesToShow));
	        intent.putExtra(Intent.EXTRA_TITLE, title);
			startActivity(intent);
		} catch (SeriesNotSameDurationException e) {
			alertDlg(R.string.ser_no_same_dur);
		}
	}
		
	private void refreshList() {
		String selection = "";
		String titleAdd = null;
		if(filterColumn != null) {
			titleAdd = " (for " +filterColumn.getName();
			selection = " Where " + TableDescriptor.COLUMN_ID_COLUMN + " = " +filterColumn.getId();
		}
		if(filterDetector != null) {
			if(titleAdd == null)
				titleAdd = " (for " +filterDetector.getName();
			else
				titleAdd += " and " +filterDetector.getName();
			if(selection.equals(""))
				selection = " Where " + TableDescriptor.COLUMN_ID_DETECTOR + " = " +filterDetector.getId();
			else
				selection += " And " + TableDescriptor.COLUMN_ID_DETECTOR + " = " +filterDetector.getId();
		}
		if(titleAdd == null) titleAdd = "";
		else titleAdd += ")";
		String title = this.getString(R.string.stored_series) + titleAdd;
		txtTitleStoredData.setText(title+":");
		if(list.size() > 0) list.clear();
		databaseService.fillSeriesList(getDbToRead(), list, selection);
		if(arrAdapter != null) arrAdapter.notifyDataSetChanged();
	}


	public Column getFilterColumn() {
		return filterColumn;
	}

	public void setFilterColumn(Column filterColumn) {
		this.filterColumn = filterColumn;
	}

	public Detector getFilterDetector() {
		return filterDetector;
	}

	public void setFilterDetector(Detector filterDetector) {
		this.filterDetector = filterDetector;
	}

}
