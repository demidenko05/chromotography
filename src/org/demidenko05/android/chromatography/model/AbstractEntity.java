package org.demidenko05.android.chromatography.model;

import java.util.LinkedHashMap;
import java.util.Map;
import android.content.ContentValues;

public abstract class AbstractEntity implements TableDescriptor {
	private long id;
	@Override
	public Map<String, ColumnDescriptor> getColumsDescriptor() {
		Map<String, ColumnDescriptor> columnsDescriptor = new LinkedHashMap<String, TableDescriptor.ColumnDescriptor>();
		columnsDescriptor.put(COLUMN_ID, new TableDescriptor.ColumnDescriptor("Integer Primary Key Autoincrement", DbColumnType.PRIMARY_KEY));
		return columnsDescriptor;
	}
	@Override
	public ContentValues getValuesMap() {
		ContentValues valuesMap = new ContentValues();
		return valuesMap;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
