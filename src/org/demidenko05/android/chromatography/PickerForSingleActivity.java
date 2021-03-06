package org.demidenko05.android.chromatography;

import java.util.List;
import org.demidenko05.android.chromatography.model.AbstractEntityWithName;
import org.demidenko05.android.chromatography.model.Analyte;
import org.demidenko05.android.chromatography.model.Column;
import org.demidenko05.android.chromatography.model.Detector;
import org.demidenko05.android.chromatography.model.Solvent;
import org.demidenko05.android.chromatography.sqlite.OrmService;
import org.demidenko05.android.chromatography.R;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PickerForSingleActivity extends ListActivity {

	private List<? extends AbstractEntityWithName> list;
	private TextView txtTitlePickerSingle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picker_for_single);
		int filterFor = getIntent().getExtras().getInt(CommunicateSchema.PICKER_FOR);
		txtTitlePickerSingle = (TextView) findViewById(R.id.txtTitlePickerSingle);
		switch (filterFor) {
		case CommunicateSchema.REQUEST_COLUMN:
			list = OrmServicesFactory.getInstance().getOrmService(Column.class).getEntities(null, null, null, null, OrmService.COLUMN_NAME);
			txtTitlePickerSingle.setText(this.getString(R.string.select_an_item)+" of columns:");
			break;

		case CommunicateSchema.REQUEST_DETECTOR:
			list = OrmServicesFactory.getInstance().getOrmService(Detector.class).getEntities(null, null, null, null, OrmService.COLUMN_NAME);
			txtTitlePickerSingle.setText(this.getString(R.string.select_an_item)+" of detectors:");
			break;
		
		case CommunicateSchema.REQUEST_SOLVENT:
			list = OrmServicesFactory.getInstance().getOrmService(Solvent.class).getEntities(null, null, null, null, OrmService.COLUMN_NAME);
			txtTitlePickerSingle.setText(this.getString(R.string.select_an_item)+" of solvents:");
			break;
		
		case CommunicateSchema.REQUEST_ANALYTE:
			list = OrmServicesFactory.getInstance().getOrmService(Analyte.class).getEntities(null, null, null, null, OrmService.COLUMN_NAME);
			txtTitlePickerSingle.setText(this.getString(R.string.select_an_item)+" of analytes:");
			break;
		}
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter arrAdapter = new ArrayAdapter (this, android.R.layout.simple_list_item_single_choice, list);
		setListAdapter(arrAdapter);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.for_single_picker, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    
		case R.id.act_apply:
			pickItem();
			return true;
			
		default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	private void pickItem() {
		int pos = getListView().getCheckedItemPosition();
		if(pos != 	android.widget.AdapterView.INVALID_POSITION) {
			AbstractEntityWithName item = list.get(pos);
			Intent data = new Intent();
			data.putExtra("name", item.getName());
			data.putExtra("id", item.getId());
			setResult(RESULT_OK, data);
			finish();
		}
		else {
			AlertDialog.Builder builder = new Builder(this);
		    builder
		    //.setTitle(R.string.warning)
		    .setMessage(R.string.select_an_item)
		    .setCancelable(false)
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    	public void onClick(DialogInterface dialog, int id) {
		    		dialog.cancel();
		    	}})
		    .show();
		}
	}
		
}
