package org.demidenko05.android.chromatography;

import java.util.LinkedHashMap;
import java.util.Map;
import org.demidenko05.android.chromatography.model.AbstractEntity;
import org.demidenko05.android.chromatography.model.Analyte;
import org.demidenko05.android.chromatography.model.Column;
import org.demidenko05.android.chromatography.model.Detector;
import org.demidenko05.android.chromatography.model.SeriesBody;
import org.demidenko05.android.chromatography.model.SeriesHead;
import org.demidenko05.android.chromatography.model.SeriesSolvents;
import org.demidenko05.android.chromatography.model.Solvent;
import org.demidenko05.android.chromatography.sqlite.OrmService;
import org.demidenko05.android.chromatography.sqlite.OrmServiceEntityWithName;
import org.demidenko05.android.chromatography.sqlite.OrmServiceSeriesBody;
import org.demidenko05.android.chromatography.sqlite.OrmServiceSeriesHead;
import org.demidenko05.android.chromatography.sqlite.OrmServiceSeriesSolvents;

import android.database.sqlite.SQLiteDatabase;

public class OrmServicesFactory {
	
	private static final OrmServicesFactory instance = new OrmServicesFactory();
	
	
	public static synchronized OrmServicesFactory getInstance() {
		return instance;
	}
	
	@SuppressWarnings("rawtypes")
	public void init(SQLiteDatabase db) {
		ormServices = new LinkedHashMap<Class<? extends AbstractEntity>, OrmService>();
		ormServices.put(Analyte.class, new OrmServiceEntityWithName<Analyte>("analytes", Analyte.class, db));
		ormServices.put(Column.class, new OrmServiceEntityWithName<Column>("columns", Column.class, db));
		ormServices.put(Detector.class, new OrmServiceEntityWithName<Detector>("detectors", Detector.class, db));
		ormServices.put(Solvent.class, new OrmServiceEntityWithName<Solvent>("solvents", Solvent.class, db));
		ormServices.put(SeriesHead.class,  new OrmServiceSeriesHead(db));
		ormServices.put(SeriesSolvents.class, new OrmServiceSeriesSolvents(db));
		ormServices.put(SeriesBody.class, new OrmServiceSeriesBody(db));
	}
	
	@SuppressWarnings("rawtypes")
	private Map<Class<? extends AbstractEntity>, OrmService> ormServices;
	
	@SuppressWarnings({ "unchecked" })
	public <T extends AbstractEntity> OrmService<T> getOrmService(Class<T> entityClass) {
		OrmService<T> ormService = ormServices.get(entityClass);
		if(ormService == null)
			throw new RuntimeException("Can't resolve OrmService for class - " + entityClass);
		return ormService;
	}

	@SuppressWarnings("rawtypes")
	public Map<Class<? extends AbstractEntity>, OrmService> getOrmServices() {
		return ormServices;
	}
	
}
