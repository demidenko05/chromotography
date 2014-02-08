package org.demidenko05.android.chromatography.model;

import java.util.Map;
import android.content.ContentValues;

public class SeriesSolvents extends AbstractHasSeries {
	public static final String TABLE_NAME = "series_solvents";
	public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_ID_SERIES, COLUMN_ID_SOLVENT, COLUMN_AMOUNT_SOLVENT};
	static {
	}
	private Solvent solvent;
	private String amount;
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	@Override
	public Map<String, ColumnDescriptor> getColumsDescriptor() {
		Map<String, ColumnDescriptor> columnsDescriptor = super.getColumsDescriptor();
		columnsDescriptor.put(COLUMN_AMOUNT_SOLVENT, new TableDescriptor.ColumnDescriptor("Text Not Null", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_ID_SOLVENT, new TableDescriptor.ColumnDescriptor("Integer Not Null", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_ID_SERIES, new TableDescriptor.ColumnDescriptor("Integer Not Null", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_ID_SERIES+"FK", new TableDescriptor.ColumnDescriptor("Foreign Key ("+COLUMN_ID_SERIES+") REFERENCES "+SeriesHead.TABLE_NAME+" ("+COLUMN_ID+")", DbColumnType.FOREIGN_KEY));
		columnsDescriptor.put(COLUMN_ID_SOLVENT+"FK", new TableDescriptor.ColumnDescriptor("Foreign Key ("+COLUMN_ID_SOLVENT+") REFERENCES "+Solvent.TABLE_NAME+" ("+COLUMN_ID+")", DbColumnType.FOREIGN_KEY));
		return columnsDescriptor;
	}
	@Override
	public ContentValues getValuesMap() {
		ContentValues valuesMap = super.getValuesMap();
		valuesMap.put(COLUMN_ID_SOLVENT, getSolvent().getId());
		valuesMap.put(COLUMN_AMOUNT_SOLVENT, getAmount());		
		return valuesMap;
	}
	@Override
	public String[] getAllColumns() {
		return ALL_COLUMNS;
	}
	public Solvent getSolvent() {
		return solvent;
	}
	public void setSolvent(Solvent solvent) {
		this.solvent = solvent;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
}
