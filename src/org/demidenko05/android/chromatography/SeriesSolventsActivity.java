package org.demidenko05.android.chromatography;

import org.demidenko05.android.Miscellaneous;
import org.demidenko05.android.chromatography.model.SeriesHead;
import org.demidenko05.android.chromatography.model.SeriesSolvents;
import org.demidenko05.android.chromatography.model.Solvent;
import org.demidenko05.android.chromatography.sqlite.OrmService;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.app.Activity;
import android.content.Intent;

public class SeriesSolventsActivity extends Activity {
	
	private SeriesSolvents seriesSolvent;
	private EditText etSolvent;
	private EditText etAmount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_series_solvents);
		etSolvent = (EditText) findViewById(R.id.edtSolvent);
		etAmount = (EditText) findViewById(R.id.edtAmount);
		long idSeries;
		if(savedInstanceState != null) { //return from rotate
			seriesSolvent = new SeriesSolvents();
			idSeries = savedInstanceState.getLong(OrmService.COLUMN_ID_SERIES);
			long idSolvent = savedInstanceState.getLong(OrmService.COLUMN_ID_SOLVENT);
			if(idSolvent != 0) {
				seriesSolvent.setSolvent(OrmServicesFactory.getInstance().getOrmService(Solvent.class).getEntityById(idSolvent));
				etSolvent.setText(seriesSolvent.getSolvent().getName());
			}
		}
		else {//create or edit
			idSeries = getIntent().getExtras().getLong(OrmService.COLUMN_ID_SERIES);
			long idSeriesSolvent = getIntent().getExtras().getLong(CommunicateSchema.ID_SERIESSOLVENT);
			if(idSeriesSolvent != 0) {
				seriesSolvent = OrmServicesFactory.getInstance().getOrmService(SeriesSolvents.class).getEntityById(idSeriesSolvent);
				if(seriesSolvent.getSolvent() != null) 
					etSolvent.setText(seriesSolvent.getSolvent().getName());
				if(seriesSolvent.getAmount() != null) 
					etAmount.setText(seriesSolvent.getAmount());		
			}
			else 
				seriesSolvent = new SeriesSolvents();
		}
		SeriesHead seriesHead = new SeriesHead(); seriesHead.setId(idSeries);
		seriesSolvent.setSeriesHead(seriesHead);
	}

	public void onClick(View view) {
	    switch (view.getId()) {
		case R.id.btnSelectSolvent:
			Intent intent = new Intent(this, PickerForSingleActivity.class);
			intent.putExtra(CommunicateSchema.PICKER_FOR, CommunicateSchema.REQUEST_SOLVENT);
			startActivityForResult(intent, CommunicateSchema.REQUEST_SOLVENT);			
			break;
			
		case R.id.btnStoreSeriesSolvent:
			if(seriesSolvent.getSolvent() == null || etAmount.getText().toString().trim().equals(""))
				Miscellaneous.alertDlg(R.string.must_complete_data_ss, this);
			else {
				seriesSolvent.setAmount(etAmount.getText().toString().trim());
				if(seriesSolvent.getId() == 0) 
					OrmServicesFactory.getInstance().getOrmService(SeriesSolvents.class).insert(seriesSolvent);
				else 
					OrmServicesFactory.getInstance().getOrmService(SeriesSolvents.class).update(seriesSolvent);
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
			    if (requestCode == CommunicateSchema.REQUEST_SOLVENT) {
			    	Solvent solvent = new Solvent(); solvent.setId(id); solvent.setName(name);
			    	seriesSolvent.setSolvent(solvent);
					etSolvent.setText(seriesSolvent.getSolvent().getName());
			    }
	    	}
	    }
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(seriesSolvent.getSolvent() != null) {
			outState.putLong(OrmService.COLUMN_ID_SOLVENT, seriesSolvent.getSolvent().getId());
		}
		outState.putLong(OrmService.COLUMN_ID_SERIES, seriesSolvent.getSeriesHead().getId());
		super.onSaveInstanceState(outState);
	}

}
