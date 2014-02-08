/**
 * This class instantiate only when need
 * to avoid use a lot of memory then the DB is already created and populated
 */

package org.demidenko05.android.chromatography.sqlite;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import org.demidenko05.android.chromatography.model.Analyte;
import org.demidenko05.android.chromatography.model.Column;
import org.demidenko05.android.chromatography.model.DbColumnType;
import org.demidenko05.android.chromatography.model.Detector;
import org.demidenko05.android.chromatography.model.SeriesSolvents;
import org.demidenko05.android.chromatography.model.SeriesBody;
import org.demidenko05.android.chromatography.model.SeriesHead;
import org.demidenko05.android.chromatography.model.Solvent;
import org.demidenko05.android.chromatography.model.TableDescriptor;
import android.database.sqlite.SQLiteDatabase;

public class DbCreator {
	
	private Set<TableDescriptor> tables = new HashSet<TableDescriptor>();
		
	public DbCreator() {
		tables.add(new Column());
		tables.add(new Solvent());
		tables.add(new Detector());
		tables.add(new Analyte());
		tables.add(new SeriesHead());
		tables.add(new SeriesBody());
		tables.add(new SeriesSolvents());
	}
	
	public void createAndPopulate(SQLiteDatabase db) {
		//create:
		for(TableDescriptor table : tables) {
			String createQuery = "Create Table " + table.getTableName() + "(";
			boolean itIsFirst = true;
			for(Entry<String, TableDescriptor.ColumnDescriptor> entry : table.getColumsDescriptor().entrySet()) {
				if(!itIsFirst) createQuery += ", ";
				else itIsFirst = false;
				if(entry.getValue().getType().equals(DbColumnType.FOREIGN_KEY))
					createQuery += entry.getValue().getColumnDefinition();
				else
					createQuery += entry.getKey() + " " +entry.getValue().getColumnDefinition();
			}
			createQuery += ");";
			db.execSQL(createQuery);
		}
		//populate:
		DatabaseService databaseService = DatabaseService.getInstance();
		//Columns:
	    Column columnPc = new Column(); columnPc.setName("Primesep C");
	    columnPc.setId(databaseService.insert(db, columnPc));
	    Column columnP100 = new Column(); columnP100.setName("Primesep 100");
	    columnP100.setId(databaseService.insert(db, columnP100));
	    Column columnOblN = new Column(); columnOblN.setName("Obelisc N");
	    columnOblN.setId(databaseService.insert(db, columnOblN));
	    //Detectors:
	    Detector detectUv = new Detector(); detectUv.setName("UV Detection");
	    detectUv.setId(databaseService.insert(db, detectUv));
	    Detector detectelsd = new Detector(); detectelsd.setName("ELSD Detection");
	    detectelsd.setId(databaseService.insert(db, detectelsd));
	    Detector detectMs = new Detector(); detectMs.setName("MS Detection");
	    detectMs.setId(databaseService.insert(db, detectMs));
	    //Analytes:
	    Analyte analyteBnz = new Analyte(); analyteBnz.setName("Benzoquinone");
	    analyteBnz.setId(databaseService.insert(db, analyteBnz));
	    Analyte analyteBenzil = new Analyte(); analyteBenzil.setName("Benzylamine");
	    analyteBenzil.setId(databaseService.insert(db, analyteBenzil));
	    Analyte analyteAcid = new Analyte(); analyteAcid.setName("Aspartic Acid");
	    analyteAcid.setId(databaseService.insert(db, analyteAcid));
	    Analyte analyteSAcid = new Analyte(); analyteSAcid.setName("Succinic Acid");
	    analyteSAcid.setId(databaseService.insert(db, analyteSAcid));
	    Analyte analytePhlm = new Analyte(); analytePhlm.setName("Phenylalanine");
	    analytePhlm.setId(databaseService.insert(db, analytePhlm));
	    Analyte analytePhenol = new Analyte(); analytePhenol.setName("Phenol");
	    analytePhenol.setId(databaseService.insert(db, analytePhenol));
	    Analyte analyteCaffeine = new Analyte(); analyteCaffeine.setName("Caffeine");
	    analyteCaffeine.setId(databaseService.insert(db, analyteCaffeine));
	    //Solvents:
	    Solvent slWater = new Solvent(); slWater.setName("Water");
	    slWater.setId(databaseService.insert(db, slWater));
	    Solvent slAcn = new Solvent(); slAcn.setName("cetonitrile (MeCN, ACN)");
	    slAcn.setId(databaseService.insert(db, slAcn));
	    Solvent slTfa = new Solvent(); slTfa.setName("trifluoroacetic acid (TFA)");
	    slTfa.setId(databaseService.insert(db, slTfa));
	    Solvent slAmmac = new Solvent(); slAmmac.setName("ammonium acetate)");
	    slAmmac.setId(databaseService.insert(db, slAmmac));
	    //Series heads:
	    SeriesHead series1 = new SeriesHead();
	    series1.setName("Dewetting Study 5min");
	    series1.setColumn(columnP100);
	    series1.setDetector(detectUv);
	    series1.setWavelength(270);
	    series1.setId(databaseService.insert(db, series1));
	    SeriesSolvents serSlv1 = new SeriesSolvents();
	    serSlv1.setSolvent(slWater);
	    serSlv1.setSeriesHead(series1);
	    serSlv1.setAmount("100%");    
	    databaseService.insert(db, serSlv1);
	    SeriesBody serBd;
	    for(int i=0;i<=25;i++) {
	    	serBd = new SeriesBody();
	    	serBd.setSeriesHead(series1);
	    	serBd.setInjection(5);
	    	serBd.setAnalyte(analyteBnz);
	    	if(i == 12)
	    		serBd.setValue(850);
	    	else if(i == 11 || i == 13)
	    		serBd.setValue(650);
	    	else if(i == 10 || i == 14)
	    		serBd.setValue(450);
	    	else
	    		serBd.setValue(0);
	    	serBd.setTime(Double.valueOf(i)/5);
	    	databaseService.insert(db, serBd);
	    }
	    SeriesHead series2 = new SeriesHead();
	    series2.setName("Dewetting Study 5 min");
	    series2.setColumn(columnP100);
	    series2.setDetector(detectUv);
	    series2.setWavelength(270);
	    series2.setId(databaseService.insert(db, series2));
	    serSlv1 = new SeriesSolvents();
	    serSlv1.setSolvent(slWater);
	    serSlv1.setSeriesHead(series2);
	    serSlv1.setAmount("85%");    
	    databaseService.insert(db, serSlv1);
	    serSlv1 = new SeriesSolvents();
	    serSlv1.setSolvent(slAcn);
	    serSlv1.setSeriesHead(series2);
	    serSlv1.setAmount("15%");    
	    databaseService.insert(db, serSlv1);
	    for(int i=0;i<=25;i++) {
	    	serBd = new SeriesBody();
	    	serBd.setSeriesHead(series2);
	    	serBd.setInjection(5);
	    	serBd.setAnalyte(analyteBnz);
	    	if(i == 17)
	    		serBd.setValue(600);
	    	else if(i == 16 || i == 18)
	    		serBd.setValue(450);
	    	else if(i == 15 || i == 19)
	    		serBd.setValue(250);
	    	else
	    		serBd.setValue(100);
	    	serBd.setTime(Double.valueOf(i)/5);
	    	databaseService.insert(db, serBd);
	    }
	    SeriesHead series3 = new SeriesHead();
	    series3.setName("Cation Exchange 9min");
	    series3.setColumn(columnPc);
	    series3.setDetector(detectUv);
	    series3.setFlowRate("1.0 mL/min");
	    series3.setWavelength(210);
	    series3.setId(databaseService.insert(db, series3));
	    serSlv1 = new SeriesSolvents();
	    serSlv1.setSolvent(slWater);
	    serSlv1.setSeriesHead(series3);
	    serSlv1.setAmount("60%");    
	    databaseService.insert(db, serSlv1);
	    serSlv1 = new SeriesSolvents();
	    serSlv1.setSolvent(slAcn);
	    serSlv1.setSeriesHead(series3);
	    serSlv1.setAmount("40%");    
	    databaseService.insert(db, serSlv1);
	    serSlv1 = new SeriesSolvents();
	    serSlv1.setSolvent(slTfa);
	    serSlv1.setSeriesHead(series3);
	    serSlv1.setAmount("0.1 pH 2.0");    
	    databaseService.insert(db, serSlv1);
	    for(int i=0;i<=18;i++) {
	    	serBd = new SeriesBody();
	    	serBd.setAnalyte(analyteBenzil);
	    	serBd.setSeriesHead(series3);
	    	if(i == 2)
	    		serBd.setValue(850);
	    	else
	    		serBd.setValue(0);
	    	serBd.setTime(Double.valueOf(i)/2);
	    	databaseService.insert(db, serBd);
	    }
	    for(int i=0;i<=18;i++) {
	    	serBd = new SeriesBody();
	    	serBd.setAnalyte(analyteCaffeine);
	    	serBd.setSeriesHead(series3);
	    	if(i == 4)
	    		serBd.setValue(950);
	    	else
	    		serBd.setValue(0);
	    	serBd.setTime(Double.valueOf(i)/2);
	    	databaseService.insert(db, serBd);
	    }
	    for(int i=0;i<=18;i++) {
	    	serBd = new SeriesBody();
	    	serBd.setAnalyte(analytePhenol);
	    	serBd.setSeriesHead(series3);
	    	if(i == 8)
	    		serBd.setValue(700);
	    	else
	    		serBd.setValue(0);
	    	serBd.setTime(Double.valueOf(i)/2);
	    	databaseService.insert(db, serBd);
	    }
	    SeriesHead series4 = new SeriesHead();
	    series4.setName("Cation Exchange 9min");
	    series4.setColumn(columnPc);
	    series4.setDetector(detectUv);
	    series4.setFlowRate("1.0 mL/min");
	    series4.setWavelength(210);
	    series4.setId(databaseService.insert(db, series4));
	    serSlv1 = new SeriesSolvents();
	    serSlv1.setSolvent(slWater);
	    serSlv1.setSeriesHead(series4);
	    serSlv1.setAmount("60%");    
	    databaseService.insert(db, serSlv1);
	    serSlv1 = new SeriesSolvents();
	    serSlv1.setSolvent(slAcn);
	    serSlv1.setSeriesHead(series4);
	    serSlv1.setAmount("40%");    
	    databaseService.insert(db, serSlv1);
	    serSlv1 = new SeriesSolvents();
	    serSlv1.setSolvent(slAmmac);
	    serSlv1.setSeriesHead(series4);
	    serSlv1.setAmount("20mM pH 5.0");    
	    databaseService.insert(db, serSlv1);
	    for(int i=0;i<=18;i++) {
	    	serBd = new SeriesBody();
	    	serBd.setAnalyte(analyteBenzil);
	    	serBd.setSeriesHead(series4);
	    	if(i == 7)
	    		serBd.setValue(300);
	    	else
	    		serBd.setValue(0);
	    	serBd.setTime(Double.valueOf(i)/2);
	    	databaseService.insert(db, serBd);
	    }
	    for(int i=0;i<=18;i++) {
	    	serBd = new SeriesBody();
	    	serBd.setAnalyte(analyteCaffeine);
	    	serBd.setSeriesHead(series4);
	    	if(i == 5)
	    		serBd.setValue(650);
	    	else
	    		serBd.setValue(0);
	    	serBd.setTime(Double.valueOf(i)/2);
	    	databaseService.insert(db, serBd);
	    }
	    for(int i=0;i<=18;i++) {
	    	serBd = new SeriesBody();
	    	serBd.setAnalyte(analytePhenol);
	    	serBd.setSeriesHead(series4);
	    	if(i == 14)
	    		serBd.setValue(350);
	    	else
	    		serBd.setValue(0);
	    	serBd.setTime(Double.valueOf(i)/2);
	    	databaseService.insert(db, serBd);
	    }
	}
	
	public Set<TableDescriptor> getTables() {
		return tables;
	}

	public void setTables(Set<TableDescriptor> tables) {
		this.tables = tables;
	}

}
