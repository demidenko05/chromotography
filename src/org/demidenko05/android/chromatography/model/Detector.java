package org.demidenko05.android.chromatography.model;

import android.database.Cursor;

public class Detector extends AbstractEntityWithName {
	public static final String TABLE_NAME = "detectors";
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractEntityWithName> T createEntity(Cursor cursor,
			T emptyEntity) {
		Detector entity = new Detector();
		fillEntity(cursor, entity);
		return (T) entity;
	}
}
