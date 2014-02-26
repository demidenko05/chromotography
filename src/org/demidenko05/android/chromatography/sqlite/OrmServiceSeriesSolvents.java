package org.demidenko05.android.chromatography.sqlite;

import java.util.Map;
import org.demidenko05.android.chromatography.OrmServicesFactory;
import org.demidenko05.android.chromatography.model.DbColumnType;
import org.demidenko05.android.chromatography.model.SeriesHead;
import org.demidenko05.android.chromatography.model.SeriesSolvents;
import org.demidenko05.android.chromatography.model.Solvent;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OrmServiceSeriesSolvents extends OrmService<SeriesSolvents> {

	public OrmServiceSeriesSolvents(SQLiteDatabase db) {
		super("series_solvents", SeriesSolvents.class, db);
	}

	@Override
	public Map<String, ColumnDescriptor> getColumsDescriptor() {
		Map<String, ColumnDescriptor> columnsDescriptor = super.getColumsDescriptor();
		columnsDescriptor.put(COLUMN_AMOUNT_SOLVENT, new ColumnDescriptor("Text Not Null", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_ID_SOLVENT, new ColumnDescriptor("Integer Not Null", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_ID_SERIES, new ColumnDescriptor("Integer Not Null", DbColumnType.FIELD));
		columnsDescriptor.put(COLUMN_ID_SERIES+"FK", new ColumnDescriptor("Foreign Key ("+COLUMN_ID_SERIES+") REFERENCES "+OrmServicesFactory.getInstance().getOrmService(SeriesHead.class).getTableName()+" ("+COLUMN_ID+")", DbColumnType.FOREIGN_KEY));
		columnsDescriptor.put(COLUMN_ID_SOLVENT+"FK", new ColumnDescriptor("Foreign Key ("+COLUMN_ID_SOLVENT+") REFERENCES "+OrmServicesFactory.getInstance().getOrmService(Solvent.class).getTableName()+" ("+COLUMN_ID+")", DbColumnType.FOREIGN_KEY));
		return columnsDescriptor;
	}

	@Override
	protected void fillEntity(SeriesSolvents entity, Cursor cursor) {
		entity.setId(cursor.getLong(0));
		SeriesHead seriesHead = new SeriesHead();
		seriesHead.setId(cursor.getLong(1));
		entity.setSeriesHead(seriesHead);
		entity.setSolvent(OrmServicesFactory.getInstance().getOrmService(Solvent.class).getEntityById(cursor.getLong(2)));
		entity.setAmount(cursor.getString(3));
	}

	@Override
	protected String[] getAllColumns() {
		String[] allColumns = {COLUMN_ID, COLUMN_ID_SERIES, COLUMN_ID_SOLVENT, COLUMN_AMOUNT_SOLVENT};
		return allColumns;
	}

	@Override
	protected ContentValues getValuesMap(SeriesSolvents entity) {
		ContentValues valuesMap = new ContentValues();
		valuesMap.put(COLUMN_ID_SERIES, entity.getSeriesHead().getId());
		valuesMap.put(COLUMN_ID_SOLVENT, entity.getSolvent().getId());
		valuesMap.put(COLUMN_AMOUNT_SOLVENT, entity.getAmount());
		return valuesMap;
	}

}
