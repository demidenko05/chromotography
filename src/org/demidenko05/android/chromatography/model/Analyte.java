package org.demidenko05.android.chromatography.model;

import android.database.Cursor;

public class Analyte extends AbstractEntityWithName {
	public static final String TABLE_NAME = "analytes";
	public Analyte() {}
	public Analyte(long id, String name) {
		setId(id);
		setName(name);
	}
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	@Override
	public String[] getAllColumns() {
		return new String[] {COLUMN_ID, COLUMN_NAME};
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractEntityWithName> T createEntity(Cursor cursor,
			T emptyEntity) {
		Analyte entity = new Analyte();
		fillEntity(cursor, entity);
		return (T) entity;
	}
}
