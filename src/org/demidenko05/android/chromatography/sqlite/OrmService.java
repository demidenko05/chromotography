package org.demidenko05.android.chromatography.sqlite;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.demidenko05.android.chromatography.model.AbstractEntity;
import org.demidenko05.android.chromatography.model.DbColumnType;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class OrmService <T extends AbstractEntity> {
	
	private final String tableName;
	private final Class<T> entityClass;
	protected final SQLiteDatabase db;
		
	public OrmService(String tableName, Class<T> entityClass, SQLiteDatabase db) {
		this.tableName = tableName;
		this.entityClass = entityClass;
		this.db = db;
	}
	
	public T getEntityById(long id) {
		T entity = null;
		Cursor cursor;;
		synchronized (db) {
			cursor = db.query(getTableName(),
					getAllColumns(), COLUMN_ID + "=" + id, null, null, null, null);
		}
		if(cursor.moveToFirst()) {
			entity = createEntity(cursor);
		}
		return entity;
	}

	public long insert(T entity) {
	    long id;
		synchronized (db) {
		    id = db.insert(getTableName(), null, getValuesMap(entity));
		}
		return id;
	}

	public void update(T entity) {
		synchronized (db) {
			db.update(getTableName(), getValuesMap(entity), COLUMN_ID+"="+entity.getId(), null);
		}
	}

	public void delete(T entity) {
		synchronized (db) {
			db.delete(getTableName(), COLUMN_ID+"="+entity.getId(), null);
		}
	}

	public List<T> getEntities(String selection, String[] selectionArgs, 
			String groupBy, String having, String orderBy) {
		List<T> list = new ArrayList<T>();
		Cursor cursor;
		synchronized (db) {
			cursor = db.query(getTableName(),
					getAllColumns(), selection, selectionArgs, groupBy, having, orderBy);
		}
		if(cursor.moveToFirst()) {
			while(!cursor.isAfterLast()) {
				list.add(createEntity(cursor));
				cursor.moveToNext();
			}
		}
		return list;
	}

	public void fillEntityList(List<T> list, 
			String selection, String[] selectionArgs, 
			String groupBy, String having, String orderBy) {
		Cursor cursor;
		synchronized (db) {
			cursor = db.query(getTableName(),
					getAllColumns(), selection, selectionArgs, groupBy, having, orderBy);
		}
		if(cursor.moveToFirst()) {
			while(!cursor.isAfterLast()) {
				list.add(createEntity(cursor));
				cursor.moveToNext();
			}
		}
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public String getTableName() {
		return tableName;
	}
	
	protected T createEntity(Cursor cursor) {
		T entity = null;
		try {
			entity = entityClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		fillEntity(entity, cursor);
		return entity;
	}
	
	//to override
	protected abstract void fillEntity(T entity, Cursor cursor);

	protected abstract String[] getAllColumns();

	protected abstract ContentValues getValuesMap(T entity);

	public Map<String, OrmService.ColumnDescriptor> getColumsDescriptor() {
		Map<String, OrmService.ColumnDescriptor> columnsDescriptor = new LinkedHashMap<String, OrmService.ColumnDescriptor>();
		columnsDescriptor.put(COLUMN_ID, new OrmService.ColumnDescriptor("Integer Primary Key Autoincrement", DbColumnType.PRIMARY_KEY));
		return columnsDescriptor;
	}
	
	public static class ColumnDescriptor {
		private String columnDefinition;
		private DbColumnType type;
		public ColumnDescriptor(String columnDefinition, DbColumnType type) {
			this.columnDefinition = columnDefinition;
			this.type = type;
		}
		public String getColumnDefinition() {
			return columnDefinition;
		}
		public void setColumnDefinition(String columnDefinition) {
			this.columnDefinition = columnDefinition;
		}
		public DbColumnType getType() {
			return type;
		}
		public void setType(DbColumnType type) {
			this.type = type;
		}
	}
	public static final String COLUMN_ID = "_id"; 
	public static final String COLUMN_TIME = "_time"; 
	public static final String COLUMN_VALUE = "_value"; 
	public static final String COLUMN_NAME = "_name"; 
	public static final String COLUMN_ID_COLUMN = "id_column"; 
	public static final String COLUMN_ID_DETECTOR = "id_detector"; 
	public static final String COLUMN_ID_ANALYTE = "id_analyte"; 
	public static final String COLUMN_ID_SOLVENT = "id_solvent"; 
	public static final String COLUMN_AMOUNT_SOLVENT = "solvent_amount";
	public static final String COLUMN_WAVELENGHT = "wavelength"; 
	public static final String COLUMN_INJECTION = "injection"; 
	public static final String COLUMN_FLOWRATE = "flow_rate"; 
	public static final String COLUMN_ID_SERIES = "id_series";
}
