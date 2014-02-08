package org.demidenko05.android.chromatography.sqlite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static org.demidenko05.android.chromatography.model.TableDescriptor.*;

import org.demidenko05.android.chromatography.exception.SeriesNotSameDurationException;
import org.demidenko05.android.chromatography.model.AbstractEntity;
import org.demidenko05.android.chromatography.model.AbstractEntityWithName;
import org.demidenko05.android.chromatography.model.Analyte;
import org.demidenko05.android.chromatography.model.Column;
import org.demidenko05.android.chromatography.model.Detector;
import org.demidenko05.android.chromatography.model.SeriesBody;
import org.demidenko05.android.chromatography.model.SeriesHead;
import org.demidenko05.android.chromatography.model.SeriesSolvents;
import org.demidenko05.android.chromatography.model.Solvent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseService {
		
	private static DatabaseService databaseService = new DatabaseService();
	
	//synchronized no need because activities have same thread
	public static synchronized DatabaseService getInstance() {
		return databaseService;
	}
		
	public long insert(SQLiteDatabase db, AbstractEntity entity) {
	    return db.insert(entity.getTableName(), null, entity.getValuesMap());
	}

	public void update(SQLiteDatabase db, AbstractEntity entity) {
		db.update(entity.getTableName(), entity.getValuesMap(), "Where "+AbstractEntity.COLUMN_ID+"="+entity.getId(), null);
	}

	public void delete(SQLiteDatabase db, AbstractEntity entity) {
		db.delete(entity.getTableName(), "Where "+AbstractEntity.COLUMN_ID+"="+entity.getId(), null);
	}
	
	public <T  extends AbstractEntityWithName> List<T> getEntities(SQLiteDatabase db, T emptyEntity, String selection, String[] selectionArgs, 
			String groupBy, String having, String orderBy) {
		List<T> list = new ArrayList<T>();
		Cursor cursor = db.query(emptyEntity.getTableName(),
				emptyEntity.getAllColumns(), selection, selectionArgs, groupBy, having, orderBy);
		if(cursor.moveToFirst()) {
			while(!cursor.isAfterLast()) {
				list.add(emptyEntity.createEntity(cursor, emptyEntity));
				cursor.moveToNext();
			}
		}
		return list;
	}

	public void fillSeriesList(SQLiteDatabase db,  List<SeriesHead> list,  String selection) {
		String queryStr = "Select s." + COLUMN_ID + //0
				", s." +COLUMN_NAME  + //1
				", s." +COLUMN_WAVELENGHT  + //2
				", s." +COLUMN_FLOWRATE  + //3
				", c." + COLUMN_NAME + //4
				", d." + COLUMN_NAME + //5
				", slv." + COLUMN_NAME + //6
				", serslv." + COLUMN_AMOUNT_SOLVENT + //7
				" From " + SeriesHead.TABLE_NAME+ " s" +
				" Join " + Column.TABLE_NAME+ " c" +
				" On s." + COLUMN_ID_COLUMN + " = c." + COLUMN_ID + 
				" Join " + Detector.TABLE_NAME+ " d" +
				" On s." + COLUMN_ID_DETECTOR + " = d." + COLUMN_ID + 
				" Join " + SeriesSolvents.TABLE_NAME+ " serslv" +
				" On s." + COLUMN_ID + " = serslv." + COLUMN_ID_SERIES + 
				" Join " + Solvent.TABLE_NAME+ " slv" +
				" On serslv." + COLUMN_ID_SOLVENT + " = slv." + COLUMN_ID + 
				selection +
				" Order By s." + COLUMN_ID;
		Cursor cursor = db.rawQuery(queryStr, null);
		if(cursor.moveToFirst()) {
			String name = null;
			SeriesHead serHead = null;
			while(!cursor.isAfterLast()) {
				if(serHead == null || serHead.getId() != cursor.getLong(0)) { //enter into a new SeriesHead
					//save old if exist
					if(serHead != null) {
						serHead.setName(name); list.add(serHead);
					}
					// create new
					serHead = new SeriesHead(); serHead.setId(cursor.getLong(0));
					name = cursor.getString(1) + " " +cursor.getString(4) + " " + replaceNull(cursor.getString(3)) + " " +
							cursor.getString(5) + " " + cursor.getInt(2) + " " +cursor.getString(6) + " " +cursor.getString(7);
				}// else already in a group - just resolve solvents names:
				else 
					name += " " + cursor.getString(6) + " " +cursor.getString(7);
				cursor.moveToNext();
			}
			serHead.setName(name); list.add(serHead); //last entry
		}
	}
	
	private String replaceNull(String in) {
		if(in == null) return "";
		return in;
	}

	public String[] getTitles(SQLiteDatabase db, Map<String, SeriesHead> seriesToShow) throws SeriesNotSameDurationException {//analytes
		List<String> resultInnm = new ArrayList<String>();
		String queryStr = "Select a." +COLUMN_NAME  + ", Count(s." + COLUMN_ID + "), Min(s." +COLUMN_INJECTION  + "), Min(a." +COLUMN_ID  + ")" +
				" From " + Analyte.TABLE_NAME + " a" +
				" Join " + SeriesBody.TABLE_NAME+ " s" +
				" On a." + COLUMN_ID + " = s." +COLUMN_ID_ANALYTE +
				" Where s." +COLUMN_ID_SERIES + " = ?" +
				" Group By a." +COLUMN_NAME ;
		int duration = -99;
		for(Entry<String, SeriesHead> entry : seriesToShow.entrySet()) {
			Cursor cursor = db.rawQuery(queryStr, new String[]{Long.toString(entry.getValue().getId())});
			if(cursor.moveToFirst()) {
				entry.getValue().setSeriesBody(new ArrayList<SeriesBody>());
				while(!cursor.isAfterLast()) {
					if(duration == -99) duration = cursor.getInt(1);
					else if(duration != cursor.getInt(1)) throw new SeriesNotSameDurationException();
					SeriesBody serBody = new SeriesBody();
					Analyte analyte = new Analyte();
					analyte.setId(cursor.getLong(3));
					serBody.setAnalyte(analyte);
					entry.getValue().getSeriesBody().add(serBody);
					String injection = (cursor.getInt(2) == 0 ? "" : cursor.getInt(2) + "ul");
					resultInnm.add("(" + entry.getKey() + ")" + cursor.getString(0) + " " + injection);
					cursor.moveToNext();
				}
			}
		}
		String[] result = new String[resultInnm.size()];
		for(int i=0;i<result.length;i++)
			result[i] = resultInnm.get(i);
		return result;
	}

	public double[][] getYAxisDataList(SQLiteDatabase db, Map<String, SeriesHead> seriesToShow) {
		int size = 0;
		for(Entry<String, SeriesHead> entry : seriesToShow.entrySet()) {
			size += entry.getValue().getSeriesBody().size();
		}
		double[][] result = new double[size][];
		int i = 0;
		for(Entry<String, SeriesHead> entry : seriesToShow.entrySet()) {
			for(SeriesBody serBody : entry.getValue().getSeriesBody()) {
				Cursor cursor = db.query(SeriesBody.TABLE_NAME,
						new String[]{COLUMN_VALUE}, 
						COLUMN_ID_ANALYTE +"=" +serBody.getAnalyte().getId() +
				" And " + COLUMN_ID_SERIES + "=" + entry.getValue().getId()						
						, null, null, null, COLUMN_ID);
				if(cursor.moveToFirst()) {
					result[i] = new double[cursor.getCount()];
					int j = 0;
					while(!cursor.isAfterLast()) {
						result[i][j++] = cursor.getDouble(0);
						cursor.moveToNext();
					}
				}
				i++;
			}
		}
		return result;
	}

	public double[] getXAxisData(SQLiteDatabase db, Map<String, SeriesHead> seriesToShow) {
		//assume that all series must have same X range, consequently get X range from 1st seriesBody
		double[] result = null;
		Cursor cursor = db.query(SeriesBody.TABLE_NAME,
				new String[]{COLUMN_TIME, COLUMN_ID}, 
				COLUMN_ID_ANALYTE + "=" + seriesToShow.get("a").getSeriesBody().get(0).getAnalyte().getId() +
				" And " + COLUMN_ID_SERIES + "=" + seriesToShow.get("a").getId()
				, null, null, null, null);
		if(cursor.moveToFirst()) {
			result = new double[cursor.getCount()];
			int i=0;
				while(!cursor.isAfterLast()) {
				result[i++] = cursor.getDouble(0);
				cursor.moveToNext();
			}
		}
		return result;
	}
		
}
