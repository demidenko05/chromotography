package org.demidenko05.android.chromatography.model;

import android.database.Cursor;

public class Solvent extends AbstractEntityWithName {
	public static final String TABLE_NAME = "solvents";
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractEntityWithName> T createEntity(Cursor cursor,
			T emptyEntity) {
		Solvent entity = new Solvent();
		fillEntity(cursor, entity);
		return (T) entity;
	}
}
