package org.demidenko05.android.chromatography.sqlite;

import java.util.Map;
import org.demidenko05.android.chromatography.OrmServicesFactory;
import org.demidenko05.android.chromatography.model.Analyte;
import org.demidenko05.android.chromatography.model.DbColumnType;
import org.demidenko05.android.chromatography.model.SeriesBody;
import org.demidenko05.android.chromatography.model.SeriesHead;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OrmServiceSeriesBody extends OrmService<SeriesBody> {

	public OrmServiceSeriesBody(SQLiteDatabase db) {
		super("series_body", SeriesBody.class, db);
	}

	@Override
	public Map<String, ColumnDescriptor> getColumsDescriptor() {
		Map<String, ColumnDescriptor> columnsDescriptor = super.getColumsDescriptor();
		columnsDescriptor.put(COLUMN_ID_SERIES, new ColumnDescriptor("Integer Not Null", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_ID_ANALYTE, new ColumnDescriptor("Integer Not Null", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_INJECTION, new ColumnDescriptor("Integer", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_TIME, new ColumnDescriptor("Real Not Null", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_VALUE, new ColumnDescriptor("Real Not Null", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_ID_ANALYTE+"FK", new ColumnDescriptor("Foreign Key ("+COLUMN_ID_ANALYTE+") REFERENCES "+OrmServicesFactory.getInstance().getOrmService(Analyte.class).getTableName()+" ("+COLUMN_ID+")", DbColumnType.FOREIGN_KEY));
		columnsDescriptor.put(COLUMN_ID_SERIES+"FK", new ColumnDescriptor("Foreign Key ("+COLUMN_ID_SERIES+") REFERENCES "+OrmServicesFactory.getInstance().getOrmService(SeriesHead.class).getTableName()+" ("+COLUMN_ID+")", DbColumnType.FOREIGN_KEY));
		return columnsDescriptor;
	}

	@Override
	protected void fillEntity(SeriesBody entity, Cursor cursor) {
		//no need
	}

	@Override
	protected String[] getAllColumns() {
		String[] allColumns = {COLUMN_ID, COLUMN_ID_SERIES, COLUMN_ID_ANALYTE, COLUMN_INJECTION, COLUMN_TIME, COLUMN_VALUE};
		return allColumns;
	}

	@Override
	protected ContentValues getValuesMap(SeriesBody entity) {
		ContentValues valuesMap = new ContentValues();
		valuesMap.put(COLUMN_ID_SERIES, entity.getSeriesHead().getId());
		valuesMap.put(COLUMN_TIME, entity.getTime());
		valuesMap.put(COLUMN_VALUE, entity.getValue());
		valuesMap.put(COLUMN_ID_ANALYTE, entity.getAnalyte().getId());
		valuesMap.put(COLUMN_INJECTION, entity.getInjection());
		return valuesMap;
	}

}
