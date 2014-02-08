package org.demidenko05.android.chromatography.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.ContentValues;
import android.database.Cursor;

public class SeriesHead extends AbstractEntityWithName {
	public static final String TABLE_NAME = "series_head";
	public static final String[] ALL_COLUMNS={COLUMN_ID, COLUMN_NAME, COLUMN_ID_COLUMN, COLUMN_ID_DETECTOR, COLUMN_WAVELENGHT, COLUMN_FLOWRATE};
	private Column column;
	private Detector detector;
	private int wavelength;
	private String flowRate;
	private Set<Solvent> solvents;
	private List<SeriesBody> seriesBody;
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	@Override
	public Map<String, ColumnDescriptor> getColumsDescriptor() {
		Map<String, ColumnDescriptor> columnsDescriptor = super.getColumsDescriptor();
		columnsDescriptor.put(COLUMN_ID_COLUMN, new TableDescriptor.ColumnDescriptor("Integer Not Null", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_ID_DETECTOR, new TableDescriptor.ColumnDescriptor("Integer Not Null", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_WAVELENGHT, new TableDescriptor.ColumnDescriptor("Integer", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_FLOWRATE, new TableDescriptor.ColumnDescriptor("Text", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_ID_COLUMN+"FK", new TableDescriptor.ColumnDescriptor("Foreign Key ("+COLUMN_ID_COLUMN+") REFERENCES "+Column.TABLE_NAME+" ("+COLUMN_ID+")", DbColumnType.FOREIGN_KEY));
		columnsDescriptor.put(COLUMN_ID_DETECTOR+"FK", new TableDescriptor.ColumnDescriptor("Foreign Key ("+COLUMN_ID_DETECTOR+") REFERENCES "+Detector.TABLE_NAME+" ("+COLUMN_ID+")", DbColumnType.FOREIGN_KEY));
		return columnsDescriptor;
	}
	@Override
	public String[] getAllColumns() {
		return ALL_COLUMNS;
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractEntityWithName> T createEntity(Cursor cursor,//no useful for it
			T emptyEntity) {
		SeriesHead entity = new SeriesHead();
		fillEntity(cursor, entity);
		return (T) entity;
	}
	@Override
	public ContentValues getValuesMap() {
		ContentValues valuesMap = super.getValuesMap();
		valuesMap.put(COLUMN_ID_COLUMN, getColumn().getId());
		valuesMap.put(COLUMN_ID_DETECTOR, getDetector().getId());
		valuesMap.put(COLUMN_WAVELENGHT, getWavelength());
		valuesMap.put(COLUMN_FLOWRATE, getFlowRate());
		return valuesMap;
	}
	public Column getColumn() {
		return column;
	}
	public void setColumn(Column column) {
		this.column = column;
	}
	public Detector getDetector() {
		return detector;
	}
	public void setDetector(Detector detector) {
		this.detector = detector;
	}
	public Set<Solvent> getSolvents() {
		return solvents;
	}
	public void setSolvents(Set<Solvent> solvents) {
		this.solvents = solvents;
	}
	public int getWavelength() {
		return wavelength;
	}
	public void setWavelength(int wavelength) {
		this.wavelength = wavelength;
	}
	public String getFlowRate() {
		return flowRate;
	}
	public void setFlowRate(String flowRate) {
		this.flowRate = flowRate;
	}
	public List<SeriesBody> getSeriesBody() {
		return seriesBody;
	}
	public void setSeriesBody(List<SeriesBody> seriesBody) {
		this.seriesBody = seriesBody;
	}
}
