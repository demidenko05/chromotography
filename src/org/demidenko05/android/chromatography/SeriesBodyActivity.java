package org.demidenko05.android.chromatography;

import org.demidenko05.android.Miscellaneous;
import org.demidenko05.android.chromatography.model.SeriesHead;
import org.demidenko05.android.chromatography.model.SeriesBody;
import org.demidenko05.android.chromatography.model.Analyte;
import org.demidenko05.android.chromatography.sqlite.OrmService;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.app.Activity;
import android.content.Intent;

public class SeriesBodyActivity extends Activity {
	
	private SeriesBody seriesBody = new SeriesBody();
	private EditText etAnalyte;
	private EditText etInjection;
	private EditText etDuration;
	private EditText etStepsInMinute;
	private EditText etRetentionButtom;
	private EditText etRetentionTop;
	private EditText etRetentionAt;
	private EditText etTimeSeries;
	private double[] timeSeries;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_series_body);
		etAnalyte = (EditText) findViewById(R.id.edAnalyte);
		etInjection = (EditText) findViewById(R.id.edInjection);
		etDuration = (EditText) findViewById(R.id.edDuration);
		etStepsInMinute = (EditText) findViewById(R.id.edStepsInMinute);
		etRetentionTop = (EditText) findViewById(R.id.edRetentionTop);
		etRetentionButtom = (EditText) findViewById(R.id.edRetentionButtom);
		etRetentionAt = (EditText) findViewById(R.id.edRetentionAt);
		etTimeSeries = (EditText) findViewById(R.id.edTimeSeries);
		long idSeries, idAnalyte = 0;
		if(savedInstanceState != null) {//return from rotate
			idSeries = savedInstanceState.getLong(OrmService.COLUMN_ID_SERIES);
			idAnalyte = savedInstanceState.getLong(OrmService.COLUMN_ID_ANALYTE);
			timeSeries = savedInstanceState.getDoubleArray(CommunicateSchema.TIMES_SERIES_ARRAY);
			if(idAnalyte != 0) {
				seriesBody.setAnalyte(OrmServicesFactory.getInstance().getOrmService(Analyte.class).getEntityById(idAnalyte));
				etAnalyte.setText(seriesBody.getAnalyte().getName());
			}
		}
		else {//create
			idSeries = getIntent().getExtras().getLong(OrmService.COLUMN_ID_SERIES);
		}
		SeriesHead seriesHead = new SeriesHead(); seriesHead.setId(idSeries);
		seriesBody.setSeriesHead(seriesHead );
	}

	public void onClick(View view) {
		int stepsInMunute = 0;
		String stInMin = etStepsInMinute.getText().toString().trim();
		if(!stInMin.equals("")) stepsInMunute = Integer.valueOf(stInMin);
	    switch (view.getId()) {
		case R.id.btnSelectAnalyte:
			Intent intent = new Intent(this, PickerForSingleActivity.class);
			intent.putExtra(CommunicateSchema.PICKER_FOR, CommunicateSchema.REQUEST_ANALYTE);
			startActivityForResult(intent, CommunicateSchema.REQUEST_ANALYTE);			
			break;
			
		case R.id.btnGenerateTimeSeries:
			int duration = 0;
			String sDur = etDuration.getText().toString().trim();
			if(!sDur.equals("")) duration = Integer.valueOf(sDur);
			int retTop = 0;
			String sRetTop = etRetentionTop.getText().toString().trim();
			if(!sRetTop.equals("")) retTop = Integer.valueOf(sRetTop);
			int retBtm = 0;
			String sRetBtm = etRetentionButtom.getText().toString().trim();
			if(!sRetBtm.equals("")) retBtm = Integer.valueOf(sRetBtm);
			int retAt = 0;
			String sRetAt = etRetentionAt.getText().toString().trim();
			if(!sRetAt.equals("")) retAt = Integer.valueOf(sRetAt);
			if(seriesBody.getAnalyte() == null || stepsInMunute == 0 || duration == 0 || retTop == 0  || retAt == 0)
				Miscellaneous.alertDlg(R.string.must_complete_data_sbts, this);
			else {
				timeSeries = new double[duration];
				for(int i=0; i<duration; i++) {
					if(i == retAt) timeSeries[i] = retTop;
					else  timeSeries[i] = retBtm;
				}
				etTimeSeries.setText("");
				for(double value : timeSeries)
					etTimeSeries.append(String.valueOf(value) + "; ");
			}
			break;

		case R.id.btnStoreSeriesBody:
			if(seriesBody.getAnalyte() == null || timeSeries == null || stepsInMunute == 0)
				Miscellaneous.alertDlg(R.string.must_complete_data_sb, this);
			else {
				String injection = etInjection.getText().toString().trim();
				if(!injection.equals(""))
					seriesBody.setInjection(Integer.valueOf(injection));
				int i = 0;
				for(Double value : timeSeries) {
					seriesBody.setTime(Double.valueOf(i++)/stepsInMunute);
					seriesBody.setValue(value);
					OrmServicesFactory.getInstance().getOrmService(SeriesBody.class).insert(seriesBody);
				}
				Intent data = new Intent();
				setResult(RESULT_OK, data);
				finish();
			}
			break;
	    }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == Activity.RESULT_OK) {
	    	String name = data.getStringExtra("name");
	    	Long id = data.getLongExtra("id", -1L);
	    	if(!id.equals(-1L)) {
			    if (requestCode == CommunicateSchema.REQUEST_ANALYTE) {
			    	Analyte analyte = new Analyte(); analyte.setId(id); analyte.setName(name);
			    	seriesBody.setAnalyte(analyte);
			    	etAnalyte.setText(seriesBody.getAnalyte().getName());
			    }
	    	}
	    }
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(seriesBody.getAnalyte() != null) {
			outState.putLong(OrmService.COLUMN_ID_ANALYTE, seriesBody.getAnalyte().getId());
		}
		outState.putLong(OrmService.COLUMN_ID_SERIES, seriesBody.getSeriesHead().getId());
		if(timeSeries != null) {
			outState.putDoubleArray(CommunicateSchema.TIMES_SERIES_ARRAY, timeSeries);
		}
		super.onSaveInstanceState(outState);
	}

}
