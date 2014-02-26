package org.demidenko05.android.chromatography.sqlite;

import java.util.Map;
import org.demidenko05.android.chromatography.model.AbstractEntityWithName;
import org.demidenko05.android.chromatography.model.DbColumnType;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OrmServiceEntityWithName  <T extends AbstractEntityWithName> extends OrmService<T> {

	public OrmServiceEntityWithName(String tableName, Class<T> entityClass, SQLiteDatabase db) {
		super(tableName, entityClass, db);
	}

	@Override
	public Map<String, ColumnDescriptor> getColumsDescriptor() {
		Map<String, ColumnDescriptor> columnsDescriptor = super.getColumsDescriptor();
		columnsDescriptor.put(COLUMN_NAME, new ColumnDescriptor("Text Not Null", DbColumnType.FIELD));
		return columnsDescriptor;
	}

	@Override
	protected void fillEntity(T entity, Cursor cursor) {
		entity.setId(cursor.getLong(0));
		entity.setName(cursor.getString(1));
	}

	@Override
	protected String[] getAllColumns() {
		String[] allColumns = {COLUMN_ID, COLUMN_NAME};
		return allColumns;
	}

	@Override
	protected ContentValues getValuesMap(T entity) {
		ContentValues valuesMap = new ContentValues();
		valuesMap.put(COLUMN_NAME, entity.getName());
		return valuesMap;
	}

}
