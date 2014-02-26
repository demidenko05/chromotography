package org.demidenko05.android.chromatography;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.demidenko05.android.Miscellaneous;
import org.demidenko05.android.chromatography.provider.ContentProviderForChartdroid;
import org.demidenko05.android.chromatography.exception.IncompleteDataException;
import org.demidenko05.android.chromatography.exception.SeriesNotSameDurationException;
import org.demidenko05.android.chromatography.model.Column;
import org.demidenko05.android.chromatography.model.Detector;
import org.demidenko05.android.chromatography.model.SeriesHead;
import org.demidenko05.android.chromatography.sqlite.DatabaseService;
import org.demidenko05.android.chromatography.sqlite.OrmService;
import org.demidenko05.android.chromatography.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StoredDataActivity extends ListActivity {

	private DatabaseService databaseService = DatabaseService.getInstance();
	private DataToPlot dataToPlot = DataToPlot.getInstance();
	private Column filterColumn;
	private Detector filterDetector;
	private List<SeriesHead> list = new ArrayList<SeriesHead>();
	private TextView txtTitleStoredData;
	private ArrayAdapter<SeriesHead> arrAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stored_data);
		txtTitleStoredData = (TextView) findViewById(R.id.txtTitleStoredData);
		if(savedInstanceState != null) {//return from rotate
			Long columnId = savedInstanceState.getLong(OrmService.COLUMN_ID_COLUMN);
			if(columnId != null) filterColumn = OrmServicesFactory.getInstance().getOrmService(Column.class).getEntityById(columnId);
			Long detectorId = savedInstanceState.getLong(OrmService.COLUMN_ID_DETECTOR);
			if(detectorId != null) filterDetector = OrmServicesFactory.getInstance().getOrmService(Detector.class).getEntityById(detectorId);
		}
		arrAdapter = new ArrayAdapter<SeriesHead>(this, android.R.layout.simple_list_item_multiple_choice, list);
		setListAdapter(arrAdapter);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		refreshList();
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
			intent.putExtra(CommunicateSchema.PICKER_FOR, CommunicateSchema.REQUEST_COLUMN);
			startActivityForResult(intent, CommunicateSchema.REQUEST_COLUMN);			
			return true;

		case R.id.act_fltr_detect:
			intent = new Intent(this, PickerForSingleActivity.class);
			intent.putExtra(CommunicateSchema.PICKER_FOR, CommunicateSchema.REQUEST_DETECTOR);
			startActivityForResult(intent, CommunicateSchema.REQUEST_DETECTOR);
			return true;
			
		case R.id.act_clear_fltcolumn:
			if(filterColumn != null) {
				filterColumn = null;
				refreshList();
			}
			else Miscellaneous.alertDlg(R.string.there_is_no_filter, this);
			return true;

		case R.id.act_clear_fltdetector:
			if(filterDetector != null) {
				filterDetector = null;
				refreshList();
			}
			else Miscellaneous.alertDlg(R.string.there_is_no_filter, this);
			return true;

		case R.id.act_show:
			showDiagramm();
			return true;

		case R.id.act_edit:
			editSeries();
			return true;

		default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == Activity.RESULT_OK) {
	    	String name = data.getStringExtra("name");
	    	Long id = data.getLongExtra("id", -1L);
	    	if(!id.equals(-1L)) {
			    if (requestCode == CommunicateSchema.REQUEST_COLUMN) {
			    	filterColumn = new Column(); filterColumn.setId(id); filterColumn.setName(name);
			    }
			    else if (requestCode == CommunicateSchema.REQUEST_DETECTOR) {
			    	filterDetector = new Detector(); filterDetector.setId(id); filterDetector.setName(name);
			    }
	    	}
		    refreshList();
	    }
	}
	
	public String getFilterColumnText() {
		if(filterColumn == null) return "No";
		return filterColumn.getName();
	}
	
	public String getFilterDetectorText() {
		if(filterDetector == null) return "No";
		return filterDetector.getName();
	}
	
	protected void editSeries() {
		SparseBooleanArray selPoss = getListView().getCheckedItemPositions();
		if(selPoss == null || selPoss.size() == 0) {
			Miscellaneous.alertDlg(R.string.pl_sel_ser_to_edit, this);
        	return;
		}
		int j=0;
		SeriesHead seriesToEdit = null;
		for(int i=0;i<list.size();i++){
			if(selPoss.get(i)) {
				j++;
				seriesToEdit = list.get(i);
			}
		}
		if(j > 1 || j == 0) {
			Miscellaneous.alertDlg(R.string.pl_sel_ser_to_show, this);
        	return;
		}
		Intent intent = new Intent(this, CreateSeriesActivity.class);
		intent.putExtra(OrmService.COLUMN_ID_SERIES, seriesToEdit.getId());
		startActivityForResult(intent, CommunicateSchema.EDIT_SERIES);
	}
	private void showDiagramm() {
        Intent intent = new Intent(Intent.ACTION_VIEW, ContentProviderForChartdroid.PROVIDER_URI);
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> listPkg = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if(listPkg.size() <= 0) {
        	Miscellaneous.alertDlg(R.string.need_chartdroid, this);
        	return;
        }
		SparseBooleanArray selPoss = getListView().getCheckedItemPositions();
		if(selPoss == null || selPoss.size() == 0) {
			Miscellaneous.alertDlg(R.string.pl_sel_ser_to_show, this);
        	return;
		}
		Map<String, SeriesHead> seriesToShow = new LinkedHashMap<String, SeriesHead>();
		String title = "";
		//get selected series:
		int j=0;
		for(int i=0;i<list.size();i++){
			if(selPoss.get(i)) {
				char letter = (char) ('a'+j++);
				seriesToShow.put(Character.toString(letter), list.get(i));
				title += " (" +letter + ") " + list.get(i).getName();
			}
		}
		if(seriesToShow.size() == 0) {
			Miscellaneous.alertDlg(R.string.pl_sel_ser_to_show, this);
        	return;
		}
		//put series to chart activity
		try {
			dataToPlot.setSeriesToShow(seriesToShow);
			dataToPlot.setTitles(databaseService.getTitles(seriesToShow));
			dataToPlot.setXAxisData(databaseService.getXAxisData(seriesToShow));
			dataToPlot.setYAxisDataList(databaseService.getYAxisDataList(seriesToShow));
	        intent.putExtra(Intent.EXTRA_TITLE, title);
			startActivity(intent);
		} catch (SeriesNotSameDurationException e) {
			Miscellaneous.alertDlg(R.string.ser_no_same_dur, this);
		} catch (IncompleteDataException e) {
			Miscellaneous.alertDlg(R.string.incomplete_data, this);
		}
	}
		
	private void refreshList() {
		String selection = "";
		String titleAdd = null;
		if(filterColumn != null) {
			titleAdd = " (for " +filterColumn.getName();
			selection = " Where " + OrmService.COLUMN_ID_COLUMN + " = " +filterColumn.getId();
		}
		if(filterDetector != null) {
			if(titleAdd == null)
				titleAdd = " (for " +filterDetector.getName();
			else
				titleAdd += " and " +filterDetector.getName();
			if(selection.equals(""))
				selection = " Where " + OrmService.COLUMN_ID_DETECTOR + " = " +filterDetector.getId();
			else
				selection += " And " + OrmService.COLUMN_ID_DETECTOR + " = " +filterDetector.getId();
		}
		if(titleAdd == null) titleAdd = "";
		else titleAdd += ")";
		String title = this.getString(R.string.stored_series) + titleAdd;
		txtTitleStoredData.setText(title+":");
		if(list.size() > 0) list.clear();
		databaseService.fillSeriesList(list, selection);
		arrAdapter.notifyDataSetChanged();
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(filterColumn != null) {
			outState.putLong(OrmService.COLUMN_ID_COLUMN, filterColumn.getId());
		}
		if(filterDetector != null) {
			outState.putLong(OrmService.COLUMN_ID_DETECTOR, filterDetector.getId());
		}
		super.onSaveInstanceState(outState);
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
