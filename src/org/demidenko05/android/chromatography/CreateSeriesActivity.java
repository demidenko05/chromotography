package org.demidenko05.android.chromatography;

import java.util.ArrayList;
import java.util.List;
import org.demidenko05.android.Miscellaneous;
import org.demidenko05.android.chromatography.model.Analyte;
import org.demidenko05.android.chromatography.model.Column;
import org.demidenko05.android.chromatography.model.Detector;
import org.demidenko05.android.chromatography.model.SeriesHead;
import org.demidenko05.android.chromatography.model.SeriesSolvents;
import org.demidenko05.android.chromatography.sqlite.DatabaseService;
import org.demidenko05.android.chromatography.sqlite.Datasource;
import org.demidenko05.android.chromatography.sqlite.OrmService;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class CreateSeriesActivity extends Activity {

	private Datasource ds;
	private SQLiteDatabase db;
	private DatabaseService databaseService = DatabaseService.getInstance();
	private EditText etName;
	private EditText etColumn;
	private EditText etDetector;
	private EditText etWavelenght;
	private EditText etFlowRate;
	private SeriesHead seriesHead;
	private ExpandableListAdapter listAdapter;
	private ExpandableListView expListView;
	private List<SeriesSolvents> solvents = new ArrayList<SeriesSolvents>();
	private List<Analyte> analytes = new ArrayList<Analyte>();
	protected View groupHeaderView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_series);
		ds = Datasource.getInstance();
		expListView = (ExpandableListView) findViewById(R.id.elvSolvAnalyt);
		listAdapter = new ExpandableListAdapter(this, solvents, analytes);
		expListView.setAdapter(listAdapter);
		expListView.setGroupIndicator(getResources().getDrawable(R.drawable.expandable_group));
		expListView.expandGroup(1);
		expListView.expandGroup(2);
		LayoutInflater infalInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		groupHeaderView = infalInflater.inflate(R.layout.create_series_head, null);
		etName = (EditText) groupHeaderView.findViewById(R.id.editName);
		etColumn = (EditText) groupHeaderView.findViewById(R.id.editColumn);
		etDetector = (EditText) groupHeaderView.findViewById(R.id.editDetector);
		etWavelenght = (EditText) groupHeaderView.findViewById(R.id.edWavelength);
		etFlowRate = (EditText) groupHeaderView.findViewById(R.id.edFlowRate);
		if(savedInstanceState != null) {//return from rotate
			long seriesId = savedInstanceState.getLong(OrmService.COLUMN_ID_SERIES);
			if(seriesId != 0) {
				getSeriesHeadAndRefresh(seriesId);
			}
			else {
				seriesHead = new SeriesHead();
				Long columnId = savedInstanceState.getLong(OrmService.COLUMN_ID_COLUMN);
				if(columnId != null) {
					seriesHead.setColumn(OrmServicesFactory.getInstance().getOrmService(Column.class).getEntityById(getDbToRead(), columnId));
					etColumn.setText(seriesHead.getColumn().getName());
				}
				Long detectorId = savedInstanceState.getLong(OrmService.COLUMN_ID_DETECTOR);
				if(detectorId != null) {
					seriesHead.setDetector(OrmServicesFactory.getInstance().getOrmService(Detector.class).getEntityById(getDbToRead(), detectorId));
					etDetector.setText(seriesHead.getDetector().getName());
				}
			}
		}
		else {//start for create or edit
			if(getIntent().getExtras() != null) {//edit
				long seriesId = getIntent().getExtras().getLong(OrmService.COLUMN_ID_SERIES);
				getSeriesHeadAndRefresh(seriesId);
			}
			else {//new
				seriesHead = new SeriesHead();
			}
		}
	}

	protected void getSeriesHeadAndRefresh(long seriesId) {
		seriesHead = OrmServicesFactory.getInstance().getOrmService(SeriesHead.class).getEntityById(getDbToRead(), seriesId);
		refreshHeadFromEntity();
		refreshAnalytes();
		refreshSolvents();
		listAdapter.notifyDataSetChanged();
	}
	
	private void refreshHeadFromEntity() {
		etName.setText(seriesHead.getName());
		int wavelength = seriesHead.getWavelength();
		if(wavelength != 0) etWavelenght.setText(String.valueOf(wavelength));
		etFlowRate.setText(seriesHead.getFlowRate());
		etColumn.setText(seriesHead.getColumn().getName());
		etDetector.setText(seriesHead.getDetector().getName());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.create_series, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		case R.id.act_store_series:
			seriesHead.setName(etName.getText().toString().trim());
			seriesHead.setFlowRate(etFlowRate.getText().toString().trim());
			int wavelength = 0;
			String sWvLn = etWavelenght.getText().toString().trim();
			if(!sWvLn.equals("")) wavelength = Integer.valueOf(sWvLn);
			seriesHead.setWavelength(wavelength);
			if(seriesHead.getColumn() == null || seriesHead.getDetector() == null
					 && seriesHead.getName().equals(""))
				Miscellaneous.alertDlg(R.string.must_complete_data, this);
			else if(seriesHead.getId() < 1) {
				seriesHead.setId(OrmServicesFactory.getInstance().getOrmService(SeriesHead.class).insert(getDbToWrite(), seriesHead));
				Toast.makeText(this, "Series head has been inserted!", Toast.LENGTH_SHORT).show();
			}
			else {
				OrmServicesFactory.getInstance().getOrmService(SeriesHead.class).update(getDbToWrite(), seriesHead);
				Toast.makeText(this, "Series head has been updated!", Toast.LENGTH_SHORT).show();
			}
			closeDb();
			return true;

		case R.id.actAddSeriesLolvent:
			if(seriesHead.getId() < 1)
				Miscellaneous.alertDlg(R.string.must_save_series, this);
			else {
				startSeriesSolventActivity(0);
			}
			return true;

		case R.id.actAddSeriesBody:
			if(seriesHead.getId() < 1)
				Miscellaneous.alertDlg(R.string.must_save_series, this);
			else {
				Intent intent = new Intent(this, SeriesBodyActivity.class);
				intent.putExtra(OrmService.COLUMN_ID_SERIES, seriesHead.getId());
				startActivityForResult(intent, CommunicateSchema.REQUEST_SERIESBODY);
			}
			return true;

		default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	protected void fillSeriesHead() {
		seriesHead.setName(etName.getText().toString().trim());
		String wavelenght = etWavelenght.getText().toString().trim();
		if(!wavelenght.equals(""))
			seriesHead.setWavelength(Integer.valueOf(wavelenght));
		seriesHead.setFlowRate(etFlowRate.getText().toString().trim());
	}
	
	public void onClick(View view) {
		Intent intent;
	    switch (view.getId()) {
		case R.id.btnSelectColumn:
			intent = new Intent(this, PickerForSingleActivity.class);
			intent.putExtra(CommunicateSchema.PICKER_FOR, CommunicateSchema.REQUEST_COLUMN);
			startActivityForResult(intent, CommunicateSchema.REQUEST_COLUMN);			
			break;

		case R.id.btnSelectDetector:
			intent = new Intent(this, PickerForSingleActivity.class);
			intent.putExtra(CommunicateSchema.PICKER_FOR, CommunicateSchema.REQUEST_DETECTOR);
			startActivityForResult(intent, CommunicateSchema.REQUEST_DETECTOR);
			break;
						
	    }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == Activity.RESULT_OK) {
	    	String name = data.getStringExtra("name");
	    	Long id = data.getLongExtra("id", -1L);
	    	if(!id.equals(-1L)) {
			    if (requestCode == CommunicateSchema.REQUEST_COLUMN) {
			    	Column column = new Column(); column.setId(id); column.setName(name);
			    	seriesHead.setColumn(column);
					etColumn.setText(seriesHead.getColumn().getName());
			    }
			    else if (requestCode == CommunicateSchema.REQUEST_DETECTOR) {
			    	Detector detector = new Detector(); detector.setId(id); detector.setName(name);
			    	seriesHead.setDetector(detector);
					etDetector.setText(seriesHead.getDetector().getName());
			    }
	    	}
		    else if (requestCode == CommunicateSchema.REQUEST_SERIESSOLVENT) {
		    	refreshSolvents();
		    	listAdapter.notifyDataSetChanged();
		    }
		    else if (requestCode == CommunicateSchema.REQUEST_SERIESBODY) {
		    	refreshAnalytes();
		    	listAdapter.notifyDataSetChanged();
		    }
	    }
	}

	protected void startSeriesSolventActivity(long idSolvent) {
		Intent intent = new Intent(this, SeriesSolventsActivity.class);
		intent.putExtra(OrmService.COLUMN_ID_SERIES, seriesHead.getId());
		if(idSolvent != 0) intent.putExtra(CommunicateSchema.ID_SERIESSOLVENT, idSolvent);
		startActivityForResult(intent, CommunicateSchema.REQUEST_SERIESSOLVENT);
	}
	
	protected void deleteSeriesSolvent(long idSeriesSolvent) {
		SeriesSolvents seriesSolvents = new SeriesSolvents(); seriesSolvents.setId(idSeriesSolvent);
		OrmServicesFactory.getInstance().getOrmService(SeriesSolvents.class).delete(getDbToWrite(), seriesSolvents );
		closeDb();
		refreshSolvents();
		listAdapter.notifyDataSetChanged();
	}
	
	protected void deleteSeriesBody(long idAnalyte) {
		databaseService.deleteSeriesBody(getDbToWrite(), seriesHead.getId(), idAnalyte);
		closeDb();
		refreshAnalytes();
		listAdapter.notifyDataSetChanged();
	}
	
	protected void refreshSolvents() {
		if(solvents.size() > 0) solvents.clear();
		databaseService.fillSeriesSolventsList(getDbToRead(), solvents, seriesHead.getId());
	}

	protected void refreshAnalytes() {
		if(analytes.size() > 0) analytes.clear();
		databaseService.fillSeriesAnalyteList(getDbToRead(), analytes, seriesHead.getId());
	}


	@Override
	protected void onPause() {
		closeDb();
		super.onPause();
	}
		
	protected SQLiteDatabase getDbToRead() {
		if(db == null)
			db = ds.getDbToRead();
		return db;
	}
	
	protected SQLiteDatabase getDbToWrite() {
		if(db == null || db.isReadOnly())
			db = ds.getDbToWrite();
		return db;
	}
	
	protected void closeDb() {
		if(db != null) {
			db.close();
			db = null;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(seriesHead.getColumn() != null) {
			outState.putLong(OrmService.COLUMN_ID_COLUMN, seriesHead.getColumn().getId());
		}
		if(seriesHead.getDetector() != null) {
			outState.putLong(OrmService.COLUMN_ID_DETECTOR, seriesHead.getDetector().getId());
		}
		outState.putLong(OrmService.COLUMN_ID_SERIES, seriesHead.getId());
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onBackPressed() {
		if(getIntent().getExtras() != null) {//edit
			Intent data = new Intent();
			setResult(RESULT_OK, data);
		}
		super.onBackPressed();
	}	

}
