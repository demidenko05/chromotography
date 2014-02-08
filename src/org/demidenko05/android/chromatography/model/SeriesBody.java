package org.demidenko05.android.chromatography.model;

import java.util.Map;
import android.content.ContentValues;

public class SeriesBody extends AbstractHasSeries {
	public static final String TABLE_NAME = "series_body";
	public static final String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_ID_SERIES, COLUMN_ID_ANALYTE, COLUMN_INJECTION, COLUMN_TIME, COLUMN_VALUE};
	private double time;
	private double value;
	private Analyte analyte;
	private int injection;
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	@Override
	public Map<String, ColumnDescriptor> getColumsDescriptor() {
		Map<String, ColumnDescriptor> columnsDescriptor = super.getColumsDescriptor();
		columnsDescriptor.put(COLUMN_ID_SERIES, new TableDescriptor.ColumnDescriptor("Integer Not Null", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_ID_ANALYTE, new TableDescriptor.ColumnDescriptor("Integer Not Null", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_INJECTION, new TableDescriptor.ColumnDescriptor("Integer", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_TIME, new TableDescriptor.ColumnDescriptor("Real Not Null", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_VALUE, new TableDescriptor.ColumnDescriptor("Real Not Null", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_ID_ANALYTE+"FK", new TableDescriptor.ColumnDescriptor("Foreign Key ("+COLUMN_ID_ANALYTE+") REFERENCES "+Analyte.TABLE_NAME+" ("+COLUMN_ID+")", DbColumnType.FOREIGN_KEY));
		columnsDescriptor.put(COLUMN_ID_SERIES+"FK", new TableDescriptor.ColumnDescriptor("Foreign Key ("+COLUMN_ID_SERIES+") REFERENCES "+SeriesHead.TABLE_NAME+" ("+COLUMN_ID+")", DbColumnType.FOREIGN_KEY));
		return columnsDescriptor;
	}
	@Override
	public ContentValues getValuesMap() {
		ContentValues valuesMap = super.getValuesMap();
		valuesMap.put(COLUMN_TIME, getTime());
		valuesMap.put(COLUMN_VALUE, getValue());
		valuesMap.put(COLUMN_ID_ANALYTE, getAnalyte().getId());
		valuesMap.put(COLUMN_INJECTION, getInjection());
		return valuesMap;
	}
	@Override
	public String[] getAllColumns() {
		return ALL_COLUMNS;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public Analyte getAnalyte() {
		return analyte;
	}
	public void setAnalyte(Analyte analyte) {
		this.analyte = analyte;
	}
	public int getInjection() {
		return injection;
	}
	public void setInjection(int injection) {
		this.injection = injection;
	}
}
