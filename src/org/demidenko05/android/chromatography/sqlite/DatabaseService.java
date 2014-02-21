package org.demidenko05.android.chromatography.sqlite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static org.demidenko05.android.chromatography.sqlite.OrmService.*;
import org.demidenko05.android.chromatography.OrmServicesFactory;
import org.demidenko05.android.chromatography.exception.IncompleteDataException;
import org.demidenko05.android.chromatography.exception.SeriesNotSameDurationException;
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
		
	public void fillSeriesAnalyteList(SQLiteDatabase db,  List<Analyte> listSolvents, long idSeriesHead) {
		String queryStr = "Select a." + COLUMN_NAME + //0
				", Min(s." + COLUMN_INJECTION + ")" +//1
				", Min(a." + COLUMN_ID + ")" +//2
				" From " + OrmServicesFactory.getInstance().getOrmService(Analyte.class).getTableName() + " a" +
				" Join " + OrmServicesFactory.getInstance().getOrmService(SeriesBody.class).getTableName()+ " s" +
				" On a." + COLUMN_ID + " = s." +COLUMN_ID_ANALYTE +
				" Where s." +COLUMN_ID_SERIES + " = " + idSeriesHead +
				" Group By a." +COLUMN_NAME ;
		Cursor cursor = db.rawQuery(queryStr, null);
		if(cursor.moveToFirst()) {
			while(!cursor.isAfterLast()) {
				int injection = cursor.getInt(1);
				String add = injection == 0 ? "" : " " + Integer.toString(injection) + "ul";
				Analyte analyte = new Analyte();
				analyte.setName(cursor.getString(0) + add);
				analyte.setId(cursor.getLong(2));
				listSolvents.add(analyte);
				cursor.moveToNext();
			}
		}
	}
	
	public void fillSeriesSolventsList(SQLiteDatabase db,  List<SeriesSolvents> listSolvents,  long idSeriesHead) {
		String queryStr = "Select serslv." + COLUMN_ID + //0
				", slv." + COLUMN_ID + //1
				", slv." + COLUMN_NAME + //2
				", serslv." + COLUMN_AMOUNT_SOLVENT + //3
				" From " + OrmServicesFactory.getInstance().getOrmService(SeriesHead.class).getTableName()+ " s" +
				" Join " + OrmServicesFactory.getInstance().getOrmService(SeriesSolvents.class).getTableName()+ " serslv" +
				" On s." + COLUMN_ID + " = serslv." + COLUMN_ID_SERIES + 
				" Join " + OrmServicesFactory.getInstance().getOrmService(Solvent.class).getTableName()+ " slv" +
				" On serslv." + COLUMN_ID_SOLVENT + " = slv." + COLUMN_ID + 
				" Where s." +COLUMN_ID + " = " + idSeriesHead;
		Cursor cursor = db.rawQuery(queryStr, null);
		if(cursor.moveToFirst()) {
			while(!cursor.isAfterLast()) {
				SeriesSolvents serSolv = new SeriesSolvents(); 
				serSolv.setId(cursor.getLong(0));
				serSolv.setAmount(cursor.getString(3));
				Solvent solvent = new Solvent();
				solvent.setId(cursor.getLong(1));
				solvent.setName(cursor.getString(2));
				serSolv.setSolvent(solvent);
				SeriesHead serHead = new SeriesHead();
				serHead.setId(idSeriesHead);
				serSolv.setSeriesHead(serHead);
				listSolvents.add(serSolv);
				cursor.moveToNext();
			}
		}
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
				" From " + OrmServicesFactory.getInstance().getOrmService(SeriesHead.class).getTableName()+ " s" +
				" Join " + OrmServicesFactory.getInstance().getOrmService(Column.class).getTableName()+ " c" +
				" On s." + COLUMN_ID_COLUMN + " = c." + COLUMN_ID + 
				" Join " + OrmServicesFactory.getInstance().getOrmService(Detector.class).getTableName()+ " d" +
				" On s." + COLUMN_ID_DETECTOR + " = d." + COLUMN_ID + 
				" Join " + OrmServicesFactory.getInstance().getOrmService(SeriesSolvents.class).getTableName()+ " serslv" +
				" On s." + COLUMN_ID + " = serslv." + COLUMN_ID_SERIES + 
				" Join " + OrmServicesFactory.getInstance().getOrmService(Solvent.class).getTableName()+ " slv" +
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

	public String[] getTitles(SQLiteDatabase db, Map<String, SeriesHead> seriesToShow) throws SeriesNotSameDurationException, IncompleteDataException {//analytes
		List<String> resultInnm = new ArrayList<String>();
		String queryStr = "Select a." +COLUMN_NAME  + ", Count(s." + COLUMN_ID + "), Min(s." +COLUMN_INJECTION  + "), Min(a." +COLUMN_ID  + ")" +
				" From " + OrmServicesFactory.getInstance().getOrmService(Analyte.class).getTableName() + " a" +
				" Join " + OrmServicesFactory.getInstance().getOrmService(SeriesBody.class).getTableName()+ " s" +
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
					String pref = entry.getKey() == null ? "" : "(" + entry.getKey() + ")";
					resultInnm.add(pref + cursor.getString(0) + " " + injection);
					cursor.moveToNext();
				}
			}
			else throw new IncompleteDataException();
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
				Cursor cursor = db.query(OrmServicesFactory.getInstance().getOrmService(SeriesBody.class).getTableName(),
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
		Cursor cursor = db.query(OrmServicesFactory.getInstance().getOrmService(SeriesBody.class).getTableName(),
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

	public void deleteSeriesBody(SQLiteDatabase db, long idSeries, long idAnalyte) {
		Cursor cursor = db.query(OrmServicesFactory.getInstance().getOrmService(SeriesBody.class).getTableName(),
				new String[]{COLUMN_ID}, 
				COLUMN_ID_ANALYTE + "=" + idAnalyte +
				" And " + COLUMN_ID_SERIES + "=" +idSeries
				, null, null, null, null);
		if(cursor.moveToFirst()) {
			while(!cursor.isAfterLast()) {
				SeriesBody serBody = new SeriesBody();
				serBody.setId(cursor.getLong(0));
				OrmServicesFactory.getInstance().getOrmService(SeriesBody.class).delete(db, serBody);
				cursor.moveToNext();
			}
		}
	}
		
}
