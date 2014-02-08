package org.demidenko05.android.chromatography.model;

import java.util.Map;
import android.content.ContentValues;

public interface TableDescriptor {
	public String getTableName();
	public Map<String, ColumnDescriptor> getColumsDescriptor();
	public String[] getAllColumns();
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
	public ContentValues getValuesMap();
}
