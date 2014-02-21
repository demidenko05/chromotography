package org.demidenko05.android.chromatography;

import java.util.List;

import org.demidenko05.android.chromatography.model.AbstractEntity;
import org.demidenko05.android.chromatography.model.Analyte;
import org.demidenko05.android.chromatography.model.SeriesSolvents;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
	
	private CreateSeriesActivity createSeriesActivity;
	private String[] headers = {"Header", "Solvents", "Analytes" };
	private List<SeriesSolvents> solvents;
	private List<Analyte> analytes;
	

	public ExpandableListAdapter(CreateSeriesActivity createSeriesActivity, List<SeriesSolvents> solvents,
			List<Analyte> analytes) {
		this.createSeriesActivity = createSeriesActivity;
		this.solvents = solvents;
		this.analytes = analytes;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		switch (groupPosition) {
		case 1:
			return solvents.get(childPosititon);

		case 2:
			return analytes.get(childPosititon);

		default:
			return null;
		}
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
		final Object child = getChild(groupPosition, childPosition);
		//if (convertView == null) { the reason - it has 2 different layouts
            LayoutInflater infalInflater = (LayoutInflater) createSeriesActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if(groupPosition == 1) {
	            convertView = infalInflater.inflate(R.layout.list_item_solvent, null);
			}
			else if(groupPosition == 2) {
	            convertView = infalInflater.inflate(R.layout.list_item, null);
			}
        //}
		if(groupPosition == 1 || groupPosition == 2) {
			TextView txtListChild = (TextView) convertView
	                .findViewById(R.id.lblListItem);
			txtListChild.setText(child.toString());
			Button btnDelete = (Button) convertView
	                .findViewById(R.id.btnDelete);
			OnClickListener deleteListener;
			if(groupPosition == 1) {
				deleteListener = new OnClickListener() {
					@Override
					public void onClick(View v) {
						AlertDialog.Builder builder = new Builder(createSeriesActivity);
					    builder
					    .setTitle(R.string.delete)
					    .setMessage("Delete " + child.toString() +"?")
					    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				    	public void onClick(DialogInterface dialog, int id) {
				    		dialog.cancel();
				    	}})
					    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					    	public void onClick(DialogInterface dialog, int id) {
								createSeriesActivity.deleteSeriesSolvent(((AbstractEntity)child).getId());
					    	}})
					    .show();
					}
				};
				btnDelete.setOnClickListener(deleteListener);
			}
			else {
				deleteListener = new OnClickListener() {
					@Override
					public void onClick(View v) {
						AlertDialog.Builder builder = new Builder(createSeriesActivity);
					    builder
					    .setTitle(R.string.delete)
					    .setMessage("Delete " + child.toString() +"?")
					    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				    	public void onClick(DialogInterface dialog, int id) {
				    		dialog.cancel();
				    	}})
					    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					    	public void onClick(DialogInterface dialog, int id) {
								createSeriesActivity.deleteSeriesBody(((AbstractEntity)child).getId());
					    	}})
					    .show();
					}
				};			
			}
			btnDelete.setOnClickListener(deleteListener);
		}
		if(groupPosition == 1) {
			Button btnEdit = (Button) convertView
	                .findViewById(R.id.btnEditItem);
			OnClickListener editListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					createSeriesActivity.startSeriesSolventActivity(((AbstractEntity)child).getId());
				}
			};
			btnEdit.setOnClickListener(editListener);
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		switch (groupPosition) {
		case 1:
			return solvents.size();

		case 2:
			return analytes.size();

		default:
			return 0;
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		return headers[groupPosition];
	}

	@Override
	public int getGroupCount() {
		return headers.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        if (groupPosition == 0) {
        	convertView = createSeriesActivity.groupHeaderView;
        }
        else {
            LayoutInflater infalInflater = (LayoutInflater) createSeriesActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText((String) getGroup(groupPosition));
     
        }
        return convertView;	
    }

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}


}
