package org.demidenko05.android.chromatography.model;

import android.database.Cursor;

public class Column extends AbstractEntityWithName {
	public static final String TABLE_NAME = "columns";
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractEntityWithName> T createEntity(Cursor cursor,
			T emptyEntity) {
		Column entity = new Column();
		fillEntity(cursor, entity);
		return (T) entity;
	}
}
