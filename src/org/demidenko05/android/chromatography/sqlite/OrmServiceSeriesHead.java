package org.demidenko05.android.chromatography.sqlite;

import java.util.Map;

import org.demidenko05.android.chromatography.OrmServicesFactory;
import org.demidenko05.android.chromatography.model.Column;
import org.demidenko05.android.chromatography.model.DbColumnType;
import org.demidenko05.android.chromatography.model.Detector;
import org.demidenko05.android.chromatography.model.SeriesHead;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OrmServiceSeriesHead extends OrmServiceEntityWithName<SeriesHead> {

	public OrmServiceSeriesHead(SQLiteDatabase db) {
		super("series_head", SeriesHead.class, db);
	}

	@Override
	protected ContentValues getValuesMap(SeriesHead entity) {
		ContentValues valuesMap = super.getValuesMap(entity);
		valuesMap.put(COLUMN_ID_COLUMN, entity.getColumn().getId());
		valuesMap.put(COLUMN_ID_DETECTOR, entity.getDetector().getId());
		valuesMap.put(COLUMN_WAVELENGHT, entity.getWavelength());
		valuesMap.put(COLUMN_FLOWRATE, entity.getFlowRate());
		return valuesMap;
	}

	@Override
	public Map<String, OrmService.ColumnDescriptor> getColumsDescriptor() {
		Map<String, OrmService.ColumnDescriptor> columnsDescriptor = super.getColumsDescriptor();
		columnsDescriptor.put(COLUMN_ID_COLUMN, new OrmService.ColumnDescriptor("Integer Not Null", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_ID_DETECTOR, new OrmService.ColumnDescriptor("Integer Not Null", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_WAVELENGHT, new OrmService.ColumnDescriptor("Integer", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_FLOWRATE, new OrmService.ColumnDescriptor("Text", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_ID_COLUMN+"FK", new OrmService.ColumnDescriptor("Foreign Key ("+COLUMN_ID_COLUMN+") REFERENCES "+OrmServicesFactory.getInstance().getOrmService(Column.class).getTableName()+" ("+COLUMN_ID+")", DbColumnType.FOREIGN_KEY));
		columnsDescriptor.put(COLUMN_ID_DETECTOR+"FK", new OrmService.ColumnDescriptor("Foreign Key ("+COLUMN_ID_DETECTOR+") REFERENCES "+OrmServicesFactory.getInstance().getOrmService(Detector.class).getTableName()+" ("+COLUMN_ID+")", DbColumnType.FOREIGN_KEY));
		return columnsDescriptor;
	}

	@Override
	protected void fillEntity(SeriesHead entity, Cursor cursor) {
		super.fillEntity(entity, cursor);
		entity.setColumn(OrmServicesFactory.getInstance().getOrmService(Column.class).getEntityById(cursor.getLong(2)));
		entity.setDetector(OrmServicesFactory.getInstance().getOrmService(Detector.class).getEntityById(cursor.getLong(3)));
		entity.setWavelength(cursor.getInt(4));
		entity.setFlowRate(cursor.getString(5));
	}

	@Override
	protected String[] getAllColumns() {
		String[] allColumns={COLUMN_ID, COLUMN_NAME, COLUMN_ID_COLUMN, COLUMN_ID_DETECTOR, COLUMN_WAVELENGHT, COLUMN_FLOWRATE};
		return allColumns;
	}

}
