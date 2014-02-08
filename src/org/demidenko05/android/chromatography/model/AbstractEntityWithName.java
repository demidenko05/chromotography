package org.demidenko05.android.chromatography.model;

import java.util.Map;
import android.content.ContentValues;
import android.database.Cursor;

public abstract class AbstractEntityWithName extends AbstractEntity {
	private String name;
	@Override
	public Map<String, ColumnDescriptor> getColumsDescriptor() {
		Map<String, ColumnDescriptor> columnsDescriptor = super.getColumsDescriptor();
		columnsDescriptor.put(COLUMN_NAME, new TableDescriptor.ColumnDescriptor("Text Not Null", DbColumnType.FIELD));
		return columnsDescriptor;
	}
	@Override
	public String toString() {
		return name;
	}
	@Override
	public ContentValues getValuesMap() {
		ContentValues valuesMap = super.getValuesMap();
		valuesMap.put(COLUMN_NAME, getName());
		return valuesMap;
	}
	@Override
	public String[] getAllColumns() {
		String[] allColumns = {COLUMN_ID, COLUMN_NAME};
		return allColumns;
	}
	protected void fillEntity(Cursor cursor, AbstractEntityWithName entity) {
		entity.setId(cursor.getLong(0));
		entity.setName(cursor.getString(1));
	}
	public abstract <T extends AbstractEntityWithName> T createEntity(Cursor cursor,
			T emptyEntity);
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
